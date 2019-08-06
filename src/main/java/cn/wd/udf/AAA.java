package cn.wd.udf;

import org.apache.hadoop.hive.ql.hooks.LineageInfo;
import org.apache.hadoop.hive.ql.parse.SemanticException;

import java.io.IOException;
import java.text.ParseException;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/8/6
 * @Modified by:
 */
public class AAA {

    public static void main(String[] args) throws IOException, ParseException, SemanticException
    {
        String query = args[0];
        LineageInfo lep = new LineageInfo();
        lep.getLineageInfo(query);
        for (String tab : lep.getInputTableList())
        {
            System.out.println("InputTable=" + tab);
        }
        for (String tab : lep.getOutputTableList())
        {
            System.out.println("OutputTable=" + tab);
        }
    }
}
