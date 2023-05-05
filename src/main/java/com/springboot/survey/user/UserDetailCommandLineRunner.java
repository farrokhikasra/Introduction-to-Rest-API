package com.springboot.survey.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailCommandLineRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDetailsRestRepository repository;

    public UserDetailCommandLineRunner(UserDetailsRestRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(Arrays.toString(args));
        repository.save(new UserDetails("Kasra", "Admin"));
        repository.save(new UserDetails("Pedram", "Admin"));
        repository.save(new UserDetails("Alireza", "Admin"));

        List<UserDetails> users = repository.findByRole("Admin");
        users.forEach(user -> logger.info(user.toString()));
    }
}
