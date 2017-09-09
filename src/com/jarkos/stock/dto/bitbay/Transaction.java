package com.jarkos.stock.dto.bitbay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable, Comparable<Transaction> {

    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("tid")
    @Expose
    private String tid;
    private final static long serialVersionUID = 6002063926875800234L;

    /**
     * No args constructor for use in serialization
     */
    public Transaction() {
    }

    /**
     * @param amount
     * @param price
     * @param tid
     * @param type
     * @param date
     */
    public Transaction(Long date, Float price, String type, Float amount, String tid) {
        super();
        this.date = date;
        this.price = price;
        this.type = type;
        this.amount = amount;
        this.tid = tid;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Transaction withDate(Long date) {
        this.date = date;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Transaction withPrice(Float price) {
        this.price = price;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Transaction withType(String type) {
        this.type = type;
        return this;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Transaction withAmount(Float amount) {
        this.amount = amount;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Transaction withTid(String tid) {
        this.tid = tid;
        return this;
    }

    @Override
    public int compareTo(Transaction o) {
        return Math.toIntExact(this.getDate() - o.getDate());
    }
}
