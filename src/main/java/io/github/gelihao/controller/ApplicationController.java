package io.github.gelihao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.gelihao.api.CommonResult;
import io.github.gelihao.contract.ApplicationContact;
import io.github.gelihao.contract.Credit;
import io.github.gelihao.dao.BusinessDao;
import io.github.gelihao.dao.CompanyDao;
import io.github.gelihao.dao.UserDao;
import io.github.gelihao.entity.*;
import io.github.gelihao.service.*;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;
    @Resource
    private ExaminationService examinationService;
    @Resource
    private BusinessDao businessDao;
    @Resource
    private BusinessService businessService;
    @Resource
    private CompanyDao companyDao;
    @Resource
    private UserDao userDao;
    @Resource
    private MaterialService materialService;
    @Resource
    private ConfirmationService confirmationService;

    @PostMapping(value = "/submit")
    public CommonResult<Object> uploadFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("identifier") String identifier,
            @RequestParam("businessid") String businessid
            ) throws IOException {
        System.out.println("identifier"+identifier);
        System.out.println("businessid"+businessid);
        Map<String,Object> data = new HashMap<>();
        StringBuilder filenames = new StringBuilder();
        // 1. 用数组MultipartFile[]来表示多文件,所以遍历数组,对其中的文件进行逐一操作
        for (MultipartFile file : files) {
            // 2. 通过一顿file.getXXX()的操作,获取文件信息。
            // 2.1 这里用文件名举个栗子
            String filename = file.getOriginalFilename();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            filenames.append(filePath);
        }

        Application application = new Application();
        application.setIdentifier(identifier);
        application.setBusinessid(businessid);
        application.setFilepath(String.valueOf(filenames));
        application.setEditdate(new Date());
        application.setIfmain(1);

        QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
        examinationQueryWrapper.eq("identifier",identifier).eq("businessid",businessid);
        Examination examinationOne = examinationService.getOne(examinationQueryWrapper);
        System.out.println("boolean"+Objects.isNull(examinationOne));
        if (Objects.isNull(examinationOne)){
            Examination examination = new Examination();
            examination.setIdentifier(identifier);
            examination.setCompanyname(companyDao.findNameByIdentifier(identifier));
            examination.setBusinessid(businessid);
            examination.setBusinessname(businessDao.findNameById(businessid));
            examination.setStatus(0);
            examination.setEditdate(new Date());
            examinationService.save(examination);
        }else{
            return CommonResult.failed("请勿重复申请");
        }
        applicationService.save(application);
        return CommonResult.success(data);
    }

    @PostMapping(value = "/submitsecond")
    public CommonResult<Object> uploadSecondFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("identifier") String identifier,
            @RequestParam("businessid") String businessid
    ) throws IOException {

        Map<String,Object> data = new HashMap<>();
        StringBuilder filenames = new StringBuilder();
        // 1. 用数组MultipartFile[]来表示多文件,所以遍历数组,对其中的文件进行逐一操作
        for (MultipartFile file : files) {
            // 2. 通过一顿file.getXXX()的操作,获取文件信息。
            // 2.1 这里用文件名举个栗子
            String filename = file.getOriginalFilename();
            filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + filename;
            // 3. 接下来调用方法来保存文件到本地磁盘,返回的是保存后的文件路径
            String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
            filenames.append(filePath);
        }

        Application application = new Application();
        application.setSecondfilepath(String.valueOf(filenames));
        application.setEditdate(new Date());
        application.setIfsecond(1);

        QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
        examinationQueryWrapper.eq("identifier",identifier).eq("businessid",businessid);
        Examination examinationOne = examinationService.getOne(examinationQueryWrapper);
        if (Objects.isNull(examinationOne)){
            return CommonResult.failed("不存在申请记录，请先进行申请");
        }else{
            QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper();
            applicationQueryWrapper.eq("identifier",identifier).eq("businessid",businessid);

            boolean update = applicationService.update(application, applicationQueryWrapper);
            if(update){
                return CommonResult.success();
            }else{
                return CommonResult.failed("提交失败");
            }
        }
    }
    @GetMapping("/getprogress")
    public CommonResult<Object> getProgress(@RequestParam("username") String username){
//                                            @RequestParam("businessid") String businessiden

        String identifier = userDao.findIdentifierByUsername(username);

        List<Map> data = new ArrayList<>();

        QueryWrapper<Application> queryWrapper = new QueryWrapper();
        queryWrapper.eq("identifier", identifier);
        List<Application> applications = applicationService.list(queryWrapper);
        System.out.println(applications.isEmpty());
        if (applications.isEmpty()) {
            Map<String,Object> activity = new HashMap<>();
            activity.put("title", "未申请");
            activity.put("icon", "el-icon-more");
            activity.put("content", "暂未申请业务");
            activity.put("type", "peimary");
            data.add(activity);
            return CommonResult.success(data);
        }else{
            for (Application application : applications) {
                Map<String,Object> activity = new HashMap<>();
                String businessid = application.getBusinessid();
                Date editdate = application.getEditdate();
                DateFormat df = DateFormat.getDateInstance();
                String time = df.format(editdate);
                activity.put("title", "提交申请");
                activity.put("content", "您于" + time + "提交" + businessDao.findNameById(businessid) + "业务申请");
                data.add(activity);

                QueryWrapper<Examination> examinationQueryWrapper = new QueryWrapper();
                examinationQueryWrapper.eq("identifier", identifier).eq("businessid", businessid);
                Examination one = examinationService.getOne(examinationQueryWrapper);
                Map<String,Object> exmAct = new HashMap<>();
                Map<String,Object> exmAct1 = new HashMap<>();
                if (one.getStatus() == 0){
                    exmAct.put("title", "尚未受理");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请尚未受理");
                    exmAct.put("type", "warning");
                    data.add(exmAct);
                }else if (one.getStatus() == 1){
                    exmAct.put("title", "已受理，审查中");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请已受理，正在审查");
                    exmAct.put("type", "success");
                    data.add(exmAct);
                }else if (one.getStatus() == 2){
                    exmAct1.put("title", "已受理，审查中");
                    exmAct1.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请已受理");
                    exmAct1.put("type", "success");
                    data.add(exmAct1);
                    exmAct.put("title", "审查通过");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请已审查通过");
                    exmAct.put("type", "success");
                    data.add(exmAct);
                }else if (one.getStatus() == 3){
                    exmAct1.put("title", "已受理，审查中");
                    exmAct1.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请已受理");
                    exmAct1.put("type", "success");
                    data.add(exmAct1);
                    exmAct.put("title", "审查未通过");
                    exmAct.put("content", "您提交的" + businessDao.findNameById(businessid) + "申请审查未通过");
                    exmAct.put("type", "danger");
                    data.add(exmAct);
                }

            }
        }


        return CommonResult.success(data);//data

    }

    @GetMapping("/getInfo")
    public CommonResult<Object> getInfo(@RequestParam("username") String username){

        String identifier = userDao.findIdentifierByUsername(username);
        String companyname = companyDao.findNameByIdentifier(identifier);
        Integer credit = companyDao.findUserCreditByUsername(username);
        Map<String,Object> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("companyname", companyname);
        data.put("credit", credit);
        List<Business> list = businessService.list();
        data.put("businesses",list);

        QueryWrapper<Examination> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("identifier",identifier).eq("status", 0);
        Examination one = examinationService.getOne(queryWrapper1);
        QueryWrapper<Examination> queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("identifier",identifier).eq("status", 1);
        Examination two = examinationService.getOne(queryWrapper2);
        if(Objects.isNull(one)&&Objects.isNull(two)){
            data.put("ifbusinesses",0);
        }
        return CommonResult.success(data);

    }

    @GetMapping("/getMainFile")
    public CommonResult<Object> getBusinessMainFile(@RequestParam("businessid") String businessid){
        QueryWrapper<Material> queryWrapper = new QueryWrapper();
        queryWrapper.eq("businessid",businessid).eq("type", 1);
        List<Material> list = materialService.list(queryWrapper);
//        Map<String,Object> map = new HashMap<>();
        return CommonResult.success(list);//data

    }

    @GetMapping("/getSecondFile")
    public CommonResult<Object> getSecondFile(@RequestParam("businessid") String businessid){
        QueryWrapper<Material> queryWrapper = new QueryWrapper();
        queryWrapper.eq("businessid",businessid).eq("type", 0);
        List<Material> list = materialService.list(queryWrapper);
//        Map<String,Object> map = new HashMap<>();
        return CommonResult.success(list);//data

    }
    @GetMapping("/getAllFile")
    public CommonResult<Object> getAllFile(@RequestParam("businessid") String businessid){
        QueryWrapper<Material> queryWrapper = new QueryWrapper();
        queryWrapper.eq("businessid",businessid);
        List<Material> list = materialService.list(queryWrapper);
//        Map<String,Object> map = new HashMap<>();
        return CommonResult.success(list);//data

    }

    @GetMapping("/select")
    public CommonResult<Object> getExamination(@RequestParam("businessid") String businessid,
                                               @RequestParam("identifier") String identifier){
        QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper.eq("businessid",businessid).eq("identifier",identifier);
        Application one = applicationService.getOne(applicationQueryWrapper);
        Map<String,Object> data = new HashMap<>();
        data.put("ifmain",one.getIfmain());
        data.put("ifsecond",one.getIfsecond());
        return CommonResult.success(data);//data

    }
    @GetMapping("/chaintest")
    public CommonResult<Object> uptochainTest(@RequestParam("businessid") String businessid,
                                               @RequestParam("identifier") String identifier){
        Application application = new Application();
        application.setBusinessid(businessid);
        application.setIdentifier(identifier);
        application.setEditdate(new Date());
        Integer chainResult = applicationUpToChain(application);
        if (chainResult == 0){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }

    }

    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 项目路径/FileSpace  也就是和src同级
        String fileSpace = System.getProperty("user.dir") + File.separator + "FileSpace";
        String path = fileSpace + File.separator + fileName;
        // 判断父文件夹是否存在
        File file = new File(path);
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public Integer applicationUpToChain(Application application) {
        String configFile = this.getClass().getClassLoader().getResource("config.toml").getPath();
        System.out.println(configFile);
        BcosSDK sdk =  BcosSDK.build(configFile);
        Client client = sdk.getClient(Integer.valueOf(1));
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println("blockNumber:"+blockNumber.getBlockNumber());
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        System.out.println("获取CryptoKeyPair");
        ApplicationContact applicationContact = ApplicationContact.load("0x65fb79627516d6587072e60b8f08115a09ad9c5f", client, cryptoKeyPair);
        System.out.println("ApplicationContactload");
        TransactionReceipt register = applicationContact.register(String.valueOf(application.getId()), application.getIdentifier(), application.getBusinessid(), (new Date()).toString());
        Tuple1<BigInteger> registerOutput = applicationContact.getRegisterOutput(register);
        int result = registerOutput.getValue1().intValue();
        try {
            BigInteger select = applicationContact.select(String.valueOf(application.getId()));
            System.out.println("是否存在"+select);
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.println("identifier"+application.getIdentifier());
        System.out.println("businessid"+application.getBusinessid());

        System.out.println("blockNumber:"+client.getBlockNumber());
        if(result == 0){
            // success
            return 0;
        }else{
            // fail
            return  -1;
        }

    }
}
