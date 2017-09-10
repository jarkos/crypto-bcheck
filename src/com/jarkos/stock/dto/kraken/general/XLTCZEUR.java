package com.jarkos.stock.dto.kraken.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
@Getter
@Setter
public class XLTCZEUR {

    @SerializedName("a")
    @Expose
    private List<String> a = null;
    @SerializedName("b")
    @Expose
    private List<String> b = null;
    @SerializedName("c")
    @Expose
    private List<String> lastTradePrice = null;
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

}
