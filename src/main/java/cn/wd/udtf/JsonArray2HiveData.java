package udtf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author: Francis
 * @Description: 将jsonArray字符串中的指定key数据提取出来
 * select t1.idno,
 *        detail_list.detail
 * from (select idno,
 *              name,
 *              phone,
 *              request_time,
 *              response_time,
 *              responsecode,
 *              get_json_object(response_result, '$.var_detail') as var_detail   --这个数据是jsonArray字符串
 *       from znjf.ods_t_credit_request_log
 *       where dt = '20230328'
 *         and request_url = 'http://api.prod.xdecision.net/dataservice/md5/ORModel'
 *         and idno = '320602197706060032') t1
 *          lateral view split_json_array(var_detail, 'mobile') detail_list as detail   --这个mobile是key
 * @TIME: Created on 2019/5/9
 * @Modified by:
 */
public class JsonArray2HiveData extends GenericUDTF {

//    private final Logger log = LoggerFactory.getLogger(JsonArray2HiveData.class);

    @Override
    public void process(Object[] args) throws HiveException {
        if (null==args[0]){
            int index=args.length;
            String[] data_desc = new String[index-1];
            for(int i =1;i<index;i++){
                data_desc[i-1]="";
            }
            forward(data_desc);
        }else {
            String input=args[0].toString();

            int index = args.length;

            JSONArray jsonArray = JSON.parseArray(input);
            Iterator it = jsonArray.iterator();

            while (it.hasNext()){
                String[] data_desc = new String[index-1];
                JSONObject json = (JSONObject) it.next();
                for (int i =1;i<index;i++){
                    data_desc[i-1]=null!=json.get(args[i].toString())?json.get(args[i].toString()).toString():"";
                }
                forward(data_desc);
            }
        }
    }

    @Override
    public void close() throws HiveException {

    }

    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
//        if (args.length != 1) {
//            throw new UDFArgumentLengthException("ExplodeMap takes only one argument");
//        }
        if (args[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("ExplodeMap takes string as a parameter");
        }

        ArrayList<String> fieldNames = new ArrayList<>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<>();

        for(int i=1;i<args.length;i++){
            fieldNames.add(args[i].toString());
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        }

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }
}
