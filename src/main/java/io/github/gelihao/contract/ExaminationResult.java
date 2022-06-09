package io.github.gelihao.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class ExaminationResult extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061002861002d640100000000026401000000009004565b6101ab565b600061100190508073ffffffffffffffffffffffffffffffffffffffff166356004b6a6040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018060200180602001848103845260148152602001807f745f6578616d696e6174696f6e5f726573756c74000000000000000000000000815250602001848103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001848103825260308152602001807f6964656e7469666965722c627573696e65737369642c6578616d696e61746f7281526020017f2c726573756c742c6564697464617465000000000000000000000000000000008152506040019350505050602060405180830381600087803b15801561016c57600080fd5b505af1158015610180573d6000803e3d6000fd5b505050506040513d602081101561019657600080fd5b81019080805190602001909291905050505050565b611b5b806101ba6000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063104ace0214610051578063fcd7e3c11461022c575b600080fd5b34801561005d57600080fd5b50610216600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102a9565b6040518082815260200191505060405180910390f35b34801561023857600080fd5b50610293600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506116be565b6040518082815260200191505060405180910390f35b60008060008060008060008060009650600095506102c68e6116be565b95506102d0611a40565b94508473ffffffffffffffffffffffffffffffffffffffff166313db93466040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561033657600080fd5b505af115801561034a573d6000803e3d6000fd5b505050506040513d602081101561036057600080fd5b81019080805190602001909291905050509350600086141515610bdb578373ffffffffffffffffffffffffffffffffffffffff1663e942b5168f6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561043d578082015181840152602081019050610422565b50505050905090810190601f16801561046a5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561048a57600080fd5b505af115801561049e573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168e6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6964656e74696669657200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610562578082015181840152602081019050610547565b50505050905090810190601f16801561058f5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156105af57600080fd5b505af11580156105c3573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168c6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6578616d696e61746f7200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561068757808201518184015260208101905061066c565b50505050905090810190601f1680156106b45780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156106d457600080fd5b505af11580156106e8573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168d6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f627573696e657373696400000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156107ac578082015181840152602081019050610791565b50505050905090810190601f1680156107d95780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156107f957600080fd5b505af115801561080d573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168b6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260068152602001807f726573756c740000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156108d15780820151818401526020810190506108b6565b50505050905090810190601f1680156108fe5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561091e57600080fd5b505af1158015610932573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260088152602001807f6564697464617465000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156109f65780820151818401526020810190506109db565b50505050905090810190601f168015610a235780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610a4357600080fd5b505af1158015610a57573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff166331afac368f866040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610b16578082015181840152602081019050610afb565b50505050905090810190601f168015610b435780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b158015610b6357600080fd5b505af1158015610b77573d6000803e3d6000fd5b505050506040513d6020811015610b8d57600080fd5b810190808051906020019092919050505092506001831415610bb25760009650610bd6565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe96505b6113e5565b8373ffffffffffffffffffffffffffffffffffffffff1663e942b5168a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260088152602001807f6564697464617465000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610c9b578082015181840152602081019050610c80565b50505050905090810190601f168015610cc85780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610ce857600080fd5b505af1158015610cfc573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168c6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6578616d696e61746f7200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610dc0578082015181840152602081019050610da5565b50505050905090810190601f168015610ded5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610e0d57600080fd5b505af1158015610e21573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff1663e942b5168b604051","8263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260068152602001807f726573756c740000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610ee5578082015181840152602081019050610eca565b50505050905090810190601f168015610f125780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610f3257600080fd5b505af1158015610f46573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff16637857d7c96040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b158015610fae57600080fd5b505af1158015610fc2573d6000803e3d6000fd5b505050506040513d6020811015610fd857600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663cd30a1d18e6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6964656e74696669657200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156110ab578082015181840152602081019050611090565b50505050905090810190601f1680156110d85780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156110f857600080fd5b505af115801561110c573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff1663cd30a1d18d6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f627573696e657373696400000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156111d05780820151818401526020810190506111b5565b50505050905090810190601f1680156111fd5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561121d57600080fd5b505af1158015611231573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff1663bf2b70a18f86856040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825285818151815260200191508051906020019080838360005b83811015611323578082015181840152602081019050611308565b50505050905090810190601f1680156113505780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b15801561137157600080fd5b505af1158015611385573d6000803e3d6000fd5b505050506040513d602081101561139b57600080fd5b8101908080519060200190929190505050905060018114156113c057600096506113e4565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe96505b5b7f660baf0b8d6c0aecb43eaee52e1738a62747821439a0e3be5996dde45427bd86878f8f8f8f8f8f6040518088815260200180602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019080838360005b8381101561146757808201518184015260208101905061144c565b50505050905090810190601f1680156114945780820380516001836020036101000a031916815260200191505b5087810386528c818151815260200191508051906020019080838360005b838110156114cd5780820151818401526020810190506114b2565b50505050905090810190601f1680156114fa5780820380516001836020036101000a031916815260200191505b5087810385528b818151815260200191508051906020019080838360005b83811015611533578082015181840152602081019050611518565b50505050905090810190601f1680156115605780820380516001836020036101000a031916815260200191505b5087810384528a818151815260200191508051906020019080838360005b8381101561159957808201518184015260208101905061157e565b50505050905090810190601f1680156115c65780820380516001836020036101000a031916815260200191505b50878103835289818151815260200191508051906020019080838360005b838110156115ff5780820151818401526020810190506115e4565b50505050905090810190601f16801561162c5780820380516001836020036101000a031916815260200191505b50878103825288818151815260200191508051906020019080838360005b8381101561166557808201518184015260208101905061164a565b50505050905090810190601f1680156116925780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390a1869750505050505050509695505050505050565b60008060008060006116ce611a40565b93508373ffffffffffffffffffffffffffffffffffffffff16637857d7c96040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561173457600080fd5b505af1158015611748573d6000803e3d6000fd5b505050506040513d602081101561175e57600080fd5b810190808051906020019092919050505092508373ffffffffffffffffffffffffffffffffffffffff1663e8434e3987856040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b8381101561182c578082015181840152602081019050611811565b50505050905090810190601f1680156118595780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b15801561187957600080fd5b505af115801561188d573d6000803e3d6000fd5b505050506040513d60208110156118a357600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663949d225d6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561191a57600080fd5b505af115801561192e573d6000803e3d6000fd5b505050506040513d602081101561194457600080fd5b810190808051906020019092919050505060001415611985577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9450611a37565b8173ffffffffffffffffffffffffffffffffffffffff1663846719e060006040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1580156119f557600080fd5b505af1158015611a09573d6000803e3d6000fd5b505050506040513d6020811015611a1f57600080fd5b81019080805190602001909291905050509050600094505b50505050919050565b600080600061100191508173ffffffffffffffffffffffffffffffffffffffff1663f23f63c96040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825260148152602001807f745f6578616d696e6174696f6e5f726573756c74000000000000000000000000815250602001915050602060405180830381600087803b158015611aea57600080fd5b505af1158015611afe573d6000803e3d6000fd5b505050506040513d6020811015611b1457600080fd5b810190808051906020019092919050505090508092505050905600a165627a7a72305820ba230a914f1bfe97d29a64a20c7e70e5109c36cd749de206371422cdc42242960029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061002861002d640100000000026401000000009004565b6101ab565b600061100190508073ffffffffffffffffffffffffffffffffffffffff1663c92a78016040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018060200180602001848103845260148152602001807f745f6578616d696e6174696f6e5f726573756c74000000000000000000000000815250602001848103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001848103825260308152602001807f6964656e7469666965722c627573696e65737369642c6578616d696e61746f7281526020017f2c726573756c742c6564697464617465000000000000000000000000000000008152506040019350505050602060405180830381600087803b15801561016c57600080fd5b505af1158015610180573d6000803e3d6000fd5b505050506040513d602081101561019657600080fd5b81019080805190602001909291905050505050565b611b5b806101ba6000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635b325d7814610051578063f3bf226f146100ce575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102a9565b6040518082815260200191505060405180910390f35b3480156100da57600080fd5b50610293600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061062b565b6040518082815260200191505060405180910390f35b60008060008060006102b9611a40565b93508373ffffffffffffffffffffffffffffffffffffffff1663c74f8caf6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561031f57600080fd5b505af1158015610333573d6000803e3d6000fd5b505050506040513d602081101561034957600080fd5b810190808051906020019092919050505092508373ffffffffffffffffffffffffffffffffffffffff1663d8ac595787856040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b838110156104175780820151818401526020810190506103fc565b50505050905090810190601f1680156104445780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b15801561046457600080fd5b505af1158015610478573d6000803e3d6000fd5b505050506040513d602081101561048e57600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663d3e9af5a6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561050557600080fd5b505af1158015610519573d6000803e3d6000fd5b505050506040513d602081101561052f57600080fd5b810190808051906020019092919050505060001415610570577fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff9450610622565b8173ffffffffffffffffffffffffffffffffffffffff16633dd2b61460006040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180828152602001915050602060405180830381600087803b1580156105e057600080fd5b505af11580156105f4573d6000803e3d6000fd5b505050506040513d602081101561060a57600080fd5b81019080805190602001909291905050509050600094505b50505050919050565b60008060008060008060008060009650600095506106488e6102a9565b9550610652611a40565b94508473ffffffffffffffffffffffffffffffffffffffff16635887ab246040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156106b857600080fd5b505af11580156106cc573d6000803e3d6000fd5b505050506040513d60208110156106e257600080fd5b81019080805190602001909291905050509350600086141515610f5d578373ffffffffffffffffffffffffffffffffffffffff16631a391cb48f6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260028152602001807f6964000000000000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156107bf5780820151818401526020810190506107a4565b50505050905090810190601f1680156107ec5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561080c57600080fd5b505af1158015610820573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48e6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6964656e74696669657200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b838110156108e45780820151818401526020810190506108c9565b50505050905090810190601f1680156109115780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561093157600080fd5b505af1158015610945573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48c6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6578616d696e61746f7200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610a095780820151818401526020810190506109ee565b50505050905090810190601f168015610a365780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610a5657600080fd5b505af1158015610a6a573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48d6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f627573696e657373696400000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610b2e578082015181840152602081019050610b13565b50505050905090810190601f168015610b5b5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610b7b57600080fd5b505af1158015610b8f573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48b6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260068152602001807f726573756c740000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610c53578082015181840152602081019050610c38565b50505050905090810190601f168015610c805780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610ca057600080fd5b505af1158015610cb4573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260088152602001807f6564697464617465000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015610d78578082015181840152602081019050610d5d565b50505050905090810190601f168015610da55780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b158015610dc557600080fd5b505af1158015610dd9573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff16634c6f30c08f866040518363ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018373ffffffffffffffffffffffffffffffffffffffff16","73ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610e98578082015181840152602081019050610e7d565b50505050905090810190601f168015610ec55780820380516001836020036101000a031916815260200191505b509350505050602060405180830381600087803b158015610ee557600080fd5b505af1158015610ef9573d6000803e3d6000fd5b505050506040513d6020811015610f0f57600080fd5b810190808051906020019092919050505092506001831415610f345760009650610f58565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe96505b611767565b8373ffffffffffffffffffffffffffffffffffffffff16631a391cb48a6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260088152602001807f6564697464617465000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561101d578082015181840152602081019050611002565b50505050905090810190601f16801561104a5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561106a57600080fd5b505af115801561107e573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48c6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6578616d696e61746f7200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015611142578082015181840152602081019050611127565b50505050905090810190601f16801561116f5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561118f57600080fd5b505af11580156111a3573d6000803e3d6000fd5b505050508373ffffffffffffffffffffffffffffffffffffffff16631a391cb48b6040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835260068152602001807f726573756c740000000000000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561126757808201518184015260208101905061124c565b50505050905090810190601f1680156112945780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b1580156112b457600080fd5b505af11580156112c8573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff1663c74f8caf6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561133057600080fd5b505af1158015611344573d6000803e3d6000fd5b505050506040513d602081101561135a57600080fd5b810190808051906020019092919050505091508173ffffffffffffffffffffffffffffffffffffffff1663ae763db58e6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f6964656e74696669657200000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b8381101561142d578082015181840152602081019050611412565b50505050905090810190601f16801561145a5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561147a57600080fd5b505af115801561148e573d6000803e3d6000fd5b505050508173ffffffffffffffffffffffffffffffffffffffff1663ae763db58d6040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001806020018381038352600a8152602001807f627573696e657373696400000000000000000000000000000000000000000000815250602001838103825284818151815260200191508051906020019080838360005b83811015611552578082015181840152602081019050611537565b50505050905090810190601f16801561157f5780820380516001836020036101000a031916815260200191505b509350505050600060405180830381600087803b15801561159f57600080fd5b505af11580156115b3573d6000803e3d6000fd5b505050508473ffffffffffffffffffffffffffffffffffffffff1663664b37d68f86856040518463ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825285818151815260200191508051906020019080838360005b838110156116a557808201518184015260208101905061168a565b50505050905090810190601f1680156116d25780820380516001836020036101000a031916815260200191505b50945050505050602060405180830381600087803b1580156116f357600080fd5b505af1158015611707573d6000803e3d6000fd5b505050506040513d602081101561171d57600080fd5b8101908080519060200190929190505050905060018114156117425760009650611766565b7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe96505b5b7fba5c160b96d21ca972c429f288c76af6b3d6f7def89d0b82999590892e15b546878f8f8f8f8f8f6040518088815260200180602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019080838360005b838110156117e95780820151818401526020810190506117ce565b50505050905090810190601f1680156118165780820380516001836020036101000a031916815260200191505b5087810386528c818151815260200191508051906020019080838360005b8381101561184f578082015181840152602081019050611834565b50505050905090810190601f16801561187c5780820380516001836020036101000a031916815260200191505b5087810385528b818151815260200191508051906020019080838360005b838110156118b557808201518184015260208101905061189a565b50505050905090810190601f1680156118e25780820380516001836020036101000a031916815260200191505b5087810384528a818151815260200191508051906020019080838360005b8381101561191b578082015181840152602081019050611900565b50505050905090810190601f1680156119485780820380516001836020036101000a031916815260200191505b50878103835289818151815260200191508051906020019080838360005b83811015611981578082015181840152602081019050611966565b50505050905090810190601f1680156119ae5780820380516001836020036101000a031916815260200191505b50878103825288818151815260200191508051906020019080838360005b838110156119e75780820151818401526020810190506119cc565b50505050905090810190601f168015611a145780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390a1869750505050505050509695505050505050565b600080600061100191508173ffffffffffffffffffffffffffffffffffffffff166359a48b656040518163ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825260148152602001807f745f6578616d696e6174696f6e5f726573756c74000000000000000000000000815250602001915050602060405180830381600087803b158015611aea57600080fd5b505af1158015611afe573d6000803e3d6000fd5b505050506040513d6020811015611b1457600080fd5b810190808051906020019092919050505090508092505050905600a165627a7a72305820607cd9b2f55cc3ab7052b615763a19f4c028098b14e38c6a034cfb6a43a930050029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"identifier\",\"type\":\"string\"},{\"name\":\"businessid\",\"type\":\"string\"},{\"name\":\"examinator\",\"type\":\"string\"},{\"name\":\"result\",\"type\":\"string\"},{\"name\":\"editdate\",\"type\":\"string\"}],\"name\":\"register\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"id\",\"type\":\"string\"}],\"name\":\"select\",\"outputs\":[{\"name\":\"\",\"type\":\"int256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"ret\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"id\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"identifier\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"businessid\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"examinator\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"result\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"editdate\",\"type\":\"string\"}],\"name\":\"RegisterEvent\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_SELECT = "select";

    public static final Event REGISTEREVENT_EVENT = new Event("RegisterEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected ExaminationResult(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt register(String id, String identifier, String businessid, String examinator, String result, String editdate) {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(new Utf8String(id),
                new Utf8String(identifier),
                new Utf8String(businessid),
                new Utf8String(examinator),
                new Utf8String(result),
                new Utf8String(editdate)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void register(String id, String identifier, String businessid, String examinator, String result, String editdate, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(new Utf8String(id),
                new Utf8String(identifier),
                new Utf8String(businessid),
                new Utf8String(examinator),
                new Utf8String(result),
                new Utf8String(editdate)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRegister(String id, String identifier, String businessid, String examinator, String result, String editdate) {
        final Function function = new Function(
                FUNC_REGISTER,
                Arrays.<Type>asList(new Utf8String(id),
                new Utf8String(identifier),
                new Utf8String(businessid),
                new Utf8String(examinator),
                new Utf8String(result),
                new Utf8String(editdate)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple6<String, String, String, String, String, String> getRegisterInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple6<String, String, String, String, String, String>(

                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (String) results.get(4).getValue(),
                (String) results.get(5).getValue()
                );
    }

    public Tuple1<BigInteger> getRegisterOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_REGISTER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public BigInteger select(String id) throws ContractException {
        final Function function = new Function(FUNC_SELECT,
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public List<RegisterEventEventResponse> getRegisterEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REGISTEREVENT_EVENT, transactionReceipt);
        ArrayList<RegisterEventEventResponse> responses = new ArrayList<RegisterEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RegisterEventEventResponse typedResponse = new RegisterEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.ret = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.id = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.identifier = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.businessid = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.examinator = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.result = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.editdate = (String) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeRegisterEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(REGISTEREVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeRegisterEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(REGISTEREVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static ExaminationResult load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new ExaminationResult(contractAddress, client, credential);
    }

    public static ExaminationResult deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(ExaminationResult.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class RegisterEventEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger ret;

        public String id;

        public String identifier;

        public String businessid;

        public String examinator;

        public String result;

        public String editdate;
    }
}
