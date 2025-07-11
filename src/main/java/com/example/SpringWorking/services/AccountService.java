package com.example.SpringWorking.services;

 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SpringWorking.models.Account;
import com.example.SpringWorking.models.Authority;
import com.example.SpringWorking.repositories.AccountRepository;
import com.example.SpringWorking.util.constants.Roles;

 

@Service
public class AccountService implements UserDetailsService{
    @Autowired
    private AccountRepository accrepo;
    @Autowired
    private PasswordEncoder pe;

    // METHOD 1-->SAVE NEW ONJECT
    public Account save(Account acc){
      acc.setPassword(pe.encode(acc.getPassword()));
       if(acc.getRole()==null){
        acc.setRole(Roles.USER.getRole());
       }
       if(acc.getPhoto()==null){
         acc.setPhoto("src\\main\\resources\\static\\images\\ben-o-bro-wpU4veNGnHg-unsplash.jpg");
       }
      return  accrepo.save(acc);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
           Optional<  Account> target=accrepo.findByUsername(username);



     if(!target.isPresent()){
      throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }
    else{
      Account found=target.get();
      List<GrantedAuthority> grantedAuthority=new ArrayList<>();
      grantedAuthority.add(new SimpleGrantedAuthority(found.getRole()));
      for(Authority _auth:found.getAuthorities()){
        grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
      }
      User user=new User(found.getUsername(), found.getPassword(), grantedAuthority);
    return user;
    }
  }

  public Optional<Account> findByUsername(String username){
    return accrepo.findByUsername(username);
  }

    // METHOD 2-->TO DELETE PARTICULAR ACCOUNT
    // public void delete(Account acc){
    //     accrepo.delete(acc);
    // }

    // METHOD 3-->READ SINGLE ACCOUNT FROM ACCCOUNT TABLE
    public Optional< Account> findById(Long acc_id){
        return accrepo.findById( acc_id);
    }


    public Optional< Account> findByEmail(String email){
      return accrepo.findByEmail(email);
  }

  public Optional< Account> findByToken(String token){
    return accrepo.findByToken(token);
}

}
