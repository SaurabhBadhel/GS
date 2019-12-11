package com.pureintensions.epc.data.model;

public class Dpr {

    String Name,Link;

    public Dpr(String name, String link) {
        Name = name;
        Link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
