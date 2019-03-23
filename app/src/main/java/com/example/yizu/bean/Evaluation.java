package com.example.yizu.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by q on 2017/7/19.
 */
public class Evaluation extends BmobObject {
    private Double StarRating;//星级
    private String  TextEvaluation;//文字评价
    private User eval;//租用方评论
    private Record comment;//记录的评价
    private Goods BelongTo;//属于某个物品


    public Double getStarRating() {
        return StarRating;
    }

    public void setStarRating(Double starRating) {
        StarRating = starRating;
    }

    public String getTextEvaluation() {
        return TextEvaluation;
    }

    public void setTextEvaluation(String textEvaluation) {
        TextEvaluation = textEvaluation;
    }

    public User getEval() {
        return eval;
    }

    public void setEval(User eval) {
        this.eval = eval;
    }

    public Record getComment() {
        return comment;
    }

    public void setComment(Record comment) {
        this.comment = comment;
    }

    public Goods getBelongTo() {
        return BelongTo;
    }

    public void setBelongTo(Goods belongTo) {
        BelongTo = belongTo;
    }
}
