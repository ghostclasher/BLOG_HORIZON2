package com.example.SpringWorking.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringWorking.models.Authority;
import com.example.SpringWorking.repositories.AuthorityRepository;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository AuthorityRepository;

    public Authority save(Authority authority){
        return AuthorityRepository.save(authority);
    }

    public Optional<Authority> findById(long id){
        return AuthorityRepository.findById(id);
    }
    
}
