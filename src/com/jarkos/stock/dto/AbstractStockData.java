package com.jarkos.stock.dto;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public abstract class AbstractStockData {

    public abstract float getLastBtcPrice();

    public abstract float getLastLtcPrice();

    public abstract Object getLtcEurStockData();
}
