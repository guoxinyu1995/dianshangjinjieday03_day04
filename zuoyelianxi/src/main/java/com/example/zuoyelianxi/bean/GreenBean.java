package com.example.zuoyelianxi.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GreenBean {
    @Id(autoincrement = false)
    long pid;
    private double bargainPrice;
    private String createtime;
    private String detailUrl;
    private String images;
    private int itemtype;
    private double price;
    private int pscid;
    private int salenum;
    private int sellerid;
    private String subhead;
    private String title;
    @Generated(hash = 292065205)
    public GreenBean(long pid, double bargainPrice, String createtime,
            String detailUrl, String images, int itemtype, double price, int pscid,
            int salenum, int sellerid, String subhead, String title) {
        this.pid = pid;
        this.bargainPrice = bargainPrice;
        this.createtime = createtime;
        this.detailUrl = detailUrl;
        this.images = images;
        this.itemtype = itemtype;
        this.price = price;
        this.pscid = pscid;
        this.salenum = salenum;
        this.sellerid = sellerid;
        this.subhead = subhead;
        this.title = title;
    }
    @Generated(hash = 1002137420)
    public GreenBean() {
    }
    public long getPid() {
        return this.pid;
    }
    public void setPid(long pid) {
        this.pid = pid;
    }
    public double getBargainPrice() {
        return this.bargainPrice;
    }
    public void setBargainPrice(double bargainPrice) {
        this.bargainPrice = bargainPrice;
    }
    public String getCreatetime() {
        return this.createtime;
    }
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
    public String getDetailUrl() {
        return this.detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public int getItemtype() {
        return this.itemtype;
    }
    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getPscid() {
        return this.pscid;
    }
    public void setPscid(int pscid) {
        this.pscid = pscid;
    }
    public int getSalenum() {
        return this.salenum;
    }
    public void setSalenum(int salenum) {
        this.salenum = salenum;
    }
    public int getSellerid() {
        return this.sellerid;
    }
    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }
    public String getSubhead() {
        return this.subhead;
    }
    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
  
}
