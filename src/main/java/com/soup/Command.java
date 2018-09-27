package com.soup;

import java.util.ArrayList;

public class Command {
    private String cmd;
    private ArrayList<String> subs = new ArrayList<String>();

    public Command(String command) {

        if(command.contains(";")) {
            cmd = command.substring(0, command.indexOf(';'));
            if (command.contains("|")) {
                for (String sub : command.substring(command.indexOf(";")).split("|")) {
                    subs.add(sub);
                }
            } else {
                if (!command.substring(command.indexOf(";")).isEmpty()) {
                    subs.add(command.substring(command.indexOf(";") + 1));
                }
            }
        } else {
            cmd = command;
        }

    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public ArrayList<String> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<String> subs) {
        this.subs = subs;
    }

    public void addSub(String sub) {
        subs.add("sub");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(cmd + ";");

        for (String sub: subs) {
            builder.append(sub);
        }

        return builder.toString();
    }
}
