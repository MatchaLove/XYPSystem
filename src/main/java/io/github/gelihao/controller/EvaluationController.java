package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.contract.Credit;
import io.github.gelihao.contract.HelloWorld;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.Blacklist;
import io.github.gelihao.entity.Confirmation;
import io.github.gelihao.entity.Evaluation;
import io.github.gelihao.entity.User;
import io.github.gelihao.service.BlacklistService;
import io.github.gelihao.service.ConfirmationService;
import io.github.gelihao.service.EvaluationService;
import io.github.gelihao.utils.ZipUtils;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/creditGrading")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;
    @Resource
    private ConfirmationService confirmationService;
    @Resource
    private BlacklistService blacklistService;

    private BcosSDK bcosSDK;

    private Client client;

    private HelloWorld helloWorld;

    private Credit credit;

    @GetMapping("/fetchlist")
    public CommonResult<Object> getEvaluationList(@RequestParam Map listQuery){
        Long current = Long.parseLong((String) listQuery.get("current"));
        Long size = Long.parseLong((String) listQuery.get("size"));

        Page<Evaluation> evaluationPage = evaluationService.pageByListQuery(listQuery);

        Map<String,Object> data = new HashMap<>();
        data.put("total", evaluationPage.getTotal());
        data.put("items", evaluationPage.getRecords());
        return CommonResult.success(data);
    }
    @PostMapping ("/updatecredit")
    public CommonResult<Object> updateCredit(@RequestBody Evaluation evaluation) throws Exception{
        System.out.println(evaluation);

        if(bcosSDK == null){
            initCreditSDK();
        }
        evaluationService.updateById(evaluation);
        Integer upResult = creditUpToChain(evaluation);
        if (upResult == 0){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }


    }

    @GetMapping("/delete")
    public CommonResult<Object> delete(@RequestParam("identifier") String identifier,@RequestParam("companyname") String companyname){
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(companyname), "companyname", companyname);
        boolean remove = evaluationService.remove(queryWrapper);
        if (remove) {
            return CommonResult.success("删除成功");
        }
        else {
            return CommonResult.failed("删除失败");
        }

    }

    @GetMapping("/getdata")
    public CommonResult<Object> getData(@RequestParam("identifier") String identifier){
        QueryWrapper<Confirmation> confirmationQueryWrapper = new QueryWrapper();
        confirmationQueryWrapper.eq("identifier",identifier);
        Confirmation one = confirmationService.getOne(confirmationQueryWrapper);
        System.out.println(one);
        if (Objects.isNull(one)) {
            return CommonResult.failed("该企业暂不存在数据");
        }else {
            BeanMap data = BeanMap.create(one);
            return CommonResult.success(data);
        }

    }

    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("identifier") String identifier,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        // 下载文件
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf");

        QueryWrapper<Confirmation> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier);
        Confirmation one = confirmationService.getOne(queryWrapper);
        String filePath = one.getFilepath();
        File file =new File(filePath);
        FileInputStream in = new FileInputStream(file);
//        ByteArrayInputStream in =new ByteArrayInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] bytes =new byte[BUFFER_SIZE];
        while((in.read(bytes))!=-1){
            out.write(bytes);
        }
        out.flush();
        in.close();
        out.close();

//        ZipUtils.downloadZip(response.getOutputStream(), fileList);

    }

    public Integer creditUpToChain(Evaluation evaluation) {
//        String configFile = this.getClass().getClassLoader().getResource("config.toml").getPath();
//        System.out.println(configFile);
//        BcosSDK sdk =  BcosSDK.build(configFile);
//        System.out.println(sdk.getConfig());
//        Client client = sdk.getClient(Integer.valueOf(1));
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println("blockNumber:"+blockNumber.getBlockNumber());
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        System.out.println("获取CryptoKeyPair");
//        Credit credit = Credit.load("0x483ee26cbbca742b88f24ff117ae075815d5852e",client,cryptoKeyPair);
        System.out.println("creditload");
        TransactionReceipt register = credit.register(evaluation.getIdentifier(), BigInteger.valueOf(evaluation.getCredit()), evaluation.getEvaluator(), (new Date()).toString());
        Tuple1<BigInteger> registerOutput = credit.getRegisterOutput(register);
        int result = registerOutput.getValue1().intValue();
        Tuple2<BigInteger, BigInteger> select = null;
        try {
            select = credit.select(evaluation.getIdentifier());
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.println("identifier"+evaluation.getIdentifier());
        System.out.println("信用"+select.getValue2());
        System.out.println("blockNumber:"+blockNumber.getBlockNumber());
        if(result == 0){
            // success
            return 0;
        }else{
            //fail
            return  -1;
        }

    }

    private void initCreditSDK() throws Exception {
        System.out.println("-----init BcosSDK:------");
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:fisco-config.xml");
        System.out.println("-----init config ok------");
        bcosSDK = context.getBean(BcosSDK.class);
        System.out.println("-----init BcosSDK ok------");
        // 为群组1初始化client
        client = bcosSDK.getClient(Integer.valueOf(1));
        System.out.println("-----init client ok------");
        // 向群组1部署HelloWorld合约
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//        provide = Provide.deploy(client, cryptoKeyPair);
        credit = Credit.deploy(client, cryptoKeyPair);
        System.out.println("-----deploy Credit ok------");
    }

}
