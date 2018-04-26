package com.easy.ms.dynamicdb;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {
    private static ThreadLocal<String> currentDataSourceId = new ThreadLocal<String>();
    public static List<String> dataSourceIds = new ArrayList<String>();

    public static String getCurrentDataSourceId() {
        return currentDataSourceId.get();
    }

    public static void setCurrentDataSourceId(String currentDataSourceId) {
        DynamicDataSourceContextHolder.currentDataSourceId.set(currentDataSourceId);
    }
    
    public static void setDataSourceIds(List<String> dataSourceIds) {
        DynamicDataSourceContextHolder.dataSourceIds = dataSourceIds;
    }
    
    public static void clearDataSourceType() {
        currentDataSourceId.remove();
     }
    
     /**
      * 判断指定DataSrouce当前是否存在
      *
      * @param dataSourceId
      * @return
      * @author SHANHY
      * @create  2016年1月24日
      */
     public static boolean containsDataSource(String dataSourceId){
         return dataSourceIds.contains(dataSourceId);
     }

}
