package com.darkprograms.modelbot.util;

import java.awt.*;
import java.lang.String;
import java.lang.System;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 2/14/13
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommandUtil implements TweetThread.OnTweetListener, RefreshAccessTokenTask.OnRefreshListener{

    private static CommandUtil instance;

    public static CommandUtil getInstance(){
        if(instance == null){
            instance = new CommandUtil();
        }
        return instance;
    }

    private CommandUtil(){

    }

    private boolean commandLineMode = false;

    public void initFromArgs(String args[]){
        if(args.length == 0){
           printHelp();
        }

        if(containsOption("-h", args)){
            printHelp();
            return;
        }

        if(containsOption("-nogui", args)){
           runCommandLineMode(args);
        }
    }

    private void runCommandLineMode(String args[]){
        println("Model Bot v1.50");
        println("By Luke Kuza");
        println("Starting up...");
        setCommandLineMode(true);
        if (Desktop.isDesktopSupported()) {
            MainUtil.getInstance().connectToTwitter(true);
        } else {
            MainUtil.getInstance().connectToTwitter(false);
        }
        MainUtil.getInstance().setEnglish(containsOption("-e", args));
        MainUtil.getInstance().setRetweetQuote(containsOption("-rq", args));
        MainUtil.getInstance().setSearchString(dataForOption("-t", args));
        MainUtil.getInstance().setRunning(true);
        MainUtil.getInstance().setTweetThread(new Thread(new TweetThread(this)));
        MainUtil.getInstance().getTweetThread().start();
    }

    private String dataForOption(String option, String[] args){
        for(int i =0; i < args.length; i++){
            if(args[i].startsWith(option)){
                return args[i+1];
            }
        }
        return "";
    }

    private boolean containsOption(String option, String[] args){
        for(String arg : args){
            //System.out.println(arg);
            if(arg.startsWith(option) || arg.contains(option))
                return true;
        }
        return false;
    }

    private void printHelp(){
        println("Model Bot v1.50");
        println("By Luke Kuza");
        println("Usage");
        println("ModelBot [options]");
        println("No Options defaults to all of the following being set to 'no', or 'false'");
        println("-nogui Command Line");
        println("-t Tweets to search for. Example. #test");
        println("-rq Retweet Quote-Like: Example: \"RT @example tweet\"");
        println("-e Retweet English only tweets");
        println("-h Prints this text");
    }

    private void println(String text){
        System.out.println(text);
    }

    public boolean isCommandLineMode() {
        return commandLineMode;
    }

    public void setCommandLineMode(boolean commandLineMode) {
        this.commandLineMode = commandLineMode;
    }

    @Override
    public void onRefresh(String response) {
        println(response);
    }

    @Override
    public void onTweet(String response) {
        println(response);
    }
}
