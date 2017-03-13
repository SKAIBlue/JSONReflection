package com.inursoft;

/**
 * Created by Anonymous on 2017. 1. 21..
 */
public class InnerTest {

    public Integer i;

    public Double d;

    public String s;

    public Long l;

    public Boolean b;


    public InnerTest() {
    }

    public InnerTest(Integer i, Double d, String s, Long l, Boolean b) {
        this.i = i;
        this.d = d;
        this.s = s;
        this.l = l;
        this.b = b;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("i: " + i);
        builder.append("d: " + i);
        builder.append("s: " + i);
        builder.append("l: " + i);
        builder.append("b: " + i);
        return builder.toString();
    }
}
