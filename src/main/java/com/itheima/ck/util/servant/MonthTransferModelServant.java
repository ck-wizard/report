package com.itheima.ck.util.servant;

import com.itheima.ck.bean.MonthTransferModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MonthTransferModelServant implements BeanServant<MonthTransferModel> {
    //public static final String newFlag = "清算日期";
    /**
     * 回佣
     */
    private static final String REBATE_DETAIL_BODY = "%-23s%-16s%-14s%-15s%-10s%-10s%-13s%-13s%-13s%-19s%-19s%-17s%-15s%-14s%-14s%-12s%-22s%-35s%-16s%-15s%-17s\r\n";

    public static MonthTransferModelServant servant = new MonthTransferModelServant();
    private static Logger logger = LoggerFactory.getLogger(MonthTransferModelServant.class);
    @Override
    public MonthTransferModel transferXls(MonthTransferModel data, Map<Integer, String> datas) {
        logger.info(datas.values().toString());
        data.setCardno(datas.get(1).trim());
        data.setPaymentDate(datas.get(2) +" " + datas.get(3));
        data.setCaculateDate("?");
        data.setProdType("-");
        data.setTranType(datas.get(4).trim());
        data.setTransferChannel("-");
        data.setBuyamt((datas.get(5).trim()));
        data.setHyamt((datas.get(6).trim()));
        data.setYqsamt((datas.get(7).trim()));
        data.setScorecost((datas.get(9).trim()));
        data.setScoremoney((datas.get(10).trim()));
        data.setSfamt((datas.get(11).trim()));
        data.setAuthcode(datas.get(12).trim());
        data.setSingletrans(datas.get(13).trim());
        data.setTransSn("-");
        data.setOrderId(datas.get(17).trim());
        data.setCardBonusMoney((datas.get(20).trim()));
        data.setCardReduceMoney((datas.get(22).trim()));
        data.setAllScorePurchase((datas.get(24).trim()));
        data.setAllScoreReducePrice((datas.get(25).trim()));
        data.setAllScoreCleanWithInner((datas.get(26).trim()));
        return data;
    }



    @Override
    public  MonthTransferModel transferTxt(MonthTransferModel data, String[] datas) {
        data.setCardno(datas[0].trim());
        data.setPaymentDate(datas[1] +" " + datas[2]);
        data.setCaculateDate(datas[3]);
        data.setProdType("");
        data.setTranType("");
        data.setTransferChannel("");
        data.setBuyamt((datas[3].trim()));
        data.setHyamt((datas[4].trim()));
        data.setYqsamt((datas[9].trim()));
        data.setCardBonusMoney((datas[5].trim()));
        data.setCardReduceMoney((datas[7].trim()));
        data.setScorecost((datas[11].trim()));
        data.setScoremoney((datas[12].trim()));
        data.setSfamt((datas[16].trim()));
        data.setSingletrans(datas[18].trim());
        data.setOrderId(null);
        data.setTransSn(datas[17].trim());
        data.setAllScorePurchase("-");
        data.setAllScoreReducePrice("-");
        data.setAllScoreCleanWithInner("-");
        return data;
    }

    @Override
    public void formatPrint(PrintWriter pw, MonthTransferModel data) {
        pw.printf(REBATE_DETAIL_BODY,
                data.getCardno(),
                data.getPaymentDate().substring(0, 10),
                data.getPaymentDate().substring(11).trim(),
                data.getCaculateDate(),
                data.getProdType(),
                data.getTranType(),
                data.getTransferChannel(),
                data.getBuyamt(),
                data.getHyamt(),
                data.getYqsamt(),
                data.getCardBonusMoney(),
                data.getCardReduceMoney(),
                data.getScorecost(),
                data.getScoremoney(),
                data.getSfamt(),
                //data.getAuthcode(),
                data.getSingletrans(),
                data.getOrderId(),
                data.getTransSn(),
                data.getAllScorePurchase(),
                data.getAllScoreReducePrice(),
                data.getAllScoreCleanWithInner());
    }

}
