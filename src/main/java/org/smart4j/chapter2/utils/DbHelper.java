package org.smart4j.chapter2.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by sherry on 16/9/21.
 */
public class DbHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);

    private static final QueryRunner QUERY_RUNNER;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new BasicDataSource();

        Properties props = PropsUtil.loadProperties("jdbc.properties");
        String driver = PropsUtil.getStr(props, "jdbc.driver");
        String url = PropsUtil.getStr(props, "jdbc.url");
        String userName = PropsUtil.getStr(props, "jdbc.username");
        String password = PropsUtil.getStr(props, "jdbc.password");

        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(userName);
        DATA_SOURCE.setPassword(password);

    }

    /**
     * 查询实体表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList = null;
        Connection con = getConnection();
        try {
            entityList = QUERY_RUNNER.query(con, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体表
     */
    public static <T>T queryEntity(Class<T> entityClass,long id){
        T entity = null;
        String sql = "SELECT * FROM "+getTableName(entityClass)+" WHERE ID = ?";
        try {
            entity = QUERY_RUNNER.query(getConnection(),sql,new BeanHandler<T>(entityClass),id);
        } catch (SQLException e) {
            LOGGER.error("query entity failure ",e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> executeQuery(String sql,Object params){
        List<Map<String, Object>> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(getConnection(),sql,new MapListHandler(),params);
        } catch (SQLException e) {
            LOGGER.error("execute query sql failure ",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 执行更新、删除
     */
    public static int executeUpdate(String sql,Object... params){
        Connection con = getConnection();
        int res;
        try {
            res = QUERY_RUNNER.update(con, sql, params);
        } catch (SQLException e) {
            LOGGER.error("update sql failure ",e);
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entity,Map<String,Object> filedMap){
        if(CollectionUtil.isEmpty(filedMap)){
            LOGGER.error("cant not insert entity : filedMap is empty");
            return false;
        }

        String sql = "INSERT INTO "+getTableName(entity);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder(("("));
        for(String fileName:filedMap.keySet()){
            columns.append(fileName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");

        sql += columns+" VALUES "+values;
        Object[] params = filedMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entity,long id,Map<String,Object> filedMap){
        if(CollectionUtil.isEmpty(filedMap)){
            LOGGER.error("cant not insert entity : filedMap is empty");
            return false;
        }

        String sql = "UPDATE "+getTableName(entity)+" SET ";
        StringBuilder columns = new StringBuilder();
        for(String fileName:filedMap.keySet()){
            columns.append(fileName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE ID = ? ";

        List list = new ArrayList(filedMap.values());
        list.add(id);
        Object[] params = list.toArray();

        return executeUpdate(sql,params)==1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean delEntity(Class<T> entity,long id){
        String sql = "DELETE FROM "+getTableName(entity)+" WHERE ID = ?";
        System.out.println("sql : "+sql);
        return executeUpdate(sql,id)==1;
    }

    /**
     * 获取实体表名
     */
    public static String getTableName(Class<?> entity){
        return entity.getSimpleName();
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection con = CONNECTION_HOLDER.get();
        try {
            if (null == con) {
                con = DATA_SOURCE.getConnection();
            }
        } catch (SQLException e) {
            LOGGER.error("get connection failure ", e);
            throw new RuntimeException(e);
        } finally {
            CONNECTION_HOLDER.set(con);
        }
        return con;
    }

}
