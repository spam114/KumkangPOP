package com.symbol.kumkangpop.view;

import com.google.gson.internal.LinkedTreeMap;
import com.symbol.kumkangpop.model.object.AppVersion;
import com.symbol.kumkangpop.model.object.BusinessClass;
import com.symbol.kumkangpop.model.object.StockIn;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Object 형을 원하는 Type으로 변경한다.
 */
public class TypeChanger {

    public static AppVersion chageTypeAppVersion(Object object) {
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
        Field fields[] = AppVersion.class.getFields();
        AppVersion rData = new AppVersion();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(rData, linkedTreeMap.get(fields[i].getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rData;
    }

    public static StockIn chageTypeStockIn(Object object) {
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) object;
        Field fields[] = StockIn.class.getFields();
        StockIn rData = new StockIn();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].set(rData, linkedTreeMap.get(fields[i].getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return rData;
    }

    public static ArrayList<BusinessClass> chageTypeBusinessClassList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<BusinessClass> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = (LinkedTreeMap) arrayList.get(i);
                Field fields[] = BusinessClass.class.getFields();
                BusinessClass rData = new BusinessClass();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static ArrayList<StockIn> chageTypeStockInList(Object object) {
        ArrayList<LinkedTreeMap> arrayList = (ArrayList<LinkedTreeMap>) object;
        ArrayList<StockIn> returnList=new ArrayList<>();
        try {
            for(int i=0;i<arrayList.size();i++){
                LinkedTreeMap linkedTreeMap = arrayList.get(i);
                Field fields[] = StockIn.class.getFields();
                StockIn rData = new StockIn();
                for (int j = 0; j < fields.length; j++) {
                    fields[j].set(rData, linkedTreeMap.get(fields[j].getName()));
                }
                returnList.add(rData);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return returnList;
    }

}
