pragma solidity>=0.4.24 <0.6.11;

import "./Table.sol";

contract Credit {
    // event
    event RegisterEvent(int256 ret, string identifier, uint256 company_credit,string evaluater);

    constructor() public {
        // 构造函数中创建t_credit表
        createTable();
    }

    function createTable() private {
        TableFactory tf = TableFactory(0x1001);
        // 信用记录表, key : identifier, field : company_credit，evaluater
        // |  信用代码(主键)      |     信用分       |     评价人       |
        // |-------------------- |-------------------|-------------------|
        // |    identifier   |  company_credit  |    evaluater    |
        // |---------------------|-------------------|
        //
        // 创建表
        tf.createTable("t_credit", "identifier", "company_credit,evaluater");
    }

    function openTable() private returns(Table) {
        TableFactory tf = TableFactory(0x1001);
        Table table = tf.openTable("t_credit");
        return table;
    }

    /*
    描述 : 根据信用代码查询信用分
    参数 ：
            identifier : 信用代码

    返回值：
            参数一： 成功返回0, 账户不存在返回-1
            参数二： 第一个参数为0时有效，信用分
    */
    function select(string identifier) public constant returns(int256, uint256) {
        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(identifier, table.newCondition());
        uint256 company_credit = 0;
        if (0 == uint256(entries.size())) {
            return (-1, company_credit);
        } else {
            Entry entry = entries.get(0);
            return (0, uint256(entry.getInt("company_credit")));
        }
    }

    /*
    描述 : 信用注册
    参数 ：
            identifier : 资产账户
            amount  : 资产金额
    返回值：
            0  注册成功
            -2 其他错误
    */
    function register(string identifier, uint256 company_credit, string evaluater) public returns(int256){
        int256 ret_code = 0;
        int256 ret= 0;
        uint256 temp_company_credit = 0;
        // 查询账户是否存在
        (ret, temp_company_credit) = select(identifier);
        Table table = openTable();
        Entry entry = table.newEntry();
        if(ret != 0) {
                       
            entry.set("identifier", identifier);
            entry.set("company_credit", int256(company_credit));
            entry.set("evaluater", evaluater);
            // 插入
            int count1 = table.insert(identifier, entry);
            if (count1 == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        } else {
            // 账户已存在
 
            entry.set("company_credit", company_credit);

            Condition condition = table.newCondition();
            condition.EQ("identifier", identifier);
            condition.EQ("evaluater", evaluater);
            int256 count2 = table.update(identifier, entry, condition);
            if (count2 == 1) {
                // 成功
                ret_code = 0;
            } else {
                // 失败? 无权限或者其他错误
                ret_code = -2;
            }
        }

        emit RegisterEvent(ret_code, identifier, company_credit, evaluater);

        return ret_code;
    }
    
}