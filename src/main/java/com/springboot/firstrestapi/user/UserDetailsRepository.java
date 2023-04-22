package com.springboot.firstrestapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//I don't use this class. I am gonna live it behind for now just in case!
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    List<UserDetails> findByRole(String role);
}
