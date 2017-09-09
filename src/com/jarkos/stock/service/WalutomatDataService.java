package com.jarkos.stock.service;

import com.google.gson.Gson;
import com.jarkos.walutomat.WalutomatData;

import static com.jarkos.RequestSender.sendRequest;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class WalutomatDataService {

    private static String WalutomatPlnEurURL = "https://panel.walutomat.pl/api/v1/best_offers.php?curr1=EUR&curr2=PLN";

    public static WalutomatData getWalutomatEurToPlnData() {
        String resWalutomat = sendRequest(WalutomatPlnEurURL);
        return getWalutomatEurPlnData(resWalutomat);
    }


    private static WalutomatData getWalutomatEurPlnData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, WalutomatData.class);
    }

}
