package com.svault.vault.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svault.vault.audit.AuditedAction;
import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.TeamDTO;
import com.svault.vault.models.dto.TeamMemberDTO;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.enums.TeamMemberRole;
import com.svault.vault.models.mappers.TeamMapper;
import com.svault.vault.repositories.TeamRepository;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private final TeamMemberService teamMemberService;
    
    @AuditedAction("CREATE_TEAM")
    public TeamDTO createTeam(TeamDTO request) {

        TeamEntity team = new TeamEntity();
        team.setName(request.getName());
        UserEntity createdBy = userRepository.findById(request.getCreatedById()).orElseThrow(() -> new NotFoundException("User not found"));
        team.setCreatedBy(createdBy);

        teamRepository.save(team);

        return TeamMapper.toDto(team);
    }

    public TeamDTO editTeam(Integer id, TeamDTO request) {

        TeamEntity entity = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team does not exist"));

        if(entity == null) return null;
        if(request.getName().isBlank()) return null;

        entity.setName(request.getName());

        teamRepository.save(entity);
        
        return TeamMapper.toDto(entity);
    }

    public List<TeamDTO> findAllTeamsUserCreated(Integer id) {
        List<TeamEntity> teams = teamRepository.findAllByCreatedById(id);
        List<TeamDTO> teamDTOs = new ArrayList<TeamDTO>();
        teams.forEach(item -> {
            teamDTOs.add(TeamMapper.toDto(item));
        });
        return teamDTOs;
    }

    public List<TeamDTO> getAllTeams() {
        List<TeamEntity> teams =  teamRepository.findAll();
        return teams.stream().map(TeamMapper::toDto).collect(Collectors.toList());
    }

    public TeamDTO getCertainTeam(Integer id) {
        TeamEntity entity = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("No team"));
        return TeamMapper.toDto(entity);
    }

    public void addAdmin(TeamDTO request) {
        TeamMemberDTO admin = new TeamMemberDTO();
        admin.setUserId(request.getCreatedById());
        admin.setTeamId(request.getId());
        admin.setRole(TeamMemberRole.LEAD);

        teamMemberService.addNewMember(admin);
    }

    public void deleteTeam(Integer id) {
        TeamEntity team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException());
        teamRepository.delete(team);
    }
}
