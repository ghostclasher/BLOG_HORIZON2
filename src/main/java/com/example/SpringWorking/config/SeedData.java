package com.example.SpringWorking.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.SpringWorking.models.Account;
import com.example.SpringWorking.models.Authority;
import com.example.SpringWorking.models.Post;
import com.example.SpringWorking.services.AccountService;
import com.example.SpringWorking.services.AuthorityService;
import com.example.SpringWorking.services.PostService;
import com.example.SpringWorking.util.constants.Privillages;
import com.example.SpringWorking.util.constants.Roles;
@Component
public class SeedData implements CommandLineRunner{
    @Autowired
    private PostService postobj;

     @Autowired 
     private AccountService accserv;
     @Autowired
     private AuthorityService Authserv;

    @Override
    public void run(String... args) throws Exception {
        // inserting default privillages into authory table
        for(Privillages pr:Privillages.values()){
            Authority auth=new Authority();
            auth.setId(pr.getAuthorityId());
            auth.setName(pr.getprivillageName());
            Authserv.save(auth);

        }
        Account acc1=new Account();
        Account acc2 = new Account();
        Account acc3 = new Account();
        Account acc4 = new Account();
        acc1.setEmail("person1@gmail.com");
        acc1.setPassword("sher");
        acc1.setUsername("cheetah1");
        acc1.setAge(22);
        acc1.setDate_of_birth(LocalDate.parse("2000-10-11"));
        acc1.setGender("Male");
 
        acc2.setEmail("editor@gmail.com");
        acc2.setPassword("bhhaul");
        acc2.setUsername("cheetah2");
        acc2.setRole(Roles.EDITOR.getRole());
        acc2.setAge(22);
        acc2.setDate_of_birth(LocalDate.parse("2000-10-11"));
        acc2.setGender("Male");

        acc3.setEmail("super_editor@gmail.com");
        acc3.setPassword("supereditor");
        acc3.setUsername("supereditor");
        acc3.setRole(Roles.EDITOR.getRole());
        Set<Authority> authorities=new HashSet<>();
        Authserv.findById(Privillages.ACCESS_ADMIN_PANEL.getAuthorityId()).ifPresent(authorities::add);
        Authserv.findById(Privillages.RESET_ANY_USER_PASSWORD.getAuthorityId()).ifPresent(authorities::add);
        acc3.setAuthorities(authorities);
        acc3.setAge(22);
        acc3.setDate_of_birth(LocalDate.parse("2000-10-11"));
        acc3.setGender("Male");


        acc4.setEmail("admin@gmail.com");
        acc4.setPassword("admin");
        acc4.setUsername("admin");
        acc4.setRole(Roles.ADMIN.getRole());
        acc4.setAge(22);
        acc4.setDate_of_birth(LocalDate.parse("2000-10-11"));
        acc4.setGender("Male");


        accserv.save( acc1);
          accserv.save(acc2);
          accserv.save( acc3);
          accserv.save( acc4);
         
       //get all existing records in post table
       List<Post> posts=postobj.findAll();
    //    check if there exist some records in posts.
    if(posts.size()==0){
       
        // add new post objects 
        Post post1=new Post();
        post1.setTitle("POST 1");
        post1.setBody("It is seeding post1");
         post1.setAccount(acc1);
        postobj.save( post1);
        // acc1.setPosts(post1);
         



        Post post2=new Post();
        post2.setTitle("POST 2");
        post2.setBody("It is seeding post2");
     post2.setAccount(acc2);
        postobj.save( post2);  


        Post post3=new Post();
        post3.setTitle("POST 3");
        post3.setBody("It is seeding post3");
        post3.setAccount(acc1);
        postobj.save( post3);
        // acc1.setPosts(post3);



        Post post4=new Post();
        post4.setTitle("POST 4");
        post4.setBody("It is seeding post4");
        post4.setAccount(acc1);
        postobj.save( post4);
        // acc1.setPosts(post4);



        Post post5=new Post();
        post5.setTitle("POST 5");
        post5.setBody("It is seeding post5");
        post5.setAccount(acc1);
        postobj.save( post5);
        // acc1.setPosts(post5);



        Post post6=new Post();
        post6.setTitle("POST 6");
        post6.setBody("It is seeding post6");
        post6.setAccount(acc1);
        postobj.save( post6);
        // acc1.setPosts(post6);



        
    }

    }
    
    
}
