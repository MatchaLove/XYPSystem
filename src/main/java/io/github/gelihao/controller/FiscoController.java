//package io.github.gelihao.controller;
//
//import io.github.gelihao.entity.FiscoBcos;
//import org.fisco.bcos.sdk.BcosSDK;
//import org.fisco.bcos.sdk.client.Client;
//import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class FiscoController {
//    @Autowired
//    private FiscoBcos fiscoBcos;
//
//    @RequestMapping("/fisco")
//    public String Number(){
//        BcosSDK bcosSDK = fiscoBcos.getBcosSDK();
//        Client client = bcosSDK.getClient(Integer.valueOf(1));
//        BlockNumber blockNumber = client.getBlockNumber();
//
//        return  blockNumber.getBlockNumber().toString();
//    }
//}
