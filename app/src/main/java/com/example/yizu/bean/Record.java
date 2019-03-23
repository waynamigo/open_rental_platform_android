package com.example.yizu.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/19.
 */
public class Record extends BmobObject implements Serializable {
    private Goods make;//物品
    private User rented;//租用方
    private User renting;//出租方
    private Double money;//租金
    private Double deposit;//押金
    private Double LossOfExpense;//损耗费
    private String state;//交易状态
    private Boolean isEval=false;//是否评价

    public Boolean getEval() {
        return isEval;
    }

    public void setEval(Boolean eval) {
        isEval = eval;
    }

    public Goods getMake() {
        return make;
    }

    public void setMake(Goods make) {
        this.make = make;
    }

    public User getRented() {
        return rented;
    }

    public void setRented(User rented) {
        this.rented = rented;
    }

    public User getRenting() {
        return renting;
    }

    public void setRenting(User renting) {
        this.renting = renting;
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
