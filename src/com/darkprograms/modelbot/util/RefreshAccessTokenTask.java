package com.darkprograms.modelbot.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 2/13/13
 * Time: 8:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class RefreshAccessTokenTask extends TimerTask{

    private OnRefreshListener listener;

    private RefreshAccessTokenTask(){

    }

    public RefreshAccessTokenTask(OnRefreshListener listener){
        this.listener = listener;
    }

    public interface OnRefreshListener {
        public void onRefresh(String response);
    }


    @Override
    public void run() {
         LanguageDetectUtil.getInstance().connect();
         listener.onRefresh("Access token refreshed!");
         new java.util.Timer().schedule(new RefreshAccessTokenTask(listener), (Integer.parseInt(LanguageDetectUtil.getInstance().getExpires()) * 1000));
    }

}
