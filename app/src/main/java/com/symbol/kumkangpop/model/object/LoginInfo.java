package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class LoginInfo {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("UserName")
    public String UserName="";

    //downloadUrl
    @SerializedName("PassWord")
    public String PassWord="";

    //serverVersion
    @SerializedName("CustomerCode")
    public String CustomerCode="";

    @SerializedName("DeptCode")
    public String DeptCode="";

    @SerializedName("BusinessClassCode")
    public String BusinessClassCode="";

    @SerializedName("DeptName")
    public String DeptName="";
}
