package jjk.csutils.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jjk.csutils.pojo.MyPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: jjk
 * @create: 2020-03-02 11:20
 * @program: cloudStudy
 * @description: json转换
 */

public class JsonSwitch {

    /**
     * json转java对象
     *
     * @param json
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T getJavaObj(String json, Class<T> t) {
        T t1 = JSON.parseObject(json, t);
        return null == t1 ? null : t1;
    }

    public static <T> MyPage<T> getPage(String json, Class<T> t) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer pageSize = Integer.parseInt((String) jsonObject.get("pageSize"));
        Integer currPage = Integer.parseInt((String) jsonObject.get("currPage"));
        String param = jsonObject.get("param").toString();
        T paramObj = JSON.parseObject(param, t);
        MyPage myPage = new MyPage<T>().setCurrPage(currPage).setPageSize(pageSize).setParam(paramObj);
        return myPage;
    }

    /**
     * json根据key
     *
     * @param json
     * @param list
     * @return
     */
    public static Map<String, Object> getJsonByParam(String json, List<String> list) {
        Map<String, Object> map = new HashMap<>();
        for (String p : list) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            Object o = jsonObject.get(p);
            map.put(p, o);
        }
        return map;
    }

    /**
     * 从json串中获取对象，根据key
     *
     * @param json
     * @param key
     * @return
     */
    public static Object getJsonByKey(String json, String key) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Object o = jsonObject.get(key);
        return o;
    }

    /**
     * 对象转换为json字符串
     *
     * @param obj
     * @return
     */
    public static String getJsonString(Object obj) {
        String s = JSONObject.toJSONString(obj);
        return s;
    }
}
