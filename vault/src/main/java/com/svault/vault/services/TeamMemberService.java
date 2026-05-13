package com.svault.vault.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.TeamMemberDTO;
import com.svault.vault.models.entity.TeamEntity;
import com.svault.vault.models.entity.TeamMemberEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.keys.TeamMemberId;
import com.svault.vault.models.mappers.TeamMemberMapper;
import com.svault.vault.models.requests.NewRoleRequest;
import com.svault.vault.repositories.TeamMemberRepository;
import com.svault.vault.repositories.TeamRepository;
import com.svault.vault.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamMemberService {
    
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamMemberDTO addNewMember(TeamMemberDTO request){
        
        TeamMemberEntity teamMember = new TeamMemberEntity();
        
        TeamMemberId reqId = new TeamMemberId();
        reqId.setTeamId(request.getTeamId());
        reqId.setUserId(request.getUserId());

        teamMember.setId(reqId);

        TeamEntity teamEntity = teamRepository.findById(reqId.getTeamId()).orElseThrow(() -> new NotFoundException("No such team"));
        teamMember.setTeam(teamEntity);

        UserEntity userEntity = userRepository.findById(reqId.getUserId()).orElseThrow(() -> new NotFoundException("No such user"));
        teamMember.setUser(userEntity);

        teamMember.setRole(request.getRole());
        
        teamMember.setJoinedAt(Instant.now());

        teamMemberRepository.save(teamMember);
        return TeamMemberMapper.toDto(teamMember);
    }

    public void removeMember(TeamMemberId requset) {
        TeamMemberEntity entity = teamMemberRepository.findById(requset).orElseThrow(() -> new NotFoundException("No team member"));
        if(entity == null) return;
        
        teamMemberRepository.delete(entity);
    }

    public void changeRole(NewRoleRequest role) {
        TeamMemberId id = new TeamMemberId();
        id.setTeamId(role.getTeamId());
        id.setUserId(role.getUserId());
        TeamMemberEntity entity = teamMemberRepository.findById(id).orElseThrow(() -> new NotFoundException("No team member"));
        if(entity == null) return;

        entity.setRole(role.getRole());
        teamMemberRepository.save(entity);
    }

    public List<TeamMemberDTO> getAllTeamsUserIsIn(Integer id) {
        List<TeamMemberEntity> teams = teamMemberRepository.findByUserId(id);
        List<TeamMemberDTO> dtos = new ArrayList<TeamMemberDTO>();
        for (TeamMemberEntity teamMemberEntity : teams) {
            dtos.add(TeamMemberMapper.toDto(teamMemberEntity));
        }
        return dtos;
    }

    public List<TeamMemberDTO> getAllTeamMembers(Integer id) {
        List<TeamMemberEntity> teamMembers = teamMemberRepository.findAllByTeamId(id);
        List<TeamMemberDTO> teammates = new ArrayList<TeamMemberDTO>();
        for (TeamMemberEntity teamMemberEntity : teamMembers) {
            teammates.add(TeamMemberMapper.toDto(teamMemberEntity));
        }
        return teammates;
    }
}
