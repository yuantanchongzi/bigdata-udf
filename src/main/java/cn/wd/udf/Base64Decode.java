package cn.wd.udf;



import org.apache.hadoop.hive.ql.exec.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;


/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/4/10
 * @Modified by:frnaics
 */

public class Base64Decode extends UDF {

    private  final static Logger logger = LoggerFactory.getLogger(Base64Decode.class);

    public String evaluate(String code){
        BASE64Decoder decoder = new BASE64Decoder();

        String result = null;
        try {
            if("".equals(code)){
                return null;
            }else {
                result = new String(decoder.decodeBuffer(code));
                logger.info("the code is {},the result is {}",code,result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

//    public static void main(String[] arg) {
//        Base64Decode decode = new Base64Decode();
//        decode.evaluate("MTU2NTY1ODcwOTM=");
//    }

}
