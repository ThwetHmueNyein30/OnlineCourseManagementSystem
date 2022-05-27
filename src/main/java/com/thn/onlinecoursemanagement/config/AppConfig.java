package com.thn.onlinecoursemanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created By Tungct at 10/03/2021
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "config")
public class AppConfig {
    String uploadFolder;
    EWallet eWallet;

    Datasource ewalletDatasource;
    public static class EWallet{
        private String infoQuery;
        private String reduceBalanceQuery;
        private String infoUpdateQuery;
        private String insertQuery;
        private String historyQuery;
        private String historyInsert;

        public String getInfoQuery() {
            return infoQuery;
        }

        public void setInfoQuery(String infoQuery) {
            this.infoQuery = infoQuery;
        }

        public String getReduceBalanceQuery() {
            return reduceBalanceQuery;
        }

        public void setReduceBalanceQuery(String reduceBalanceQuery) {
            this.reduceBalanceQuery = reduceBalanceQuery;
        }

        public String getInfoUpdateQuery() {
            return infoUpdateQuery;
        }

        public void setInfoUpdateQuery(String infoUpdateQuery) {
            this.infoUpdateQuery = infoUpdateQuery;
        }

        public String getInsertQuery() {
            return insertQuery;
        }

        public void setInsertQuery(String insertQuery) {
            this.insertQuery = insertQuery;
        }

        public String getHistoryQuery() {
            return historyQuery;
        }

        public void setHistoryQuery(String historyQuery) {
            this.historyQuery = historyQuery;
        }

        public String getHistoryInsert() {
            return historyInsert;
        }

        public void setHistoryInsert(String historyInsert) {
            this.historyInsert = historyInsert;
        }
    }

    public static class Datasource {
        private String url;
        private String user;
        private String password;
        private String driverType;
        private String datasourceClassname;
        private String driverClassname;

        public String getDriverClassname() {
            return driverClassname;
        }

        public void setDriverClassname(String driverClassname) {
            this.driverClassname = driverClassname;
        }

        public String getDatasourceClassname() {
            return datasourceClassname;
        }

        public void setDatasourceClassname(String datasourceClassname) {
            this.datasourceClassname = datasourceClassname;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverType() {
            return driverType;
        }

        public void setDriverType(String driverType) {
            this.driverType = driverType;
        }
    }


}
