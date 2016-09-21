package org.smart4j.servletTest;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sherry on 16/9/21.
 */
public class CustomerServiceTest {

    private final CustomerService customerService;


    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init(){
        //TODO  初始化数据库
    }

    @Test
    public void getCustomerListTest(){
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(customerList.size() , 2);
    }

    @Test
    public void getCustomerTest(){
        long id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    @Test
    public void createCustomerTest(){
        Map<String,Object> filedMap = new HashMap<String,Object>();
        filedMap.put("name","customer3");
        filedMap.put("contact","Jhon");
        filedMap.put("telephone","13022332323");
        filedMap.put("email","23@qq.com");
        boolean res = customerService.createCustomer(filedMap);
        Assert.assertTrue(res);
    }

    @Test
    public void updateCustomerTest(){
        long id = 3;
        Map<String,Object> filedMap = new HashMap<String,Object>();
        filedMap.put("name","customer100");
        filedMap.put("contact","Jhons");
        filedMap.put("telephone","13000000001");
        boolean res = customerService.updateCustomer(id,filedMap);
        Assert.assertTrue(res);
    }

    @Test
    public void delCustomerTest(){
        long id = 4;
        boolean res = customerService.delCustomer(id);
        Assert.assertTrue(res);
    }
}
