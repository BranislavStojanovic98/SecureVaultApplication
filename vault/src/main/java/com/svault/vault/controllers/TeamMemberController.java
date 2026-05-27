package com.svault.vault.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svault.vault.models.dto.TeamMemberDTO;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.keys.TeamMemberId;
import com.svault.vault.models.requests.NewRoleRequest;
import com.svault.vault.services.TeamMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teamMember")
@RequiredArgsConstructor
public class TeamMemberController {
    
    private final TeamMemberService teamMemberService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addmember")
    public ResponseEntity<TeamMemberDTO> createTeamMember(@RequestBody TeamMemberDTO request) {
        TeamMemberDTO saved = teamMemberService.addNewMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<String> changeRole(@RequestBody NewRoleRequest request) {
        teamMemberService.changeRole(request);
        return ResponseEntity.ok("Role successfully changed");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeMember(@RequestBody TeamMemberId id) {
        teamMemberService.removeMember(id);
        return ResponseEntity.ok("User successfully removed");
    }

    //Not in use
    @GetMapping("/getAllTeams/{id}")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamsUserIsMember(@PathVariable Integer id) {
        return ResponseEntity.ok().body(teamMemberService.getAllTeamsUserIsIn(id));
    }

    @PreAuthorize("hasAuthority('TEAM_LEAD')")    
    @GetMapping("/my")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamsForUser(Authentication authentication) {
        
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Integer userId = user.getId();
        
        return ResponseEntity.ok().body(teamMemberService.getAllTeamsUserIsIn(userId));
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEAD', 'ADMIN', 'DEVELOPER')")    
    @GetMapping("/getTeammatesFor/{teamId}")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMembers(@PathVariable Integer teamId) {
        return ResponseEntity.ok().body(teamMemberService.getAllTeamMembers(teamId));
    }

}
