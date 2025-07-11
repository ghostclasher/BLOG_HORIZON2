package com.example.SpringWorking.util.constants;

public enum Privillages {
    RESET_ANY_USER_PASSWORD(1," RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2,"ACCESS_ADMIN_PANEL");


    private Long privillageId;
    private String privillageName;
     private Privillages(long authorityid,String authorityString){
        this.privillageId=authorityid;
        this.privillageName=authorityString;
     }

     public Long getAuthorityId(){
        return privillageId;
     }


     public String getprivillageName(){
        return privillageName;
     }

}
