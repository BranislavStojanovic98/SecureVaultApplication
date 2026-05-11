package com.svault.vault.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.svault.vault.models.dto.TeamMemberDTO;
import com.svault.vault.models.keys.TeamMemberId;
import com.svault.vault.models.requests.NewRoleRequest;
import com.svault.vault.services.TeamMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teamMember")
@RequiredArgsConstructor
public class TeamMemberController {
    
    private final TeamMemberService teamMemberService;
    private final JwtService jwtService;

    @PostMapping()
    public ResponseEntity<TeamMemberDTO> createTeamMember(@RequestBody TeamMemberDTO requset) {
        TeamMemberDTO saved = teamMemberService.addNewMember(requset);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/edit")
    public ResponseEntity<String> changeRole(@RequestBody NewRoleRequest request) {
        teamMemberService.changeRole(request);
        return ResponseEntity.ok("Role successfully changed");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeMember(@RequestBody TeamMemberId id) {
        teamMemberService.removeMember(id);
        return ResponseEntity.ok("User successfully removed");
    }

    @GetMapping("/getAllTeams/{id}")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamsUserIsMember(@PathVariable Integer id) {
        return ResponseEntity.ok().body(teamMemberService.getAllTeamsUserIsIn(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamsForUser(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);
        
        return ResponseEntity.ok().body(teamMemberService.getAllTeamsUserIsIn(userId));
    }

}
