package io.github.gelihao;


import io.github.gelihao.contract.HelloWorld;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigInteger;

public class BcosSDKTest2
{
    private BcosSDK bcosSDK;
    private Client client;
    private HelloWorld helloWorld;
//    private Provide provide;
//    private Test test;
//    private Build build;

    public void testClient() throws Exception {
//        System.out.println("-----getBlockNumber getBlockNumber------");
//        @SuppressWarnings("resource")
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:fisco-config.xml");
//        System.out.println("-----getBlockNumber ClassPathXmlApplicationContext ok------");
//        BcosSDK bcosSDK = context.getBean(BcosSDK.class);
//        System.out.println("-----getBlockNumber BcosSDK ok------");
//        Client client = bcosSDK.getClient(Integer.valueOf(1));
//        System.out.println("-----getBlockNumber client ok------");
//        BlockNumber blockNumber = client.getBlockNumber();
//        System.out.println("getBlockNumber: "+blockNumber.getBlockNumber().toString());
        System.out.println("-----getHelloworld------");
        if(bcosSDK == null){
            initSDK();
        }
        if(bcosSDK == null || helloWorld == null){
            System.out.println("-----init BcosSDK failed------");
        }
        // 调用HelloWorld合约的get接口
        System.out.println(1111);



//        System.out.println(transactionReceipt);
        System.out.println("------------");
//        TransactionReceipt tra = provide.addSupply("1005", "2", "1", "1", "1", "1", "1");
//        System.out.println(tra);
//        Tuple7<BigInteger, String, String, String, String, String, String> select = provide.select("1005");
//        System.out.println(select);


    }

    private void initSDK() throws Exception {
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
        helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
        System.out.println("-----deploy HelloWorld ok------");
    }

    public static void main(String[] args) throws Exception{
        new BcosSDKTest2().testClient();
    }
}
