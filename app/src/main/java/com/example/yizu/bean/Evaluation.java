package com.example.yizu.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/19.
 */
public class Evaluation extends BmobObject {
    private Integer StarRating;//星级
    private String  TextEvaluation;//文字评价
    private String rentedPerson;//租用方
    private String rentingPerson;//出租方
    private String goodsName;//物品名称

    public Integer getStarRating() {
        return StarRating;
    }

    public void setStarRating(Integer starRating) {
        StarRating = starRating;
    }

    public String getTextEvaluation() {
        return TextEvaluation;
    }

    public void setTextEvaluation(String textEvaluation) {
        TextEvaluation = textEvaluation;
    }

    public String getRentedPerson() {
        return rentedPerson;
    }

    public void setRentedPerson(String rentedPerson) {
        this.rentedPerson = rentedPerson;
    }

    public String getRentingPerson() {
        return rentingPerson;
    }

    public void setRentingPerson(String rentingPerson) {
        this.rentingPerson = rentingPerson;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
