package com.wikidata.grandchelem.model;

import java.util.ArrayList;
import java.util.List;

public class Winner {

    private String categoryLabel;
    private ArrayList<Player> players = new ArrayList<>();

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
