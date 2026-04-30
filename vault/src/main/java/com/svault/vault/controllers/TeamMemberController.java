package com.svault.vault.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
