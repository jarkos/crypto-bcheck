package com.jarkos.walutomat;

import com.google.gson.Gson;

import static com.jarkos.communication.RequestSender.sendRequest;

public class WalutomatDataService {

    private static String WalutomatPlnEurURL = "https://panel.walutomat.pl/api/v1/best_offers.php?curr1=EUR&curr2=PLN";

    public WalutomatData getWalutomatEurToPlnData() {
        String resWalutomat = null;
        try {
            resWalutomat = sendRequest(WalutomatPlnEurURL);
        } catch (Exception e) {
            System.out.println(e.getMessage().concat(" " + getServiceCodeName()));
        }
        return getWalutomatEurPlnData(resWalutomat);
    }

    private String getServiceCodeName() {
        return "Walutomat";
    }


    private static WalutomatData getWalutomatEurPlnData(String res) {
        Gson gson = new Gson();
        return gson.fromJson(res, WalutomatData.class);
    }

}
