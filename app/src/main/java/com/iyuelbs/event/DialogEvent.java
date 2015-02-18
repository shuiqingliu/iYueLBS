package com.iyuelbs.event;

/**
 * Created by Bob Peng on 2015/2/16.
 */
public class DialogEvent {
    public final String msg;

    /**
     * new default loading dialog, showing message loading.
     */
    public DialogEvent() {
        this.msg = "";
    }

    /**
     * new loading dialog with specified message.
     */
    public DialogEvent(String msg) {
        this.msg = msg;
    }
}