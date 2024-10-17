package org.example.adventurexpbackend.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HomeRESTController {

    private static final String HOME_ENDPOINT = "";

    @GetMapping(HOME_ENDPOINT)
    public String getHomeEndpoint() {
        return "Home";
    }


}
