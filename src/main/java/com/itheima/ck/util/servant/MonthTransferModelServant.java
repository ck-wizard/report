package com.itheima.ck.util.servant;

import com.itheima.ck.bean.MonthTransferModel;

import java.util.Map;

public class MonthTransferModelServant implements BeanServant<MonthTransferModel> {
    @Override
    public MonthTransferModel transferOther(MonthTransferModel bean, Map<Integer, String> map) {
        for(Map.Entry<Integer, String> entry : map.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

        }
        return null;
    }

    @Override
    public MonthTransferModel transferSelf(MonthTransferModel bean, String[] lists) {

        return null;
    }
}
