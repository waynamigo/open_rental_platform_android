package com.example.yizu.bean;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by q on 2017/7/19.
 */
public class Goods extends BmobObject implements Serializable {
    private String goodsName;//物品名称
    private String classification;//分类
    private BmobFile pic1;
    private BmobFile pic2;
    private BmobFile pic3;//三张照片
    private String description;//描述
    private String Positioning;//定位，市
    private Double deposit;//押金
    private Double moneyPer;//单位时间的租金
    private String area;//区
    private String state;//状态
    private Double StarRating;//星级
    private User user;//拥有该物品
    private BmobRelation purchase;//用户租物品，多对多
    private String Path[] = new String[3];//路径
    public void setPath(String path,int i){
        if(i>=3)return;
        else Path[i] = path;
    }
    public String getPath(int i){
        if(i>=3)return null;
        else return Path[i];
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobRelation getPurchase() {
        return purchase;
    }

    public void setPurchase(BmobRelation purchase) {
        this.purchase = purchase;
    }

    public Double getStarRating() {
        return StarRating;
    }

    public void setStarRating(Double starRating) {
        StarRating = starRating;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public BmobFile getPic1() {
        return pic1;
    }

    public void setPic1(BmobFile pic1) {
        this.pic1 = pic1;
    }

    public BmobFile getPic2() {
        return pic2;
    }

    public void setPic2(BmobFile pic2) {
        this.pic2 = pic2;
    }

    public BmobFile getPic3() {
        return pic3;
    }

    public void setPic3(BmobFile pic3) {
        this.pic3 = pic3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPositioning() {
        return Positioning;
    }

    public void setPositioning(String positioning) {
        Positioning = positioning;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getMoneyPer() {
        return moneyPer;
    }

    public void setMoneyPer(Double moneyPer) {
        this.moneyPer = moneyPer;
    }
}
