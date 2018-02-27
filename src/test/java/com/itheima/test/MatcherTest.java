package com.itheima.test;

import com.itheima.ck.config.Config;

public class MatcherTest {

    public static void main(String[] args) {
        String name = "GroupBalanceDetail20180101.xls";
        String match = "GroupBalanceDetail\\w{0,7}\\d{8}.xls";
        System.out.println(name.matches(match));
    }
}
