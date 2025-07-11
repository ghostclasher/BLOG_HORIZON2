package com.example.SpringWorking.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SpringWorking.models.Account;
import com.example.SpringWorking.models.Post;
 
import com.example.SpringWorking.services.AccountService;
import com.example.SpringWorking.services.PostService;

import jakarta.validation.Valid;
 

 

 


 

 
 
@Controller
   
public class PostController {
    @Autowired
private PostService postservice;
@Autowired
private AccountService accountservice;
        @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id,Model model,Principal principal) {
        Optional<Post>optionalpost=postservice.getById(id);
        String authuser="username";
        if(optionalpost.isPresent()){
            Post post=optionalpost.get();
            model.addAttribute("post", post);


            if(principal!=null){
                authuser=principal.getName();
            }
            if(authuser.equals(post.getAccount().getUsername())){
                model.addAttribute("isOwner", true);
            }
            else{
                model.addAttribute("isOwner", false);
            }
            return "post_views/post";
        }
        else{
            return "404";
        }
    }

    // get  mapping for adding post.
    @GetMapping("/posts/add_post")
    @PreAuthorize("isAuthenticated()")
    public String addPost(Model model,Principal principal) {
        String authUser="username";
        if(principal!=null){
            authUser=principal.getName();
        }
        Optional<Account>optionalAccount=accountservice.findByUsername(authUser);
        if(optionalAccount.isPresent()){
            Post post=new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_views/post_add";
        }
        else{
            return "redirect:/";
        }
         
    }
    // post mapping to save enetered post.
    @PostMapping("/posts/add_post")
    @PreAuthorize("isAuthenticated()")
    public String addPostHandler(@Valid@ModelAttribute Post post,BindingResult bresult,Principal principal) {
        if(bresult.hasErrors()){
            return "post_views/post_add";
        }
       String authUser="username";
       if(principal!=null){
        authUser=principal.getName();
       }
       if(post.getAccount().getUsername().compareToIgnoreCase(authUser)<0){
        return "redirect:/?error";
       }
       postservice.save(post);
        return "redirect:/post/"+post.getId();
    }

    // get mapping to edit a post.
    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit( @PathVariable Long id,Model model) {
        Optional<Post>optionalpost=postservice.getById(id);
        if(optionalpost.isPresent()){
            Post post=optionalpost.get();
            model.addAttribute("post", post);
            return"post_views/post_edit";
        }
        else{
            return "404";
        }
    }

    // post mapping to update changes recieved.
    @PostMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@Valid @ModelAttribute Post post,BindingResult bResult,@PathVariable Long id) {
        if(bResult.hasErrors()){
            return"post_views/post_edit";
        }
        Optional<Post>optionalpost=postservice.getById(id);
        if(optionalpost.isPresent()){
            Post existingpost=optionalpost.get();
            existingpost.setTitle(post.getTitle());
            existingpost.setBody(post.getBody());
            postservice.save(existingpost);
             
        }
        return "redirect:/post/"+post.getId();
         
    }

    // get mapping to delete particular post.
    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id,Principal principal) {
         Optional<Post>optionalpost=postservice.getById(id);
         String authuser="username";
         if(optionalpost.isPresent()){
            Post target=optionalpost.get();
            if(principal!=null){
                authuser=principal.getName();
            }
            if(target.getAccount().getUsername().equals(authuser)){
                postservice.delete(target);
            }
            return "redirect:/home";
         }
        
       return "redirect:/?error";
    }
    
    
    
    
    
}
