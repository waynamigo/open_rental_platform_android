package com.example.yizu.bean;

import android.media.Image;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/19.
 */
public class Goods extends BmobObject {
    private String goodsName;//物品名称
    private String classification;//分类
    private Image[] pic;//三张照片
    private String description;//描述
    private String Positioning;//定位
    private Double deposit;//押金
    private Double moneyPer;//单位时间的租金

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

    public Image[] getPic() {
        return pic;
    }

    public void setPic(Image[] pic) {
        this.pic = pic;
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
