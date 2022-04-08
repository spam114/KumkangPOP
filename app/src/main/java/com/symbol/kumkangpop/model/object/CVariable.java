package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class CVariable {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("PCCode")
    public String PCCode="";

    @SerializedName("PCName")
    public String PCName="";

    @SerializedName("GboutSourcing")
    public boolean GboutSourcing=false;

    @SerializedName("UserID")
    public String UserID="";

    @SerializedName("UserName")
    public String UserName="";

    @SerializedName("CustomerCode")
    public String CustomerCode="";

    @SerializedName("DeptCode")
    public int DeptCode=-1;

    @SerializedName("BusinessClassCode")
    public int BusinessClassCode;

    @SerializedName("LocationNo")
    public int LocationNo;

    @SerializedName("Language")
    public int Language;


}
