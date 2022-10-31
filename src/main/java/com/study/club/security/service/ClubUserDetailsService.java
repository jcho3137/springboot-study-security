package com.study.club.security.service;

import com.study.club.entity.ClubMember;
import com.study.club.repository.ClubMemberRepository;
import com.study.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername" + username);
        log.info("#################################################### 1");

        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
        log.info("#################################################### 2");
        if(result.isPresent()==false) {
            log.info("#################################################### 3");
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();
        log.info("#################################################### 4");
        log.info(clubMember);
        log.info("#################################################### 5");

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );

        clubAuthMemberDTO.setName(clubMember.getName());
        clubAuthMemberDTO.setFromSocial(clubMember.isFromSocial());

        return clubAuthMemberDTO;
    }
}
