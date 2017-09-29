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
    private GeneralKrakenData xXBTZEUR;

    @SerializedName("XLTCZEUR")
    @Expose
    private GeneralKrakenData xLTCZEUR;

    @SerializedName("XETHZEUR")
    @Expose
    private GeneralKrakenData xETHZEUR;

    @SerializedName("BCHEUR")
    @Expose
    private GeneralKrakenData xBCHEUR;

    @SerializedName("DASHEUR")
    @Expose
    private GeneralKrakenData DASHEUR;

}
