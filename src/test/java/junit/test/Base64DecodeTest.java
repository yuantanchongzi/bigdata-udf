package junit.test;

import cn.wd.udf.Base64Decode;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/5/9
 * @Modified by:
 */
public class Base64DecodeTest {
    @Test
    public void Testevaluate(){
        Base64Decode bd = new Base64Decode();
        String result = bd.evaluate("MTU2NTY1ODcwOTM=");
        Assert.assertEquals("15656587093",result);
    }
}
