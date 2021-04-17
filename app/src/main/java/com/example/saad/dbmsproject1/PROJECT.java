package com.example.saad.dbmsproject1;

public class PROJECT {

    public int id;
    public int uid;
    public String name,Abstract,url, time;

    public PROJECT() {}

    public PROJECT (int id, int uid,  int did, String name, String Abstract, String url, String time) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.Abstract = Abstract;
        this.url = url;
        this.time = time;
    }

    public int get_id(){
        return this.id;
    }
    public void set_id(int id){
        this.id = id;
    }

    public int get_uid(){
        return this.uid;
    }
    public void set_uid(int uid){
        this.uid = uid;
    }

    public String get_name(){
        return this.name;
    }
    public void set_name(String name){
        this.name = name;
    }

    public String get_abstract(){
        return this.Abstract;
    }
    public void set_abstract(String Abstract){
        this.Abstract = Abstract;
    }

    public String get_url(){
        return this.url;
    }
    public void set_url(String url){
        this.url = url;
    }

    public String get_time(){
        return this.time;
    }
    public void set_time(String time){
        this.time = time;
    }

}
