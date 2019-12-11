package com.saurabh.natveapp2;

public class product {
    private String serial, brand, description;
    private double price;
    private int qty;

    public product(){

    }

    public product(String serial) {
        this.serial = serial ;

    }

    public String getSerial() {
        return serial;
    }


}
