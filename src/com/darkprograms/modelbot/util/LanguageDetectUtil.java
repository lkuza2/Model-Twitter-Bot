package com.darkprograms.modelbot.util;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 2/7/13
 * Time: 4:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class LanguageDetectUtil {

    private static LanguageDetectUtil instance;

    public static LanguageDetectUtil getInstance() {
        if (instance == null) {
            instance = new LanguageDetectUtil();
        }
        return instance;
    }

    private LanguageDetectUtil() {

    }

    String expires;
    String accessToken;

    public void connect() {
        try {
            URL url = new URL(Constants.TRANSLATE_OAUTH_URL);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();

            String request = "grant_type=client_credentials&client_id=" +
                    URLEncoder.encode(Constants.TRANSLATE_CLIENT_ID, "UTF-8") + "&client_secret="
                    + URLEncoder.encode(Constants.TRANSLATE_CLIENT_SECRET, "UTF-8") + "&scope=http://api.microsofttranslator.com";

            outputStream.write(request.getBytes("UTF-8"));
            outputStream.flush();

            byte response[] = new byte[1024];
            InputStream inputStream = urlConnection.getInputStream();

            int bytesRead = inputStream.read(response);
            inputStream.close();
            outputStream.close();

            byte[] copiedResponse = Arrays.copyOf(response, bytesRead);

            String responseString = new String(copiedResponse);

            setAccessToken(accessTokenParse(responseString));
            setExpires(expiresInParse(responseString));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isEnglish(String tweet){
        try{
        URL url = new URL(Constants.TRANSLATE_DETECT_LANG_URL + "?appId=&text=" + URLEncoder.encode(tweet, "UTF-8"));
        URLConnection urlConnection = url.openConnection();

        urlConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());

        urlConnection.connect();

        byte response[] = new byte[1024];
        InputStream inputStream = urlConnection.getInputStream();

        int bytesRead = inputStream.read(response);
        inputStream.close();

        byte[] copiedResponse = Arrays.copyOf(response, bytesRead);

        String responseString = new String(copiedResponse);

        return responseString.split(">")[1].replace("</string", "").contains("en");

    } catch (Exception ex) {
        ex.printStackTrace();
    }
        return false;
    }

    private String expiresInParse(String input){
        return input.split("expires_in")[1].split("\"")[2];
    }

    private String accessTokenParse(String input){
        return input.split("access_token")[1].split("\"")[2];
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
