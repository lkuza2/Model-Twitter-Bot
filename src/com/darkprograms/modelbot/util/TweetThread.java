package com.darkprograms.modelbot.util;

import com.darkprograms.modelbot.gui.MainGUI;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 1/18/13
 * Time: 12:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class TweetThread implements Runnable {

    OnTweetListener listener;

    public TweetThread(OnTweetListener listener) {
        this.listener = listener;
    }

    public interface OnTweetListener {
        public void onTweet(String response);
    }

    @Override
    public void run() {
        LanguageDetectUtil.getInstance().connect();
        new java.util.Timer().schedule(new RefreshAccessTokenTask((RefreshAccessTokenTask.OnRefreshListener) listener), (Integer.parseInt(LanguageDetectUtil.getInstance().getExpires()) * 1000));

        while (MainUtil.getInstance().isRunning()) {
            String response = MainUtil.getInstance().retweet();
            if (response != null) {
                listener.onTweet(response);
            } else {
                listener.onTweet("Fail");
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                return;
            }

        }
    }


}
