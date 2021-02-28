package com.wikidata.grandchelem.service;

import com.wikidata.grandchelem.model.Edition;
import com.wikidata.grandchelem.model.Player;
import com.wikidata.grandchelem.model.Tournament;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrandChelemServiceImpl implements GrandChelemService {

    private static final String SPARQL_ENDPOINT = "https://query.wikidata.org/sparql";

    private static final String QUERY_PREFIXES = "PREFIX schema: <http://schema.org/>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX hist: <http://wikiba.se/history/ontology#>\n" +
            "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
            "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
            "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
            "PREFIX dct: <http://purl.org/dc/terms/>\n" +
            "PREFIX bd: <http://www.bigdata.com/rdf#>";

    @Override
    public List<Tournament> getAllTournaments() {
        String sparql = QUERY_PREFIXES +
                "SELECT ?tournament ?tournamentLabel ?id\n" +
                "WHERE {\n" +
                "  ?tournament wdt:P361 wd:Q102113.\n" +
                "  ?tournament wdt:P214 ?id.\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}";
        ResultSet rs = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, QueryFactory.create(sparql)).execSelect();

        ArrayList<Tournament> tournaments = new ArrayList<>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Tournament tournament = new Tournament();
            tournament.setId(qs.get("?id").toString());
            tournament.setName(formatLabel(qs.get("?tournamentLabel").toString()));
            tournaments.add(tournament);
        }

        return tournaments;
    }

    @Override
    public Tournament getTournament(String id, String year) {
        String tournamentInfoSparql = QUERY_PREFIXES +
                "SELECT ?tournament ?tournamentLabel ?countryLabel ?surfaceLabel\n" +
                "WHERE {\n" +
                "  ?tournament wdt:P361 wd:Q102113.\n" +
                "  ?tournament wdt:P214 '" + id + "'.\n" +
                "  ?tournament wdt:P17 ?country.\n" +
                "  ?tournament wdt:P765 ?surface.\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}";
        ResultSet tournamentResult = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, QueryFactory.create(tournamentInfoSparql)).execSelect();

        Tournament tournament = new Tournament();
        tournament.setId(id);
        if (tournamentResult.hasNext()) {
            QuerySolution qs = tournamentResult.next();
            tournament.setName(formatLabel(qs.get("?tournamentLabel").toString()));
            tournament.setCountry(formatLabel(qs.get("?countryLabel").toString()));
            tournament.setSurface(formatLabel(qs.get("?surfaceLabel").toString()));
        }

        String editionListSparql = QUERY_PREFIXES +
                "SELECT ?edition ?editionLabel ?classLabel ?winnerLabel ?winnerId ?date\n" +
                "WHERE {\n" +
                "  ?edition wdt:P31/wdt:P214 '" + id + "'.\n" +
                "  ?edition wdt:P585 ?date.\n" +
                "  FILTER (YEAR(?date) = "+ year + ").\n" +
                "  ?edition wdt:P527 ?category.\n" +
                "  ?category wdt:P2094 ?class.\n" +
                "  FILTER (?class = wd:Q16893072 || ?class = wd:Q16893403\n" +
                "          || ?class = wd:Q17299700 || ?class = wd:Q17299348).\n" +
                "  ?category wdt:P1346 ?winner.\n" +
                "  ?winner wdt:P214 ?winnerId.\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}";
        ResultSet editionResult = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, QueryFactory.create(editionListSparql)).execSelect();

        Edition edition = new Edition();
        boolean firstIteration = true;
        while (editionResult.hasNext()) {
            QuerySolution qs = editionResult.next();
            if (firstIteration) {
                edition.setLabel(formatLabel(qs.get("?editionLabel").toString()));
                edition.setYear(getYear(qs.get("?date").toString()));
            }

            Player player = new Player();
            player.setId(qs.get("?winnerId").toString());
            player.setName(formatLabel(qs.get("?winnerLabel").toString()));
            edition.addWinner(formatLabel(qs.get("?classLabel").toString()), player);

            firstIteration = false;
        }

        tournament.setEdition(edition);

        return tournament;
    }

    @Override
    public Player getPlayer(String id) {
        String sparql = QUERY_PREFIXES +
                "SELECT ?player ?playerLabel ?sexLabel ?countryLabel ?mainHandLabel ?playStyleLabel\n" +
                "WHERE {\n" +
                "  ?player wdt:P31 wd:Q5.\n" +
                "  ?player wdt:P106 wd:Q10833314.\n" +
                "  ?player wdt:P214 '" + id + "'.\n" +
                "  ?player wdt:P21 ?sex.\n" +
                "  ?player wdt:P1532 ?country.\n" +
                "  ?player wdt:P552 ?mainHand.\n" +
                "  ?player wdt:P741 ?playStyle.\n" +
                "  ?playStyle wdt:P31 ?playStyleType.\n" +
                "  FILTER (?playStyleType = wd:Q1333285 || ?playStyleType = wd:Q1364175).\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}";
        ResultSet rs = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, QueryFactory.create(sparql)).execSelect();

        Player player = new Player();
        boolean firstIteration = true;
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            if (firstIteration) {
                player.setName(formatLabel(qs.get("?playerLabel").toString()));
                player.setSex(formatLabel(qs.get("?sexLabel").toString()));
                player.setCountry(formatLabel(qs.get("?countryLabel").toString()));
                player.setMainHand(formatLabel(qs.get("?mainHandLabel").toString()));
            }

            player.addPlayStyle(formatLabel(qs.get("?playStyleLabel").toString()));

            firstIteration = false;
        }

        return player;
    }

    private String formatLabel(String label) {
        return label.substring(0, label.indexOf('@'));
    }

    private String getYear(String date) {
        return date.substring(0, date.indexOf('-'));
    }
}
