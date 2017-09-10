package com.jarkos.stock.dto.bitbay.general;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BitBayStockData implements Serializable {

    @SerializedName("max")
    @Expose
    private Float max;
    @SerializedName("min")
    @Expose
    private Float min;
    @SerializedName("last")
    @Expose
    private Float last;
    @SerializedName("bid")
    @Expose
    private Float bid;
    @SerializedName("ask")
    @Expose
    private Float ask;
    @SerializedName("vwap")
    @Expose
    private Float vwap;
    @SerializedName("average")
    @Expose
    private Float average;
    @SerializedName("volume")
    @Expose
    private Float volume;
    @SerializedName("bids")
    @Expose
    private List<List<Float>> bids = null;
    @SerializedName("asks")
    @Expose
    private List<List<Float>> asks = null;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;
    private final static long serialVersionUID = 4485884411538419890L;

    /**
     * No args constructor for use in serialization
     */
    public BitBayStockData() {
    }

    /**
     * @param max
     * @param min
     * @param last
     * @param bid
     * @param ask
     * @param vwap
     * @param average
     * @param volume
     * @param bids
     * @param asks
     * @param transactions
     */
    public BitBayStockData(Float max, Float min, Float last, Float bid, Float ask, Float vwap, Float average, Float volume, List<List<Float>> bids, List<List<Float>> asks,
                           List<Transaction> transactions) {
        super();
        this.max = max;
        this.min = min;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
        this.vwap = vwap;
        this.average = average;
        this.volume = volume;
        this.bids = bids;
        this.asks = asks;
        this.transactions = transactions;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public BitBayStockData withMax(Float max) {
        this.max = max;
        return this;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public BitBayStockData withMin(Float min) {
        this.min = min;
        return this;
    }

    public Float getLast() {
        return last;
    }

    public void setLast(Float last) {
        this.last = last;
    }

    public BitBayStockData withLast(Float last) {
        this.last = last;
        return this;
    }

    public Float getBid() {
        return bid;
    }

    public void setBid(Float bid) {
        this.bid = bid;
    }

    public BitBayStockData withBid(Float bid) {
        this.bid = bid;
        return this;
    }

    public Float getAsk() {
        return ask;
    }

    public void setAsk(Float ask) {
        this.ask = ask;
    }

    public BitBayStockData withAsk(Float ask) {
        this.ask = ask;
        return this;
    }

    public Float getVwap() {
        return vwap;
    }

    public void setVwap(Float vwap) {
        this.vwap = vwap;
    }

    public BitBayStockData withVwap(Float vwap) {
        this.vwap = vwap;
        return this;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public BitBayStockData withAverage(Float average) {
        this.average = average;
        return this;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public BitBayStockData withVolume(Float volume) {
        this.volume = volume;
        return this;
    }

    public List<List<Float>> getBids() {
        return bids;
    }

    public void setBids(List<List<Float>> bids) {
        this.bids = bids;
    }

    public BitBayStockData withBids(List<List<Float>> bids) {
        this.bids = bids;
        return this;
    }

    public List<List<Float>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<Float>> asks) {
        this.asks = asks;
    }

    public BitBayStockData withAsks(List<List<Float>> asks) {
        this.asks = asks;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BitBayStockData withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public void sort(List<Transaction> transactions){

    }

}
