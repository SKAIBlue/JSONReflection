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
 * Created by SKAIBlue(Jong-Woo Park) on 2017-02-27.
 */

public class JSON
{


    private JSON()
    {
    }


    /**
     * Object to json
     *
     * @param o object
     * @return converted JSONObject
     */

    public static JSONObject toJSONObject(Object o)

    {

        Class tClass = o.getClass();

        JSONObject root = new JSONObject();

        Field[] fields = tClass.getFields();


        for (int i = 0; i < fields.length; i += 1)

        {

            Field field = fields[i];

            String name = field.getName();

            if (isIgnore(name))
            {
                continue;
            }

            try
            {

                Object val = field.get(o);

                if (isDefaultType(field))

                {
                    root.put(name, val);
                } else if (isList(field))

                {
                    List list = (List) val;

                    JSONArray array = toJSONArray(list);

                    root.put(name, array);

                } else

                {
                    root.put(name, toJSONObject(val));

                }

            } catch (IllegalAccessException e)
            {

                e.printStackTrace();

            } catch (JSONException e)
            {

                e.printStackTrace();

            }

        }

        return root;

    }


    /**
     * List to JSONArray
     *
     * @param list list
     * @return converted JSONList
     */

    public static JSONArray toJSONArray(List list)

    {

        JSONArray array = new JSONArray();

        for (int i = 0; i < list.size(); i += 1)
        {


            Object listValue = list.get(i);


            if (isDefaultType(listValue))

            {

                array.put(listValue);

            } else
            {

                array.put(toJSONObject(listValue));

            }


        }

        return array;

    }


    /**
     * JSON Array to List
     *
     * @param t    type of list
     * @param json json string
     * @return List
     */

    public static List toList(Class t, String json)

    {

        try
        {

            return toList(t, new JSONArray(json));

        } catch (JSONException e)
        {

            System.out.println("JSONList 를 리스트로 변환하는 과정에서 오류가 발생하였습니다.");

            return new ArrayList();

        }

    }


    private static List toList(Class t, JSONArray array)

    {

        List list = new ArrayList();

        int length = array.length();

        try

        {

            if (isDefaultType(t))

            {

                for (int j = 0; j < length; j += 1)

                {

                    list.add(array.get(j));

                }

            } else

            {

                for (int j = 0; j < length; j += 1)

                {

                    list.add(toObject(t, (JSONObject) array.get(j)));

                }

            }

        } catch (JSONException e)

        {

            System.out.println("JSONList 에서 배열 범위를 벗어났습니다.\n JSONList out of range");

        }

        return list;

    }


    /**
     * JSONObject to Object
     *
     * @param t    type of object
     * @param json json string
     * @return Object
     */

    public static Object toObject(Class t, String json)

    {

        try
        {
            return toObject(t, new JSONObject(json));

        } catch (JSONException e)
        {

            e.printStackTrace();

            return null;

        }

    }


    private static Object toObject(Class t, JSONObject json)

