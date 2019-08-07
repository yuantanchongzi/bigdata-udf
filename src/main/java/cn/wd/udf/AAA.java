package cn.wd.udf;

import java.io.IOException;

import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.tools.LineageInfo;

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





        String query="create table if not exists intfc.intfc_investor_comminity_interface_topic(" +
                "opic_id string) "+" partitioned by (dt int) " + " stored as parquet " ;
        LineageInfo lep = new LineageInfo();
        try {
            lep.getLineageInfo(query);
        } catch (org.apache.hadoop.hive.ql.parse.ParseException e) {
            e.printStackTrace();
        }


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
