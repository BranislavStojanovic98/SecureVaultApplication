package com.svault.vault.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO request) {
        TeamDTO saved = teamService.createTeam(request);
        teamService.addAdmin(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allteams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok().body(teamService.getAllTeams());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/teamno/{id}")
    public ResponseEntity<TeamDTO> getCertainTeam(@PathVariable Integer id) {
        return ResponseEntity.ok().body(teamService.getCertainTeam(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<TeamDTO> editTeam(@PathVariable Integer id, @RequestBody TeamDTO request) {
        TeamDTO saved = teamService.editTeam(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().body(null);
    }
}
