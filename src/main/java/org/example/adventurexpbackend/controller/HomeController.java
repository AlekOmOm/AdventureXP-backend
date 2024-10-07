package org.example.adventurexpbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Void> redirectToStartPage() {
        return ResponseEntity.status(302).header("Location", "/startpage.html").build();
    }
}
