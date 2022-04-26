package io.github.gelihao;

import io.github.gelihao.contract.Asset;
import io.github.gelihao.contract.HelloWorld;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;

import java.math.BigInteger;

public class BcosSDKTest
{
    // 获取配置文件路径
    public final String configFile = BcosSDKTest.class.getClassLoader().getResource("config.toml").getPath();
    public void testClient() throws Exception {

        BcosSDK sdk =  BcosSDK.build(configFile);

        Client client = sdk.getClient(Integer.valueOf(1));

        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println(blockNumber.getBlockNumber());

        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();

        Asset asset = Asset.deploy(client, cryptoKeyPair);
        TransactionReceipt receipt = asset.register("testAccountHello", BigInteger.valueOf(10000));
        Tuple2<BigInteger, BigInteger> result = asset.select("testAccountHello");
//        HelloWorld helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
//
//        // 调用HelloWorld合约的get接口
//        String getValue = helloWorld.get();
//        System.out.println(getValue);
//        System.out.println();
//        // 调用HelloWorld合约的set接口
//        TransactionReceipt receipt = helloWorld.set("Hello, fisco2");
//        getValue = helloWorld.get();
//        System.out.println(getValue);
        System.out.println("result " + result.getValue1() + result.getValue2());
    }

    public static void main(String[] args) throws Exception{
        new BcosSDKTest().testClient();
    }
}
