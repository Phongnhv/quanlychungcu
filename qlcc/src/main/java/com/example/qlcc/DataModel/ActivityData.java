package com.example.qlcc.DataModel;

import java.util.Date;

public class ActivityData {
    private Integer id;
    private String name;
    private String hostID;
    private String location;
    private Date date;
    private String description;
    private String hostname;

    public ActivityData(Integer no,
                        String name,
                        String hostname,
                        String hostID,
                        String location,
                        Date date,
                        String description){
        this.id = no;
        this.name = name;
        this.location = location;
        this.date = date;
        this.description = description;
        this.hostID = hostID;
        this.hostname = hostname;
    }

    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
    public String getHostname(){return hostname;}
    public String getHostID(){return hostID;}
    public Date getDate(){
        return date;
    }
    public String getDescription(){
        return description;
    }
}
