package org.smart4j.chapter2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.utils.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sherry on 16/9/21.
 */
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);


    /**
     * 获取用户列表
     */
    public List<Customer> getCustomerList() {
        List<Customer> customerList = new ArrayList<Customer>();
        String sql = "select * from customer";
        customerList = DbHelper.queryEntityList(Customer.class, sql);
        return customerList;
    }

    /**
     * 获取指定用户
     */
    public Customer getCustomer(long id) {
        Customer customer = DbHelper.queryEntity(Customer.class,id);
        return customer;
    }

    /**
     * 创建一个用户
     */
    public boolean createCustomer(Map<String, Object> filedMap) {
        return DbHelper.insertEntity(Customer.class,filedMap);
    }

    /**
     * 更新一个用户
     */
    public boolean updateCustomer(long id, Map<String, Object> filedMap) {
        return DbHelper.updateEntity(Customer.class,id,filedMap);
    }

    /**
     * 删除一个用户
     */
    public boolean delCustomer(long id) {
        return DbHelper.delEntity(Customer.class,id);
    }
}
