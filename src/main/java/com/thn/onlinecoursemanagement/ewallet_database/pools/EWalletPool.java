package com.thn.onlinecoursemanagement.ewallet_database.pools;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public class EWalletPool {
    private volatile static EWalletPool instance;
    private final HikariDataSource dataSource;

    public EWalletPool() {
        dataSource=new HikariDataSource();
        dataSource.addDataSourceProperty("url","jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = 10.201.5.36)(PORT = 1521))(CONNECT_DATA =(SERVER = SHARED)(SERVICE_NAME = ent_pdb)))");
        dataSource.addDataSourceProperty("user", "MYTEL_QUEST_DB");
        dataSource.addDataSourceProperty("password", "xMer7sqZg");
        dataSource.addDataSourceProperty("driverType", "oracle.jdbc.driver.OracleDriver");
    }

    public static EWalletPool getInstance(){
        if(instance==null){
            synchronized (EWalletPool.class){
                if(instance == null){
                    instance=new EWalletPool();
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
