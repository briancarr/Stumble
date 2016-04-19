package com.stumbleapp.stumble;

/**
 * Created by Me on 17/04/2016.
 */
public class Stream {

    String name;
    String user;
    String location;
    String url;
    String date;
    String time;

    public Stream(){

    }

    public Stream(String name, String user, String location, String url, String date, String time){
        this.name = name;
        this.user = user;
        this.location = location;
        this.url = url;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getLocation(){
        return  location;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    //    @JsonIgnore
//    public String getURI() {
//        return URI;
//    }
}
