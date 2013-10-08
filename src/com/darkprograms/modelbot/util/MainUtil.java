package com.darkprograms.modelbot.util;

import twitter4j.*;
import twitter4j.auth.RequestToken;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 1/17/13
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainUtil {

    private static MainUtil instance;

    public synchronized static MainUtil getInstance() {
        if (instance == null) {
            instance = new MainUtil();
        }
        return instance;
    }

    private MainUtil() {

    }


    private Twitter twitter;

    private String searchString;

    private boolean running = false;

    private long lastTweetID;

    private boolean english = false;
    private boolean retweetQuote = false;
    private Thread tweetThread;

    public Thread getTweetThread() {
        return tweetThread;
    }

    public void setTweetThread(Thread tweetThread) {
        this.tweetThread = tweetThread;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }


    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english = english;
    }

    public long getLastTweetID() {
        return lastTweetID;
    }

    public void setLastTweetID(long lastTweetID) {
        this.lastTweetID = lastTweetID;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRetweetQuote() {
        return retweetQuote;
    }

    public void setRetweetQuote(boolean retweetQuote) {
        this.retweetQuote = retweetQuote;
    }

    public void init() {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET);

        setTwitter(twitter);
    }

    public void connectToTwitter(boolean open) {
        try {
            //System.out.println(twitter.getOAuthRequestToken());
            RequestToken requestToken = twitter.getOAuthRequestToken();

            String url = requestToken.getAuthorizationURL();

            if (open) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                if(!CommandUtil.getInstance().isCommandLineMode()){
                JTextArea textArea = new JTextArea();
                textArea.setText("Go to: " + url);
                textArea.setColumns(70);
                textArea.setOpaque(false);
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                JOptionPane.showMessageDialog(null, textArea, "PIN", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    System.out.println("Go to: " + url);
                    System.out.println();
                }
            }
            String pin;
            if(!CommandUtil.getInstance().isCommandLineMode()){
               pin = JOptionPane.showInputDialog(null, "Input PIN code from URL: ", "PIN", JOptionPane.INFORMATION_MESSAGE);
            }else{
                System.out.println("Input PIN code from URL: ");
                Scanner scanner = new Scanner(System.in);
                pin = scanner.nextLine();
            }

            getTwitter().getOAuthAccessToken(requestToken, pin);

        } catch (TwitterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String retweet() {
        Query query = new Query(getSearchString());
        try {
            QueryResult result = twitter.search(query);
            Status tweet = result.getTweets().get(0);

            if (isEnglish()) {
                if (LanguageDetectUtil.getInstance().isEnglish(tweet.getText())) {
                    String response = tweet(tweet, isRetweetQuote());
                    setLastTweetID(tweet.getId());
                    return response;
                }else{
                    return "Non-English Tweet Ignored: @" + tweet.getUser().getScreenName() + ": " + tweet.getText();
                }
            } else {
                if (tweet.getId() != getLastTweetID()) {
                    String response = tweet(tweet, isRetweetQuote());
                    setLastTweetID(tweet.getId());
                    return response;
                }
            }

        } catch (TwitterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private String tweet(Status status, boolean retweetQuote){
        try{
        if(isRetweetQuote()){
            String statusString = "RT @" + status.getUser().getScreenName() + " " + status.getText();

            if(statusString.length() > 140){
                    statusString = statusString.substring(0, 140);
            }

            getTwitter().updateStatus(statusString);
            return "Quote Retweet: " + statusString;
        }else{
            getTwitter().retweetStatus(status.getId());
            return "Retweet: @" + status.getUser().getScreenName() + ": " + status.getText();
        }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

}
