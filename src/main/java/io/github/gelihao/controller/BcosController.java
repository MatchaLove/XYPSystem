package io.github.gelihao.controller;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@ResponseBody
@RestController
public class BcosController {
    @GetMapping("/test")
    public String test(){
        System.out.println("-----test------");
        return "this is bcos demo";
    }
    @GetMapping("/block")
    public String getBlockNumber(){
        System.out.println("-----getBlockNumber getBlockNumber------");
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:fisco-config.xml");
        System.out.println("-----getBlockNumber ClassPathXmlApplicationContext ok------");
        BcosSDK bcosSDK = context.getBean(BcosSDK.class);
        System.out.println("-----getBlockNumber BcosSDK ok------");
        Client client = bcosSDK.getClient(Integer.valueOf(1));
        System.out.println("-----getBlockNumber client ok------");
        BlockNumber blockNumber = client.getBlockNumber();
        return "getBlockNumber: "+blockNumber.getBlockNumber().toString();
//        return "";
    }
}
