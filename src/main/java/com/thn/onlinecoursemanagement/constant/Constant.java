package com.thn.onlinecoursemanagement.constant;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
public final class Constant {

    public static final String SMS_ADDRESS="MYTEL";
    public static final String UPLOAD_FOLDER = "/Users/mobile-5/Downloads/SpringBoot/OnlineCourseManagement/src/main/upload";

    //ewallet_info
    public static final String EWALLET_QUERY="select * from ewallet.ewallet_info where ownerId= ?";
    public static final String EWALLET_UPDATE_QUERY = "update ewallet.ewallet_info set balance = balance - ? where id = ?";

    //ewallet_history
    public static final String EWALLET_HISTORY_QUERY="select * from ewallet.ewallet_info info, ewallet.ewallet_history history where info.id= ? and info.id=history.walletId ";
    public static final String EWALLET_HISTORY_INSERT_QUERY="insert into ewallet.ewallet_history values (?,?,?,?,?,?)";

    public static final String BASE_URL = "http://10.201.3.251:9898/smppgw/v1.0/action/submit";



}
