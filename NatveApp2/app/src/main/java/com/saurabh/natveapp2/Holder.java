package com.saurabh.natveapp2;

import java.util.HashMap;
import java.util.Map;


public class Holder {



    public  static    String location;
    public  static   String current_process;
    public  static    String editText_enter_serial_number;
    public static     String status;
    public  static   String uid;
    public  static   String  depends;
    public  static   String process_name;
    Map data= new HashMap(  );

    public Holder(String location, String current_process, String editText_enter_serial_number, String status, String uid, String depends, String process_name) {
        this.location = location;
        this.current_process = current_process;
        this.editText_enter_serial_number = editText_enter_serial_number;
        this.status = status;
        this.uid = uid;
        this.depends = depends;
        this.process_name = process_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrent_process() {
        return current_process;
    }

    public void setCurrent_process(String current_process) {
        this.current_process = current_process;
    }

    public String getEditText_enter_serial_number() {
        return editText_enter_serial_number;
    }

    public void setEditText_enter_serial_number(String editText_enter_serial_number) {
        this.editText_enter_serial_number = editText_enter_serial_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }
}
