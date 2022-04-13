package io.github.gelihao;


//import io.github.gelihao.contract.HelloWorld;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.client.protocol.response.BcosTransactionReceipt;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderInterface;
import org.fisco.bcos.sdk.transaction.codec.decode.TransactionDecoderService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.junit.Test;

public class BcosSDKTest
{
    // 获取配置文件路径
    public final String configFile = BcosSDKTest.class.getClassLoader().getResource("config-example.toml").getPath();
    @Test
    public void testClient() throws ConfigException {
        // 初始化BcosSDK
        BcosSDK sdk =  BcosSDK.build(configFile);
        // 为群组1初始化client
        Client client = sdk.getClient(Integer.valueOf(1));

        // 获取群组1的块高
        BlockNumber blockNumber = client.getBlockNumber();

        // 向群组1部署HelloWorld合约
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//        HelloWorld helloWorld = null;
//        try {
//            helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
//        } catch (ContractException e) {
//            e.printStackTrace();
//        }
        // 调用HelloWorld合约的get接口
//        String getValue= null;
//        try {
//            getValue = helloWorld.get();
//        } catch (ContractException e) {
//            e.printStackTrace();
//        }
//        // 调用HelloWorld合约的set接口
//        TransactionReceipt receipt = helloWorld.set("Hello, fisco");
//
//        BcosBlock block = client.getBlockByNumber(blockNumber.getBlockNumber(), false); //得到块的信息
//        Object o = block.getBlock().getTransactions().get(0).get(); //在块中得到交易哈希
//        Object b = block.getBlock().getHash();//在块中得到区块哈希
//        Object c=block.getBlock().getNumber();//当前块高
//        BcosTransactionReceipt transactionReceipt = client.getTransactionReceipt((String) o); //通过交易哈希得到交易回执
//        CryptoSuite cryptoSuite = client.getCryptoSuite();        // 获取当前群组对应的密码学接口
//        // 构造TransactionDecoderService实例，传入是否密钥类型参数。 事务解码器接口
//        TransactionDecoderInterface decoder = new TransactionDecoderService(cryptoSuite);
//        String setValue = decoder.decodeReceiptMessage(transactionReceipt.getResult().getInput());
//        System.out.println("getValue:"+getValue);
//        System.out.println("setValue:"+setValue);
//        System.out.println("receipt:"+receipt);
    }
}

