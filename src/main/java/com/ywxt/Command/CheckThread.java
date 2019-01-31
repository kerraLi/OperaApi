package com.ywxt.Command;

public class CheckThread implements Runnable {

    private String c;
    private String action;

    public CheckThread(String c, String action) {
        this.c = c;
        this.action = action;
    }

    @Override
    public void run() {
        if (c.equals("ali")) {
            CheckAli.exportCheck(action);
        } else if (c.equals("godaddy")) {
            CheckGodaddy.exportCheck(action);
        } else if (c.equals("aws")) {
            CheckAws.exportCheck(action);
        }
    }
}
