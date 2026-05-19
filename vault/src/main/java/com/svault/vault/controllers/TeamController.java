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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.config.JwtService;
import com.svault.vault.models.dto.TeamDTO;
import com.svault.vault.services.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO request) {
        TeamDTO saved = teamService.createTeam(request);
        teamService.addAdmin(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/allteams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok().body(teamService.getAllTeams());
    }

    @GetMapping("/teamno/{id}")
    public ResponseEntity<TeamDTO> getCertainTeam(@PathVariable Integer id) {
        return ResponseEntity.ok().body(teamService.getCertainTeam(id));
    }

    @GetMapping("/createdTeam/{id}")
    public ResponseEntity<List<TeamDTO>> getAllTeamsUserBelongsTo(@PathVariable Integer id) {
        List<TeamDTO> allTeams = teamService.findAllTeamsUserCreated(id);
        return ResponseEntity.ok().body(allTeams);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TeamDTO>> getAllTeamsForUser(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);
        
        return ResponseEntity.ok().body(teamService.findAllTeamsUserCreated(userId));
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
