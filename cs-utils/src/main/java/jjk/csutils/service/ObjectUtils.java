package jjk.csutils.service;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jjk
 * @create: 2020-03-06 14:22
 * @program: cloudStudy
 * @description: 关于对象操作的通用方法
 */
@Slf4j
public class ObjectUtils {
    /**
     * 根据指定属性判空
     *
     * @param obj
     * @param args
     * @return
     */
    public static boolean checkObjNotNull(Object obj, ArrayList<String> args) {
        boolean flag = true;
        try {
            if (null != obj) {
                if (null != args && args.size() > 0) {
                    List<Field> all = new ArrayList<>();
                    all.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
                    all.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
                    for (String key : args) {
                        for (Field f : all) {
                            if (key.equals(f.getName())){
                                f.setAccessible(true);
                                if (null == f.get(obj) || f.get(obj).equals("")) {
                                    flag = false;
                                    return flag;
                                }
                            }
                        }
                    }
                } else {
                    Field[] fileds = obj.getClass().getDeclaredFields();
                    for (Field f : fileds) {
                        f.setAccessible(true);
                        if (null == f.get(obj) || f.get(obj).equals("")) {
                            flag = false;
                            return flag;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 用于快速构建list集合
     *
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> setArrayList(T... args) {
        if (null != args && args.length > 0) {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                list.add(args[i]);
            }
            return list;
        }
        return null;
    }
}
