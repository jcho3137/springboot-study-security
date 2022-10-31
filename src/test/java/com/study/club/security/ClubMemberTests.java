package com.study.club.security;

import com.study.club.entity.ClubMember;
import com.study.club.entity.ClubMemberRole;
import com.study.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user"+i+"@abc.com")
                    .name("이름"+i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1234"))
                    .build();

            clubMember.addMemberRole(ClubMemberRole.USER);

            if( i > 80 ) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }

            if( i > 90 ) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            clubMemberRepository.save(clubMember);
        });
    }

    @Test
    public void testRead() {
        Optional<ClubMember> result = clubMemberRepository.findByEmail("user1@abc.com", false);
        ClubMember clubMember = result.get();
        System.out.println(clubMember);
    }
}
