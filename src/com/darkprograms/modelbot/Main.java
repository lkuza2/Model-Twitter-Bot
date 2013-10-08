package com.darkprograms.modelbot;

import com.darkprograms.modelbot.gui.MainGUI;
import com.darkprograms.modelbot.util.CommandUtil;
import com.darkprograms.modelbot.util.LanguageDetectUtil;
import com.darkprograms.modelbot.util.MainUtil;
import com.darkprograms.modelbot.util.RefreshAccessTokenTask;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: theshadow
 * Date: 1/17/13
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            //cant do anything anyways
        }
        MainUtil.getInstance().init();
        CommandUtil.getInstance().initFromArgs(args);
        if(!CommandUtil.getInstance().isCommandLineMode()){
        MainGUI gui = new MainGUI();
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        }
    }

}
