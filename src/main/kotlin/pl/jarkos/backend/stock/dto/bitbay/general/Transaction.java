package pl.jarkos.backend.stock.dto.bitbay.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
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

    @Override
    public int compareTo(Transaction o) {
        return Math.toIntExact(this.getDate() - o.getDate());
    }
}
