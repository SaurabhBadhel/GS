package com.example.patient_module.Module;

public class List {
   private String name;
    private String dob;

    public List(String name, String dob) {
        this.name = name;
        this.dob = dob;
    }

    public  List(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
