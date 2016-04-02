package com.demo.zlm.weibosample.api;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by malinkang on 2016/3/31.
 */
public class MytmArray implements X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        // return null;
        return new X509Certificate[] {};
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub
        // System.out.println("cert: " + chain[0].toString() + ", authType: "
        // + authType);
    }
}
