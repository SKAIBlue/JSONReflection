package com.inursoft.jsonreflection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 2017. 1. 21..
 */
public class JSON {

    private JSON() { }


    public static JSONArray toJSONArray(List list)
    {
        JSONArray array = new JSONArray();
        for(int i = 0 ; i < list.size(); ++i)
        {
            array.put(toJSONObject(list.get(i)));
        }
        return array;
    }

    /**
     * 오브젝트를 JSON으로 변환합니다.
     * @param o
     * @return
     */
    public static JSONObject toJSONObject(Object o)
    {
        Class tClass = o.getClass();
        JSONObject root = new JSONObject();
        Field[] fields = tClass.getFields();

        for(int i = 0 ; i < fields.length ; i+=1)
        {
            Field field = fields[i];
            String name = field.getName();

            try {
                Object val = field.get(o);
                if(isDefaultType(field))
                {
                    root.put(name, val);
                }
                else if(isList(field))
                {
                    List list = (List)val;
                    JSONArray array = new JSONArray();
                    for(int j = 0 ; j < list.size(); j+=1)
                    {
                        Object listValue = list.get(j);
                        if(isDefaultType(listValue))
                        {
                            array.put(listValue);
                        }
                        else
                        {
                            array.put(toJSONObject(listValue));
                        }
                    }
                    root.put(name, array);
                }
                else
                {
                    root.put(name, toJSONObject(val));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return root;
    }




    public static List toList(Class t, String json)
    {
        try {
            return toList(t, new JSONArray(json));
        } catch (JSONException e) {
            System.out.println("JSONList 를 리스트로 변환하는 과정에서 오류가 발생하였습니다.");
            return new ArrayList();
        }
    }



    public static List toList(Class t, JSONArray array)
    {
        List list = new ArrayList();
        int length = array.length();
        try
        {
            for(int i = 0 ; i < length; i+=1)
            {
                list.add(toObject(t, array.getJSONObject(i)));
            }
        }
        catch (JSONException e)
        {
            System.out.println("JSONList 에서 배열 범위를 벗어났습니다.");
        }
        return list;
    }



    public static Object toObject(Class t, String json)
    {
        try {
            return toObject(t, new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Object toObject(Class t, JSONObject json)
    {
        Object o = null;
        try {
            o = t.newInstance();
        } catch (InstantiationException e) {
            System.out.println("인스턴스를 생성할 수 없습니다. 기본 생성자가 필요합니다.");
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        Field[] fields = t.getFields();

        for(int i = 0 ; i < fields.length ; i+=1)
        {
            Field field = fields[i];
            String name = field.getName();
            try {
                Object value = json.get(name);
                String typeName = field.getType().getSimpleName();
                if(isInteger(typeName))
                {
                    field.set(o, value);
                }
                else if(isDouble(typeName))
                {
                    field.set(o, Double.valueOf(value.toString()));
                }
                else if(isLong(typeName))
                {
                    field.set(o, Long.valueOf(value.toString()));
                }
                else if(isBoolean(typeName) || isString(typeName))
                {
                    field.set(o, value);
                }
                else if(isList(typeName))
                {
                    List list = new ArrayList();
                    JSONArray array = (JSONArray)value;
                    int length = array.length();
                    Type innerType = getGenericType(field);
                    for(int j = 0 ; j < length ; j+=1)
                    {
                        list.add(toObject((Class)innerType, (JSONObject)array.get(j)));
                    }
                    field.set(o, list);
                }
                else
                {
                    field.set(o, toObject(field.getType(), (JSONObject)value));
                }
            } catch (JSONException e) {
                System.out.println(String.format("JSON 에서 field 이름 %s를 찾을 수 없습니다.", name));
            } catch (IllegalAccessException e) {
                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));
            } catch (IllegalArgumentException e) {
                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));
            }
        }
        return o;
    }




    /**
     * 오브젝트가 가지는 제네릭의 타입을 가져옵니다
     * @param obj 오브젝트
     * @return 제네릭의 타입
     */
    private static Class getGenericType(Object obj)
    {
        return getGenericType(obj.getClass());
    }



    /**
     * 이 타입이 가지는 제네릭의 타입을 가져옵니다
     * @param tClass 오브젝트
     * @return 제네릭의 타입
     */
    private static Class getGenericType(Class tClass)
    {
        return getGenericType((ParameterizedType) tClass.getGenericSuperclass());
    }


    /**
     * 이 필드가 가지는 제네릭의 타입을 가져옵니다
     * @param field 필드
     * @return 제네릭의 타입
     */
    private static Class getGenericType(Field field)
    {
        return getGenericType((ParameterizedType) field.getGenericType());
    }


    /**
     * 제네릭의 타입을 가져옵니다
     * @param pt
     * @return 제네릭의 타입
     */
    private static Class getGenericType(ParameterizedType pt)
    {
        Type[] t = pt.getActualTypeArguments();
        if(t.length > 0)
        {
            if(t[0] instanceof Class)
            {
                return (Class)t[0];
            }
        }
        return null;
    }


    private static boolean isInteger(String typeName)
    {
        return "Integer".equals(typeName);
    }

    private static boolean isDouble(String typeName)
    {
        return "Double".equals(typeName);
    }

    private static boolean isString(String typeName)
    {
        return "String".equals(typeName);
    }

    private static boolean isBoolean(String typeName)
    {
        return "Boolean".equals(typeName);
    }

    private static boolean isLong(String typeName)
    {
        return "Long".equals(typeName);
    }

    private static boolean isObject(String typeName)
    {
        return "Object".equals(typeName);
    }



    /**
     * 필드가 기본 타입인지 검사합니다
     * @param field 필드
     * @return 기본타입일 경우 true
     */
    private static boolean isDefaultType(Field field)
    {
        return isDefaultType(field.getType().getSimpleName());
    }



    /**
     * 클래스가 기본 타입인지 검사합니다
     * @param tClass 클래스
     * @return 기본 타입일 경우 true
     */
    private static boolean isDefaultType(Class tClass)
    {
        return isDefaultType(tClass.getSimpleName());
    }


    /**
     * 오브젝트가 기본 타입인지 검사합니다
     * @param obj 오브젝트
     * @return 기본 타입일 경우 true
     */
    private static boolean isDefaultType(Object obj)
    {
        return isDefaultType(obj.getClass());
    }





    /**
     * 클래스가 기본 타입인지 검사합니다
     * @param typeName 클래스 이름
     * @return 기본 타입일 경우 true
     */
    private static boolean isDefaultType(String typeName)
    {
        return "Integer".equals(typeName) ||
                "Double".equals(typeName) ||
                "String".equals(typeName) ||
                "Boolean".equals(typeName) ||
                "Long".equals(typeName) ||
                "Object".equals(typeName);
    }



    /**
     * 필드가 리스트인지 검사합니다
     * @param field 필드
     * @return 리스트일 경우 true
     */
    private static boolean isList(Field field)
    {
        return isList(field.getType().getSimpleName());
    }


    /**
     * 오브젝트가 리스트인지 검사합니다
     * @param obj 오브젝트
     * @return 리스트일 경우 true
     */
    private static boolean isList(Object obj)
    {
        return isList(obj.getClass());
    }


    /**
     * 클래스가 리스트인지 검사합니다
     * @param tClass 클래스
     * @return 리스트일 경우 true
     */
    private static boolean isList(Class tClass)
    {
        return isList(tClass.getSimpleName());
    }



    /**
     * 클래스가 리스트인지 검사합니다
     * @param typeName 클래스 이름
     * @return 리스트일 경우 true
     */
    private static boolean isList(String typeName)
    {
        return "List".equals(typeName) || "ArrayList".equals(typeName);
    }


}

