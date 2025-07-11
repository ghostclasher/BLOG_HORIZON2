package com.example.SpringWorking.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SpringWorking.models.Account;
import com.example.SpringWorking.services.AccountService;
import com.example.SpringWorking.services.EmailService;
import com.example.SpringWorking.util.AppUtil;
import com.example.SpringWorking.util.email.emailDetails;

import jakarta.validation.Valid;


@Controller
public class AccountController {
    @Autowired
    private AccountService accountservice;

    @Autowired
    private EmailService emailservice;
 
    @Value("${password.token.reset.timeout.minutes}")
    private int password_token_timeout;

    @Value("${site.domain}")
    private String sitedomain;




    Account account=new Account();
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String user_registered( @Valid@ModelAttribute Account account,BindingResult result) {
        if(result.hasErrors()){
            return "account_views/register";
        }
        accountservice.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return  "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model,Principal principal) {
        String authUser="username";
        if(principal!=null){
            authUser=principal.getName();
        }
        Optional<Account>optionalaccount=accountservice.findByUsername(authUser);
        if(optionalaccount.isPresent()){
            Account account=optionalaccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return  "account_views/profile";
        }
        else{
            return  "redirect:/?error";
        }
    }


    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String post_profile(@Valid @ModelAttribute Account account,BindingResult bindingResult,Principal principal){
        if(bindingResult.hasErrors()){
            return  "account_views/profile";
        }
        String authUser="username";
        if(principal!=null){
            authUser=principal.getName();
        }
        Optional<Account>optionalaccount=accountservice.findByUsername(authUser);
        if(optionalaccount.isPresent()){
            Account account_by_id=accountservice.findById(account.getAccount_id()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setEmail(account.getEmail());
            account_by_id.setGender(account.getGender());
            account_by_id.setPassword(account.getPassword());
            account_by_id.setUsername(account.getUsername());
            accountservice.save(account_by_id);
            SecurityContextHolder.clearContext();
            return  "account_views/login";
        }
        return"";
       
    }


    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String update_photo(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,Principal principal){
        if(file.isEmpty()){
            attributes.addFlashAttribute("error","No File Uploaded");
            return "redirect:/profile";
        }
        else{
            String filename= StringUtils.cleanPath(file.getOriginalFilename());
            try {
                int length=10;
                boolean useLetters=true;
                boolean useNumbers=true;
                String generatedString=RandomStringUtils.random(length,useLetters,useNumbers);
                String final_photo_name=generatedString + filename;
                String absolute_fileLocation= AppUtil.get_upload_path(final_photo_name);
                Path path=Paths.get(absolute_fileLocation);
                Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message","You successfully uploaded");

                String authUser="username";
                if(principal!=null){
                    authUser=principal.getName();
                }
                Optional<Account>optionalaccount=accountservice.findByUsername(authUser);
                if(optionalaccount.isPresent()){
                    Account account=optionalaccount.get();
                    Account account_by_id=accountservice.findById(account.getAccount_id()).get();
                    account_by_id.setPhoto(absolute_fileLocation);
                    accountservice.save(account_by_id);

                }
                return "redirect:/";

            }
            // try {
            //     TimeUnit.SECONDS.sleep(1);
            // } catch (InterruptedException e) {
            //      Thread.currentThread().interrupt();
            // }
            catch (Exception e) {
                
            }
        }
        return "";
    }
    

     @GetMapping("/forgot-password")
     public String forgot_password(Model model){
        return  "account_views/forgot_password";
     }

     @PostMapping("/reset-password")
     public String reset_password(@RequestParam("email") String _email,RedirectAttributes attributes,Model model){
           Optional<Account>optionalaccount=accountservice.findByEmail(_email);
           if(optionalaccount.isPresent()){
            Account account=accountservice.findById(optionalaccount.get().getAccount_id()).get();
            String reset_token=UUID.randomUUID().toString();
            account.setPassword_reset_token(reset_token);
            account.setPassword_reset_token_expiry(LocalDateTime.now().plusMinutes(password_token_timeout));
            accountservice.save(account);
            String reset_message="This is the reset password link :"+ sitedomain + "change-password?token="+reset_token;
            emailDetails emaildetail=new emailDetails(account.getEmail(),reset_message,"Reset password Demo");
            if(emailservice.sendSimpleEmail(emaildetail)==false){
                attributes.addFlashAttribute("error","Error while sending email,contact admin");
                return "redirect:/forgot-password";
            }
            attributes.addFlashAttribute("message","Password reset mail sent");
            return "redirect:/login";
        
        }
        else{
            attributes.addFlashAttribute("error","no account exist with this email");
            return "redirect:/forgot-password";
        }
        
     }


     @GetMapping("/change-password")
     public String change_password(Model model,@RequestParam("token") String token,RedirectAttributes attributes) {
        if(token.equals("")){
            attributes.addFlashAttribute("error","Invalid Token");
            return  "redirect:/forgot-password";
        }
        Optional<Account>optionalaccount=accountservice.findByToken(token);
        if(optionalaccount.isPresent()){
            Account account=accountservice.findById(optionalaccount.get().getAccount_id()).get();
            LocalDateTime now=LocalDateTime.now();
            if(now.isAfter(optionalaccount.get().getPassword_reset_token_expiry())){
                attributes.addFlashAttribute("error","token expired");
            return "redirect:/forgot-password";
            }
            model.addAttribute("account", account);
            return "account_views/change_password";
        }
        attributes.addFlashAttribute("error","Invalid Token");
        return "redirect:/forgot-password";
          
     }



     @PostMapping("/change-password")
     public String post_change_password(@ModelAttribute  Account account , RedirectAttributes attributes) {
        Account account_by_id=accountservice.findById(account.getAccount_id()).get();
        account_by_id.setPassword(account.getPassword());
        account_by_id.setPassword_reset_token("sitedomain");
        accountservice.save(account_by_id);
         attributes.addFlashAttribute("message","Password changed!");
        return "redirect:/login";
     }
     
     


    @GetMapping("/test")
    public String test(Model model) {
        return  "test";
    }

    
    
    
    
}
