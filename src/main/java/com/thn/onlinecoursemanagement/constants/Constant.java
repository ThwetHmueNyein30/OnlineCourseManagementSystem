package com.thn.onlinecoursemanagement.constants;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
public final class Constant {

    public static final String SMS_ADDRESS="MYTEL";

//    //ewallet_info
//    public static final String EWALLET_QUERY="select * from MYTEL_QUEST_DB.EWALLET_INFO  where owner_id= ?";
//    public static final String EWALLET_UPDATE_QUERY = "update MYTEL_QUEST_DB.EWALLET_INFO  set balance = balance - ? where id = ?";
//
//    //ewallet_history
//    public static final String EWALLET_HISTORY_QUERY="select * from MYTEL_QUEST_DB.EWALLET_INFO  info, MYTEL_QUEST_DB.ewallet_history history where info.id= ? and info.id = history.wallet_id ";
//    public static final String EWALLET_HISTORY_INSERT_QUERY="insert into MYTEL_QUEST_DB.EWALLET_HISTORY  (wallet_id,before_balance,after_balance,reason,created_at,updated_at) values(?,?,?,?,?,?)";

    public static final String BASE_URL = "http://10.201.3.251:9898/smppgw/v1.0/action/submit";


    //ewallet_info
    public static final String EWALLET_INFO_QUERY ="select * from ewallet.EWALLET_INFO  where ownerId= ?";
    public static final String EWALLET_INFO_UPDATE_QUERY = "update ewallet.EWALLET_INFO  set balance = balance - ? where id = ?";
    public static final String EWALLET_INFO_UPDATE_VALUE_QUERY = "update ewallet.EWALLET_INFO  set balance = ? where id = ?";
    public static final String EWALLET_INFO_INSERT_QUERY = "insert into ewallet.EWALLET_INFO (ownerId, createdAt, balance, accountName) values (?, ?,?,?)";

    //ewallet_history
    public static final String EWALLET_HISTORY_QUERY="select * from ewallet.EWALLET_INFO  info, ewallet_history history where info.id= ? and info.id = history.walletId ";
    public static final String EWALLET_HISTORY_INSERT_QUERY="insert into ewallet.EWALLET_HISTORY  (walletId,beforeBalance,afterBalance,reason,createdAt,updatedAt) values(?,?,?,?,?,?)";





}
