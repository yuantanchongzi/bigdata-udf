package cn.wd.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/5/7
 * @Modified by:
 */
public class GetDataFromJson extends UDF {

    private final Logger logger = LoggerFactory.getLogger(GetDataFromJson.class);

    public String evaluate(String jsonString,String key){

        JSONObject jsonObject = null;

        try{
            jsonObject = JSON.parseObject(jsonString);
        }catch (Exception e){
            logger.error("JSON格式有问题");
            System.exit(0);
        }

        Object value = jsonObject.get(key);

        if(null!=value){
            return value.toString();
        }else {
            return null;
        }
    }
}
