package com.wikidata.grandchelem.controller;

import com.wikidata.grandchelem.model.Player;
import com.wikidata.grandchelem.model.Tournament;
import com.wikidata.grandchelem.service.GrandChelemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GrandChelemController {

    private static final String LAST_YEAR = "2019";

    @Autowired
    private GrandChelemService grandChelemService;

    @GetMapping("/")
    private String home(Model model) {
        model.addAttribute("tournaments", grandChelemService.getAllTournaments());
        return "home";
    }

    @GetMapping("/tournament")
    private String tournament(@RequestParam String id, @RequestParam(defaultValue = LAST_YEAR) String year, Model model) {
        Tournament tournament = grandChelemService.getTournament(id, year);
        model.addAttribute("tournament", tournament);
        return "tournament";
    }

    @GetMapping("/player")
    private String player(@RequestParam String id, Model model) {
        Player player = grandChelemService.getPlayer(id);
        model.addAttribute("player", player);
        return "player";
    }
}
