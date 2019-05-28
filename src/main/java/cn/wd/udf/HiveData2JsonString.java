package cn.wd.udf;

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
public class HiveData2JsonString extends UDF {
    private final static Logger logger = LoggerFactory.getLogger(HiveData2JsonString.class);

    public String evaluate(Object[] code) {

        JSONObject jsonObject = new JSONObject();

        int code_len = code.length;

        if (code_len < 2 || code_len % 2 == 1) {
            logger.error("参数为不小于2的偶数 谢谢");
            System.exit(0);
            return null;
        } else {
            for (int i = 0; i < code_len; i++) {

                Object key = code[i];
                Object value = code[++i];

                if(null!=value){
                    jsonObject.put(key.toString(), value.toString());
                }
            }
            return jsonObject.toJSONString();
        }
    }
}
