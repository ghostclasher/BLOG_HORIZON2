package com.example.SpringWorking.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 
import com.example.SpringWorking.models.Authority;


@Repository
public interface AuthorityRepository  extends JpaRepository<Authority,Long>{
    
}
