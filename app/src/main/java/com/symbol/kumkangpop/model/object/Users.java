package com.symbol.kumkangpop.model.object;

import java.util.ArrayList;
import java.util.List;

public class Users {
    public static String UserName = "";
    public static String UserID = "";
    //LoginDate는 서버시간
    //AppCode는 strings에서
    public static String AndroidID="";
    public static String Model = "";
    public static String PhoneNumber = "";
    public static String DeviceName = "";
    public static String DeviceOS = "";
    //Appversion은 build에서
    public static String Remark = "";
    public static String fromDate="";
    public static String DeptCode="";
    public static String DeptName="";

    public static String CustomerCode="";

    public static int REQUEST_SCRAP=4;
    //스캐너관련

    //권한 리스트
    public static List<Integer> authorityList = new ArrayList<>();
    public static List<String> authorityNameList = new ArrayList<>();

    //true면 비가동상태
    //public static boolean CostCenterStopOperationStatus=true;
}