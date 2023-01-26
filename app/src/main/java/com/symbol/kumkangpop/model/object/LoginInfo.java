package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginInfo {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("UserName")
    public String UserName="";

    @SerializedName("DeptCode")
    public int DeptCode;

    @SerializedName("CustomerCode")
    public String CustomerCode="";

    @SerializedName("BusinessClassCode")
    public int BusinessClassCode;

    @SerializedName("GboutSourcing")
    public boolean GboutSourcing;

    @SerializedName("PassWord")
    public String PassWord="";

    @SerializedName("DeptName")
    public String DeptName="";

    @SerializedName("PCCode")
    public String PCCode="";

    @SerializedName("PCName")
    public String PCName="";
    public ArrayList<Authority> AuthorityList;
}
