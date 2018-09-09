import com.alibaba.fastjson.JSONObject;
import com.zwt.framework.es.util.ElasticSearchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/8/30
 * @since 1.0
 */
public class ESTest {
    private static ElasticSearchUtil esUtil=new ElasticSearchUtil();
    private static String index="user_index";
    private static String type="user_type";
    public static void main(String[] args) {
        JSONObject js=new JSONObject();
        js.put("1","2");
        esUtil.insertDocument(index,type,"123",js);
    }
}
