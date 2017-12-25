package pl.jarkos.stock.dto.huobi.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HuobiStockData {

    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("p_last")
    @Expose
    private Float last;
    @SerializedName("p_low")
    @Expose
    private Float pLow;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("top_buy")
    @Expose
    private List<TopBuy> topBuy = null;
    @SerializedName("sells")
    @Expose
    private List<Sell> sells = null;
    @SerializedName("amp")
    @Expose
    private Integer amp;
    @SerializedName("p_open")
    @Expose
    private Float pOpen;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("total")
    @Expose
    private Float total;
    @SerializedName("buys")
    @Expose
    private List<Buy> buys = null;
    @SerializedName("top_sell")
    @Expose
    private List<TopSell> topSell = null;
    @SerializedName("p_high")
    @Expose
    private Float pHigh;
    @SerializedName("p_new")
    @Expose
    private Float pNew;
    @SerializedName("trades")
    @Expose
    private List<Trade> trades = null;

    public Float getPLast() {
        return last > sells.get(0).getPrice() ? sells.get(0).getPrice() : last;
    }

}
