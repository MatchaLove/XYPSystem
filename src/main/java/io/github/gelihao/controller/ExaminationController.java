package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.contract.ApplicationContact;
import io.github.gelihao.contract.ExaminationResult;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.entity.*;
import io.github.gelihao.service.ApplicationService;
import io.github.gelihao.service.BlacklistService;
import io.github.gelihao.service.ExaminationService;
import io.github.gelihao.utils.ZipUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/examination")
public class ExaminationController {

    @Resource
    private ExaminationService examinationService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private BlacklistService blacklistService;

    // 材料审核页面
    @GetMapping("/fetchlist")
    public CommonResult<Object> getExaminationList(@RequestParam Map listQuery){

        Page<Examination> examinationPage = examinationService.pageByListQuery(listQuery);
        Map<String,Object> data = new HashMap<>();
        data.put("total", examinationPage.getTotal());
        data.put("items", examinationPage.getRecords());
        return CommonResult.success(data);//data

    }
    @GetMapping("/select")
    public CommonResult<Object> getExamination(@RequestParam("businessid") String businessid,
                                                   @RequestParam("identifier") String identifier){
        QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper<>();
        examinationQueryWrapper.eq("businessid",businessid).eq("identifier",identifier);
        Examination one = examinationService.getOne(examinationQueryWrapper);
        Map<String,Object> data = new HashMap<>();

        if(Objects.isNull(one)){
            data.put("ifexist", 0);
        }else if(one.getStatus()==0||one.getStatus()==1){
            data.put("ifexist", 1);
        }else{
            data.put("ifexist", 0);
        }
        return CommonResult.success(data);//data

    }
    @PostMapping("/updateExaminate")
    public CommonResult<Object> updateExaminate(@RequestBody Examination examination){
        System.out.println(examination);

        examinationService.updateById(examination);
        return CommonResult.success();
    }
    @GetMapping(value = "/download")
    public void downloadFile(
            @RequestParam("identifier") String identifier,
            @RequestParam("businessid") String businessid,
            @RequestParam("type") Integer type, // 0 次要材料 1主要材料
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        response.setHeader("content-type", "application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf");

        QueryWrapper<Application> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier).eq("businessid",businessid);
        Application one = applicationService.getOne(queryWrapper);
        if (type==0){
            String filePath = one.getSecondfilepath();
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
        }else if (type==1){
            String filePath = one.getFilepath();
            File file =new File(filePath);
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] bytes =new byte[BUFFER_SIZE];
            while((in.read(bytes))!=-1){
                out.write(bytes);
            }
            out.flush();
            in.close();
            out.close();
        }else{
            System.out.println("error request");
        }
    }

    @GetMapping("/chaintest")
    public CommonResult<Object> uptochainTest(@RequestParam("businessid") String businessid,
                                              @RequestParam("identifier") String identifier){
        Examination examination = new Examination();
        examination.setBusinessid(businessid);
        examination.setIdentifier(identifier);
        examination.setEditdate(new Date());
        examination.setExaminator("testman");
        examination.setStatus(1);
        Integer chainResult = examinationUpToChain(examination);
        if (chainResult == 0){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }

    }

    public Integer examinationUpToChain(Examination examination) {
        String configFile = this.getClass().getClassLoader().getResource("config.toml").getPath();
        System.out.println(configFile);
        BcosSDK sdk =  BcosSDK.build(configFile);
        Client client = sdk.getClient(Integer.valueOf(1));
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println("blockNumber:"+blockNumber.getBlockNumber());
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        System.out.println("获取CryptoKeyPair");
        ExaminationResult examinationResult = ExaminationResult.load("0xaf8d728aebc86502413724f853f409bb44f826e5", client, cryptoKeyPair);
        System.out.println("ExaminationContactload");
        TransactionReceipt register = examinationResult.register(String.valueOf(examination.getId()),
                examination.getIdentifier(), examination.getBusinessid(), examination.getExaminator(),
                examination.getStatus().toString(), (new Date()).toString());
        Tuple1<BigInteger> registerOutput = examinationResult.getRegisterOutput(register);
        int result = registerOutput.getValue1().intValue();
        try {
            BigInteger select = examinationResult.select(String.valueOf(examination.getId()));
            System.out.println("是否存在"+select);
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.println("identifier"+examination.getIdentifier());
        System.out.println("businessid"+examination.getBusinessid());

        System.out.println("blockNumber:"+client.getBlockNumber().getBlockNumber());
        if(result == 0){
            // success
            return 0;
        }else{
            // fail
            return  -1;
        }

    }
}
