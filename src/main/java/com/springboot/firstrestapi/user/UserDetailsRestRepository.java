package com.springboot.firstrestapi.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Long> {
    List<UserDetails> findByRole(String role);
    void save(UserDetails userDetails);
}
