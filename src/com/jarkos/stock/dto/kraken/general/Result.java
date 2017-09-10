package com.jarkos.stock.dto.kraken.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    @SerializedName("XXBTZEUR")
    @Expose
    private XXBTZEUR xXBTZEUR;

    @SerializedName("XLTCZEUR")
    @Expose
    private XLTCZEUR xLTCZEUR;

}
