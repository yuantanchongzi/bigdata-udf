package cn.wd.udaf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Francis
 * @Description:
 * @TIME: Created on 2019/5/7
 * @Modified by:
 */
public class HiveData2ArrayString extends AbstractGenericUDAFResolver {

    private static final Logger LOG = LoggerFactory.getLogger(HiveData2ArrayString.class);

    @Override
    //参数校验
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters)
            throws SemanticException {
        return new GenericUdafMeberLevelEvaluator();
    }
    //处理类
    public static class GenericUdafMeberLevelEvaluator extends GenericUDAFEvaluator {
        private PrimitiveObjectInspector inputOI;
        private PrimitiveObjectInspector outputOI;
        private Text result;

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters)
                throws HiveException {
            super.init(m, parameters);

            //init input
            if (m == Mode.PARTIAL1 || m == Mode.COMPLETE){
                LOG.info(" Mode:"+m.toString()+" result has init");
                inputOI = (PrimitiveObjectInspector) parameters[0];
            }
            //init output
            if (m == Mode.PARTIAL2 || m == Mode.FINAL) {
                outputOI = (PrimitiveObjectInspector) parameters[0];
                result = new Text();
                return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
            }else{
                result = new Text();
                return PrimitiveObjectInspectorFactory.writableStringObjectInspector;
            }

        }

        static class SumAgg implements AggregationBuffer {
            boolean empty;
            List value;
        }

        @Override
        //不同节点调用的初始方法
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            SumAgg buffer = new SumAgg();
            reset(buffer);
            return buffer;
        }

        @Override
        //重置方法
        public void reset(AggregationBuffer agg) throws HiveException {
            ((SumAgg) agg).value = new ArrayList<>();
            ((SumAgg) agg).empty = true;
        }

        private boolean warned = false;

        public List String2List(String str_list){
//            StringUtils
            List list = new ArrayList<String>();
            if(str_list.length()>2){
                String pro = str_list.substring(2,str_list.length()-2);

                List list_str = Arrays.asList(pro.split("\\}\\,\\{"));
                list = new ArrayList<String>();
                for(Object li:list_str){
                    String vindex = "{"+li+"}";
                    list.add(vindex);
                }
            }
            return list;
        }

        //迭代处理
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters)
                throws HiveException {
            if (parameters[0] == null) {
                return;
            }
            try {
                String json = PrimitiveObjectInspectorUtils.getString(parameters[0], inputOI);

                (((SumAgg) agg).value).add(json);

            } catch (NumberFormatException e) {
                if (!warned) {
                    warned = true;
                    LOG.warn(getClass().getSimpleName() + " ");
                }
            }

        }

        @Override
        //聚合操作。
        public void merge(AggregationBuffer agg, Object partial) {
            if (partial != null) {
                if (inputOI != null) {
                    String p = PrimitiveObjectInspectorUtils.getString(partial,
                            inputOI);
                    LOG.info("+p:" + p);
                    (((SumAgg) agg).value).addAll(String2List(p));

                } else {
                    String p = PrimitiveObjectInspectorUtils.getString(partial,
                            outputOI);
                    LOG.info("+p:" + p);
                    (((SumAgg) agg).value).addAll(String2List(p));
                }
            }
        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) {
            return terminate(agg);
        }

        @Override
        public Object terminate(AggregationBuffer agg){
            SumAgg myagg = (SumAgg) agg;
            result.set(myagg.value.toString());
            return result;
        }

//        public static void main(String[] arg) {
//            List list = String2List("[{\"g\":\"2021-04-05\",\"f\":\"2021-04-06\"},{\"a\":\"2021-04-07\",\"b\":\"2021-04-08\"}]");
//        }
    }
}