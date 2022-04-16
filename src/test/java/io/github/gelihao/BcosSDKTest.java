package io.github.gelihao;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;

public class BcosSDKTest
{
    // 获取配置文件路径
    public final String configFile = BcosSDKTest.class.getClassLoader().getResource("config.toml").getPath();
    public void testClient() throws ConfigException {

        BcosSDK sdk =  BcosSDK.build(configFile);

        Client client = sdk.getClient(Integer.valueOf(1));

        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println(blockNumber.getBlockNumber());

//        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//        HelloWorld helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
//
//        // 调用HelloWorld合约的get接口
//        String getValue = helloWorld.get();
//
//        // 调用HelloWorld合约的set接口
//        TransactionReceipt receipt = helloWorld.set("Hello, fisco");
    }

    public static void main(String[] args) throws Exception{
        new BcosSDKTest().testClient();
    }
}
