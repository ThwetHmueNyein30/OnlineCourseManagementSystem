package com.thn.onlinecoursemanagement.database.pools;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public class EwalletPool {
    private volatile static EwalletPool instance;
    private HikariDataSource dataSource;

    public EwalletPool() {
        dataSource=new HikariDataSource();
        dataSource.addDataSourceProperty("url","jdbc:mysql://localhost:3306/ewallet");
        dataSource.addDataSourceProperty("user", "root");
        dataSource.addDataSourceProperty("password", "");
        dataSource.addDataSourceProperty("driverType", "com.mysql.jdbc.Driver");
    }

    public static EwalletPool getInstance(){
        if(instance==null){
            synchronized (EwalletPool.class){
                if(instance == null){
                    instance=new EwalletPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource(){
        return dataSource;
    }
}
