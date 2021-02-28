package com.wikidata.grandchelem.service;

import com.wikidata.grandchelem.model.Player;
import com.wikidata.grandchelem.model.Tournament;

import java.util.List;

public interface GrandChelemService {

    List<Tournament> getAllTournaments();

    Tournament getTournament(String id, String year);

    Player getPlayer(String id);
}
