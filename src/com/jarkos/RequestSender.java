package com.jarkos;

import com.jarkos.json.JsonFetcher;
import org.apache.http.conn.ConnectTimeoutException;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by jkostrzewa on 2017-09-09.
 */
public class RequestSender {

    public static String sendRequest(String url) {
        String result = null;
        try {
            result = JsonFetcher.fetchJsonData(url);
        } catch (SocketTimeoutException ste) {
            System.err.println("SOCKET TIMEOUT EXCEPTION.");
            return null;
        } catch (ConnectTimeoutException | SSLPeerUnverifiedException | UnknownHostException cne) {
            System.err.println("CONNECTION EXCEPTION.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