    {

        Object o = null;

        try
        {

            o = t.newInstance();

        } catch (InstantiationException e)
        {

            System.out.println("인스턴스를 생성할 수 없습니다. 기본 생성자가 필요합니다.\n Cannot create instance. need default initializer");

            return null;

        } catch (IllegalAccessException e)
        {

            e.printStackTrace();

            return null;

        }


        Field[] fields = t.getFields();


        for (int i = 0; i < fields.length; i += 1)

        {

            Field field = fields[i];

            String name = field.getName();

            try
            {

                Object value = json.get(name);

                Type type = field.getType();

                if (isInteger(type))

                {

                    field.set(o, value);

                } else if (isDouble(type))

                {

                    field.set(o, Double.valueOf(value.toString()));

                } else if (isLong(type))

                {

                    field.set(o, Long.valueOf(value.toString()));

                } else if (isBoolean(type) || isString(type))

                {

                    field.set(o, value);

                } else if (isList(type))

                {

                    Type innerType = getGenericType(field);

                    List list = toList((Class) innerType, (JSONArray) value);

                    field.set(o, list);

                } else

                {

                    field.set(o, toObject(field.getType(), (JSONObject) value));

                }

            } catch (JSONException e)
            {

                System.out.println(String.format("JSON 에서 field 이름 %s를 찾을 수 없습니다.", name));

            } catch (IllegalAccessException e)
            {

                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));

            } catch (IllegalArgumentException e)
            {

                System.out.println(String.format("Field 에서 %s의 값을 설정할 수 없습니다.", name));

            }

        }

        return o;

    }


    private static boolean isIgnore(String name)
    {
        return name.equals("serialVersionUID") | name.contains("$");
    }


    /**
     * 오브젝트가 가지는 제네릭의 타입을 가져옵니다
     *
     * @param obj 오브젝트
     * @return 제네릭의 타입
     */

    private static Class getGenericType(Object obj)

    {

        return getGenericType(obj.getClass());

    }


    /**
     * 이 타입이 가지는 제네릭의 타입을 가져옵니다
     *
     * @param tClass 오브젝트
     * @return 제네릭의 타입
     */

    private static Class getGenericType(Class tClass)

    {

        return getGenericType((ParameterizedType) tClass.getGenericSuperclass());

    }


    /**
     * 이 필드가 가지는 제네릭의 타입을 가져옵니다
     *
     * @param field 필드
     * @return 제네릭의 타입
     */

    private static Class getGenericType(Field field)

    {

        return getGenericType((ParameterizedType) field.getGenericType());

    }


    /**
     * 제네릭의 타입을 가져옵니다
     *
     * @param pt
     * @return 제네릭의 타입
     */

    private static Class getGenericType(ParameterizedType pt)

    {

        Type[] t = pt.getActualTypeArguments();

        if (t.length > 0)

        {

            if (t[0] instanceof Class)

            {

                return (Class) t[0];

            }

        }

        return null;

    }


    private static boolean isInteger(Type type)
    {

        return type.equals(Integer.class);

    }


    private static boolean isDouble(Type type)

    {

        return type.equals(Double.class);

    }


    private static boolean isString(Type type)

    {

        return type.equals(String.class);

    }


    private static boolean isNumber(Type type)

    {

        return type.equals(Number.class);

    }


    private static boolean isBoolean(Type type)

    {

        return type.equals(Boolean.class);

    }


    private static boolean isLong(Type type)

    {

        return type.equals(Long.class);

    }


    private static boolean isObject(Type type)

    {

        return type.equals(Object.class);

    }


    /**
     * 필드가 기본 타입인지 검사합니다
     *
     * @param field 필드
     * @return 기본타입일 경우 true
     */

    private static boolean isDefaultType(Field field)

    {

        return isDefaultType(field.getType());

    }


    /**
     * 오브젝트가 기본 타입인지 검사합니다
     *
     * @param obj 오브젝트
     * @return 기본 타입일 경우 true
     */

    private static boolean isDefaultType(Object obj)

    {

        return isDefaultType(obj.getClass());

    }


    /**
     * 클래스가 기본 타입인지 검사합니다
     *
     * @param type 클래스 이름
     * @return 기본 타입일 경우 true
     */

    private static boolean isDefaultType(Type type)

    {

        return isNumber(type) ||

                isInteger(type) ||

                isBoolean(type) ||

                isDouble(type) ||

                isString(type) ||

                isDouble(type) ||

                isLong(type) ||

                isObject(type);

    }


    /**
     * 필드가 리스트인지 검사합니다
     *
     * @param field 필드
     * @return 리스트일 경우 true
     */

    private static boolean isList(Field field)

    {

        return isList(field.getType());

    }


    /**
     * 오브젝트가 리스트인지 검사합니다
     *
     * @param obj 오브젝트
     * @return 리스트일 경우 true
     */

    private static boolean isList(Object obj)

    {

        return isList(obj.getClass());

    }


    /**
     * 클래스가 리스트인지 검사합니다
     *
     * @param type 클래스 이름
     * @return 리스트일 경우 true
     */

    private static boolean isList(Type type)
    {

        return type.equals(List.class) || type.equals(ArrayList.class);

    }


}
