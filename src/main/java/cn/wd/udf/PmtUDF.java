package cn.wd.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/7/31
 * @Modified by:
 */
public class PmtUDF extends UDF {
    private  final static Logger logger = LoggerFactory.getLogger(Base64Decode.class);

    public Double evaluate(String code){

        String[] cs = code.split(",");

        double monthRate = Double.parseDouble(cs[0]);
        int periods = Integer.parseInt(cs[1]);
        double amount = Double.parseDouble(cs[2]);
        int percise = Integer.parseInt(cs[3]);


        double var = Math.pow(1 + monthRate,periods);
        double pmtAmount = amount * (monthRate * var) / (var - 1);
        return new BigDecimal(pmtAmount).setScale(percise, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
