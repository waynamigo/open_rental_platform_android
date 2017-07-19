package com.example.yizu.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/19.
 */
public class Record extends BmobObject {
    private String goodsName;//物品名称
    private String rentedPerson;//租用方
    private String rentingPerson;//出租方
    private String time;//交易时间
    private Double money;//租金
    private Double deposit;//押金
    private Double LossOfExpense;//损耗费
    private String state;//交易状态

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getLossOfExpense() {
        return LossOfExpense;
    }

    public void setLossOfExpense(Double lossOfExpense) {
        LossOfExpense = lossOfExpense;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
