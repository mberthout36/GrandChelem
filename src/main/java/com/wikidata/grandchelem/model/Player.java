package com.wikidata.grandchelem.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String id;
    private String name;
    private String sex;
    private String country;
    private String mainHand;
    private ArrayList<String> playStyle = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMainHand() {
        return mainHand;
    }

    public void setMainHand(String mainHand) {
        this.mainHand = mainHand;
    }

    public List<String> getPlayStyle() {
        return playStyle;
    }

    public void addPlayStyle(String playStyle) {
        this.playStyle.add(playStyle);
    }
}
