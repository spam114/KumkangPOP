package com.symbol.kumkangpop.model.object;

import com.symbol.kumkangpop.view.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class Users {
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
    public static String DeptName="";

    public static SoundManager SoundManager;

    public static int REQUEST_SCRAP=4;
    //스캐너관련

    //권한 리스트
    public static ArrayList<Authority> AuthorityList = new ArrayList<>();

    //PC정보
    public static String PCCode = "";
    public static String PCName = "";
    public static boolean GboutSourcing = true; //외주처여부

    //로그인정보
    public static String UserID;
    public static String UserName;
    public static String CustomerCode;  //사업자번호
    public static int DeptCode;
    public static int BusinessClassCode;
    public static int LocationNo;
    public static int Language;

    public static String UserImage;
    public static int ServiceType;//0:금강공업(음성,진천),1:KKM,2:KKV,-1:TEST
    public static String ServiceAddress;
    public static String LoginServiceAddress;

    public static int selectedLocationIndex = 0;
}