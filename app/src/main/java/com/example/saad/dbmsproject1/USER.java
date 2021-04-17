package com.example.saad.dbmsproject1;

public class USER {


        public int id;
        public int deptID;
        public String userName;
        public String email;
        public String password;

        public USER(){}
        public USER(int id, String userName, String email, String password, int deptID) {
            this.id = id;
            this.userName = userName;
            this.email = email;
            this.password = password;
            this.deptID = deptID;
        }

        public int get_id(){
            return this.id;
        }
        public void set_id(int id){
            this.id = id;
        }

    public String get_un(){
        return this.userName;
    }
    public void set_un(String un){
        this.userName = un;
    }

    public String get_email(){
        return this.email;
    }
    public void set_email(String email){
        this.email = email;
    }

    public String get_pass(){
        return this.password;
    }
    public void set_pass(String pass){
        this.password = pass;
    }

    public int get_did(){
        return this.deptID;
    }
    public void set_did(int did){
        this.deptID = did;
    }

}




