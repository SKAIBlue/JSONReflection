package com.inursoft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 2017. 1. 21..
 */
public class Test {

    public Integer i;

    public Double d;

    public String s;

    public Long l;

    public Boolean b;

    public List<InnerTest> list;

    public InnerTest inner;

    public Test() {
    }

    public Test(Integer i, Double d, String s, Long l, Boolean b, InnerTest inner) {
        this.i = i;
        this.d = d;
        this.s = s;
        this.l = l;
        this.b = b;
        this.list = new ArrayList<>();
        this.inner = inner;
    }
}
