package cn.wd.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/4/10
 * @Modified by:
 */
public class Timestamp2String extends UDF{

    public static String evaluate(String timestamp,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String date = null;

        if("".equals(timestamp)){
            return null;
        }

        if (timestamp.length() == 13){
            date = sdf.format(Long.parseLong(timestamp));
        }else{
            date = sdf.format(Long.parseLong(timestamp)*1000);
        }
        return date;
    }

//    public static void main(String[] arg) {
//        System.out.println(evaluate("1554973741936","yyyy-MM-dd HH:mm:ss"));
//    }
}
