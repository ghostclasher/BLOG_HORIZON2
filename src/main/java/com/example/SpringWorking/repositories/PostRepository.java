package com.example.SpringWorking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringWorking.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
    
    
}
