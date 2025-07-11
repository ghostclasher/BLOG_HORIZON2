package com.example.SpringWorking.repositories;

 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SpringWorking.models.Account;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
     @Query("SELECT u FROM Account u WHERE u.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Account u WHERE u.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

     
    @Query("SELECT u FROM Account u WHERE u.password_reset_token = :password_reset_token")
    Optional<Account> findByToken(@Param("password_reset_token") String password_reset_token);
    
}
