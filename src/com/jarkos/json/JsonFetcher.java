package com.jarkos.json;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by jkostrzewa on 2017-08-09.
 */
public class JsonFetcher {

    public static final String UTF_8 = "UTF-8";

    public static String fetchJsonData(String url) throws URISyntaxException, IOException{
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParameters = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        HttpConnectionParams.setTcpNoDelay(httpParameters, true);
        HttpGet request = new HttpGet();
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        InputStream ips = response.getEntity().getContent();
        BufferedReader buf = new BufferedReader(new InputStreamReader(ips, UTF_8));
        StringBuilder sb = new StringBuilder();
        String s;
        while (true) {
            s = buf.readLine();
            if (s == null || s.length() == 0) {
                break;
            }
            sb.append(s);
        }
        buf.close();
        ips.close();
        String result = sb.toString();
        return result;
    }
}
