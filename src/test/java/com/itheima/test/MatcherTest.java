package com.itheima.test;

import com.itheima.ck.config.Config;

public class MatcherTest {

    public static void main(String[] args) {
        String name = "RetailBalanceDetail20180101";
        String match = Config.targetFileName1;
        System.out.println(name.matches(match));
    }
}
