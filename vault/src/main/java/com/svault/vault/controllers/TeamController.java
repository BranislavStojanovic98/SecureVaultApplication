package com.svault.vault.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.TeamDTO;
import com.svault.vault.services.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO request) {
        TeamDTO saved = teamService.createTeam(request);
        teamService.addAdmin(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TeamDTO> editTeam(@PathVariable Integer id, @RequestBody TeamDTO request) {
        TeamDTO saved = teamService.editTeam(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
