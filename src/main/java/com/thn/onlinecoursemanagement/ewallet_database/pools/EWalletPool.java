package com.thn.onlinecoursemanagement.ewallet_database.pools;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
@Slf4j
public class EWalletPool {
    private volatile static EWalletPool instance;
    private final HikariDataSource dataSource;

    public EWalletPool(AppConfig appConfig) {
        log.info("DATABASE USER: {}, PASSWORD: {}, URL: {}", appConfig.getEwalletDatasource().getUser(), appConfig.getEwalletDatasource().getPassword(), appConfig.getEwalletDatasource().getDriverType());
        dataSource=new HikariDataSource();
        dataSource.setJdbcUrl(appConfig.getEwalletDatasource().getUrl());
        dataSource.addDataSourceProperty("user", appConfig.getEwalletDatasource().getUser());
        dataSource.addDataSourceProperty("password", appConfig.getEwalletDatasource().getPassword());
        dataSource.setDriverClassName(appConfig.getEwalletDatasource().getDriverClassname());
    }

    public static EWalletPool getInstance(AppConfig appConfig){
        if(instance==null){
            synchronized (EWalletPool.class){
                if(instance == null){
                    instance=new EWalletPool(appConfig);
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
