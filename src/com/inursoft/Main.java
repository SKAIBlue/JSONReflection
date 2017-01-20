package com.inursoft;

import com.inursoft.jsonreflection.JSON;

public class Main {

    public static void main(String[] args)
    {
        Test test = new Test(1, 1.0,"s", 1L, false, new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        test.list.add(new InnerTest(1, 1.0, "s", 1L, false));
        System.out.println(JSON.toJSONObject(JSON.toObject(Test.class, JSON.toJSONObject(test).toString())).toString());

    }

}
