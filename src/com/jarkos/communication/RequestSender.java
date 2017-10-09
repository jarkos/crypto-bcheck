package com.jarkos.communication;

import com.jarkos.stock.exception.DataFetchUnavailableException;
import org.apache.http.conn.ConnectTimeoutException;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class RequestSender {

    public static String sendRequest(String url) throws DataFetchUnavailableException {
        String result = null;
        try {
            result = JsonFetcher.fetchJsonData(url);
        } catch (SocketTimeoutException ste) {
            throw new DataFetchUnavailableException("TIMEOUT EXCEPTION!");
        } catch (ConnectTimeoutException | SSLPeerUnverifiedException | UnknownHostException cne) {
            throw new DataFetchUnavailableException("CONNECTION EXCEPTION!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}