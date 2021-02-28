package com.wikidata.grandchelem.model;

import java.util.ArrayList;
import java.util.List;

public class Edition {

    private String label;
    private String year;
    private ArrayList<Winner> winners = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void addWinner(String category, Player player) {
        for (Winner winner : winners) {
            if (category.equals(winner.getCategoryLabel())) {
                winner.addPlayer(player);
                return;
            }
        }

        Winner winner = new Winner();
        winner.setCategoryLabel(category);
        winner.addPlayer(player);
        winners.add(winner);
    }
}
