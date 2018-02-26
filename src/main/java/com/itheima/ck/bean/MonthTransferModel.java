package com.itheima.ck.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class MonthTransferModel implements Serializable {
    private String vendorId;
    private String vendorName;
    private String orderId;
    private String prodName;
    private String cardno;
    private String paymentDate;
    private String tranType;
    private BigDecimal buyamt;
    private BigDecimal hyamt;
    private BigDecimal yqsamt;
    private BigDecimal scorecost;
    private BigDecimal scoremoney;
    private BigDecimal sfamt;
    private String authcode;
    private String singletrans;
    private String transferChannel;
    private String prodType;
    private BigDecimal cardBonusMoney;
    private BigDecimal cardReduceMoney;
    private String caculateDate;
    private String orderType;
    private String transSn;
    private BigDecimal allScorePurchase;
    private BigDecimal allScoreCleanWithInner;
    private BigDecimal allScoreReducePrice;
    private int totalCount;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public BigDecimal getBuyamt() {
        return buyamt;
    }

    public void setBuyamt(BigDecimal buyamt) {
        this.buyamt = buyamt;
    }

    public BigDecimal getHyamt() {
        return hyamt;
    }

    public void setHyamt(BigDecimal hyamt) {
        this.hyamt = hyamt;
    }

    public BigDecimal getYqsamt() {
        return yqsamt;
    }

    public void setYqsamt(BigDecimal yqsamt) {
        this.yqsamt = yqsamt;
    }

    public BigDecimal getScorecost() {
        return scorecost;
    }

    public void setScorecost(BigDecimal scorecost) {
        this.scorecost = scorecost;
    }

    public BigDecimal getScoremoney() {
        return scoremoney;
    }

    public void setScoremoney(BigDecimal scoremoney) {
        this.scoremoney = scoremoney;
    }

    public BigDecimal getSfamt() {
        return sfamt;
    }

    public void setSfamt(BigDecimal sfamt) {
        this.sfamt = sfamt;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getSingletrans() {
        return singletrans;
    }

    public void setSingletrans(String singletrans) {
        this.singletrans = singletrans;
    }

    public String getTransferChannel() {
        return transferChannel;
    }

    public void setTransferChannel(String transferChannel) {
        this.transferChannel = transferChannel;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public BigDecimal getCardBonusMoney() {
        return cardBonusMoney;
    }

    public void setCardBonusMoney(BigDecimal cardBonusMoney) {
        this.cardBonusMoney = cardBonusMoney;
    }

    public BigDecimal getCardReduceMoney() {
        return cardReduceMoney;
    }

    public void setCardReduceMoney(BigDecimal cardReduceMoney) {
        this.cardReduceMoney = cardReduceMoney;
    }

    public String getCaculateDate() {
        return caculateDate;
    }

    public void setCaculateDate(String caculateDate) {
        this.caculateDate = caculateDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTransSn() {
        return transSn;
    }

    public void setTransSn(String transSn) {
        this.transSn = transSn;
    }

    public BigDecimal getAllScorePurchase() {
        return allScorePurchase;
    }

    public void setAllScorePurchase(BigDecimal allScorePurchase) {
        this.allScorePurchase = allScorePurchase;
    }

    public BigDecimal getAllScoreCleanWithInner() {
        return allScoreCleanWithInner;
    }

    public void setAllScoreCleanWithInner(BigDecimal allScoreCleanWithInner) {
        this.allScoreCleanWithInner = allScoreCleanWithInner;
    }

    public BigDecimal getAllScoreReducePrice() {
        return allScoreReducePrice;
    }

    public void setAllScoreReducePrice(BigDecimal allScoreReducePrice) {
        this.allScoreReducePrice = allScoreReducePrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
