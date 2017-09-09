
package com.jarkos.stock.dto.kraken;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XXBTZEUR {

    @SerializedName("a")
    @Expose
    private List<String> a = null;
    @SerializedName("b")
    @Expose
    private List<String> b = null;
    @SerializedName("c")
    @Expose
    private List<String> lastTrade = null;
    @SerializedName("v")
    @Expose
    private List<String> v = null;
    @SerializedName("p")
    @Expose
    private List<String> p = null;
    @SerializedName("t")
    @Expose
    private List<Integer> t = null;
    @SerializedName("l")
    @Expose
    private List<String> l = null;
    @SerializedName("h")
    @Expose
    private List<String> h = null;
    @SerializedName("o")
    @Expose
    private String open;

    public List<String> getA() {
        return a;
    }

    public void setA(List<String> a) {
        this.a = a;
    }

    public List<String> getB() {
        return b;
    }

    public void setB(List<String> b) {
        this.b = b;
    }

    public List<String> getLastTradePrice() {
        return lastTrade;
    }

    public void setLastTrade(List<String> lastTrade) {
        this.lastTrade = lastTrade;
    }

    public List<String> getV() {
        return v;
    }

    public void setV(List<String> v) {
        this.v = v;
    }

    public List<String> getP() {
        return p;
    }

    public void setP(List<String> p) {
        this.p = p;
    }

    public List<Integer> getT() {
        return t;
    }

    public void setT(List<Integer> t) {
        this.t = t;
    }

    public List<String> getL() {
        return l;
    }

    public void setL(List<String> l) {
        this.l = l;
    }

    public List<String> getH() {
        return h;
    }

    public void setH(List<String> h) {
        this.h = h;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

}
