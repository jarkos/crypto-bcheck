package pl.jarkos.backend.walutomat;

import com.google.gson.Gson;

import static pl.jarkos.backend.communication.RequestSender.sendRequest;

public class WalutomatDataService {

    private static String WalutomatPlnEurURL = "https://panel.walutomat.pl/api/v1/best_offers.php?curr1=EUR&curr2=PLN";

    public WalutomatData getWalutomatEurToPlnData() {
//        int[] ints = {12, 43, 56, 2, 4, 7767, 72, 22, 1};
//        for (int i = 0; i < ints.length - 1; i++) {
//            for (int j = 1; j < ints.length - i; j++) {
//                if (ints[j - 1] > ints[j]) {
//                    int temp = ints[j-1];
//                    ints[j-1] = ints[j];
//                    ints[j]=temp;
//                }
//            }
//        }

//        System.out.println(ints.toString());
        int a = 5 / 2;
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
