package com.example.SpringWorking.util.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class emailDetails {
    // class memebers
    private String recipient;
    private String msgBody;
    private String subject;
    
}
