package com.itheima.ck.bean;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

// 存储商户中间信息
public class VendorSubInfoModel {
    private String clearDateLine;
    private String claarVendorLine;
    private String clearVendorNameLine;
    // 这个只使用文本的...
    private String vendorInfoHead;
    // 这个也只使用文本的..
    private String totalInfoFront;
    private String otherLine;
    // 是否是txt
    boolean txt = false;

    public boolean isTxt() {
        return txt;
    }

    public void setTxt(boolean txt) {
        this.txt = txt;
    }

    private List<MonthTransferModel> vendorData = new ArrayList<>();

    public String getClearDateLine() {
        return clearDateLine;
    }

    public void setClearDateLine(String clearDateLine) {
        this.clearDateLine = clearDateLine;
    }

    public String getClaarVendorLine() {
        return claarVendorLine;
    }

    public void setClaarVendorLine(String claarVendorLine) {
        this.claarVendorLine = claarVendorLine;
    }

    public String getClearVendorNameLine() {
        return clearVendorNameLine;
    }

    public void setClearVendorNameLine(String clearVendorNameLine) {
        this.clearVendorNameLine = clearVendorNameLine;
    }

    public String getVendorInfoHead() {
        return vendorInfoHead;
    }

    public void setVendorInfoHead(String vendorInfoHead) {
        this.vendorInfoHead = vendorInfoHead;
    }

    public String getOtherLine() {
        return otherLine;
    }

    public void setOtherLine(String otherLine) {
        this.otherLine = otherLine;
    }

    public List<MonthTransferModel> getVendorData() {
        return vendorData;
    }

    public void add(MonthTransferModel vendorInfo) {
        this.vendorData.add(vendorInfo);
    }
}
