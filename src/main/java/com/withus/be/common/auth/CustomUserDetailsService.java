package com.withus.be.common.auth;

import com.withus.be.domain.Member;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final MemberRepository memberRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return memberRepository.findByEmail(email)
              .map(member -> createUser(email, member))
              .orElseThrow(() -> new UsernameNotFoundException(String.format("'%s' -> can't find it in database.", email)));
   }

   private User createUser(String email, Member member) {
      if (!member.isActivated()) throw new RuntimeException(String.format("'%s' -> not activated.", email));

      GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(member.getAuthority()));
      return new User(member.getEmail(), member.getPassword(), Collections.singleton(grantedAuthority));
   }
}
