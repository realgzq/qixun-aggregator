package com.qixun.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil2 {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil2.class);


    public static ResponseEntity getResponse(String requrl,
                                             String paramStr,
                                             Map<String, String> headMap,
                                             int connectiontimeout,
                                             int readtimeout,
                                             String charset,
                                             boolean isMethodPost,
                                             boolean isHttps
                                             ) {
        if (connectiontimeout == 0) {
            connectiontimeout = 5000;
        }

        if (readtimeout == 0) {
            readtimeout = 5000;
        }

        log.info("请求地址为：" + requrl + ",请求参数为：" + paramStr);

        if (isHttps) {
            try {
                trustAllHttpsCertificates();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        OutputStreamWriter out = null;
        BufferedReader in = null;
        HttpURLConnection connect = null;
        String result = "";
        ResponseEntity responseEntity = new ResponseEntity();
        String line = null;
        try {
            URL url = new URL(requrl);
            if (isHttps) {
                connect = (HttpsURLConnection) url.openConnection();
            } else {
                connect = (HttpURLConnection) url.openConnection();
            }
            connect.setConnectTimeout(connectiontimeout);
            connect.setReadTimeout(readtimeout);
            connect.setDoInput(true);
            connect.setRequestMethod(isMethodPost ? "POST" : "GET");

            if (headMap != null && headMap.size() > 0) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    connect.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (paramStr != null && !"".equals(paramStr)) {
                connect.setDoOutput(true);
                out = new OutputStreamWriter(connect.getOutputStream(), charset);
                out.write(paramStr);
                out.flush();
            }
            responseEntity.setResponseCode(connect.getResponseCode());

            in = new BufferedReader(new InputStreamReader(connect
                    .getInputStream(), charset));
            StringBuilder content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            result = content.toString();
            log.info("请求地址为：" + requrl + ",响应信息为：" + result);
        } catch (Exception e) {
            log.info("访问服务器异常，原因：" + e.getMessage());
            try {
                in = new BufferedReader(new InputStreamReader(connect
                        .getErrorStream(), charset));
                StringBuilder content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                result = content.toString();
            } catch (Exception e1) {
                log.info("读取服务器异常信息异常，原因：" + e1.getMessage());
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (connect != null) {
                    connect.disconnect();
                }
            } catch (Exception e) {
                log.info("关闭资源异常，原因：" + e.getMessage());
            }
        }
        responseEntity.setResponseStr(result);
        return responseEntity;
    }




    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    public static class ResponseEntity {
        private int responseCode;
        private String responseStr;
        public int getResponseCode() {
            return responseCode;
        }
        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }
        public String getResponseStr() {
            return responseStr;
        }
        public void setResponseStr(String responseStr) {
            this.responseStr = responseStr;
        }
    }

}
