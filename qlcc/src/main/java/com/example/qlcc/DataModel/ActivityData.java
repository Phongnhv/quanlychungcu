package com.example.qlcc.DataModel;

import java.util.Date;

public class ActivityData {
    private final Integer id;
    private final Date endDate;
    private final String name;
    private final String hostID;
    private final String location;
    private final Date date;
    private final String description;
    private final String hostname;

    public ActivityData(Integer no,
                        String name,
                        String hostname,
                        String hostID,
                        String location,
                        Date date,
                        String description,
                        Date endDate){
        this.id = no;
        this.name = name;
        this.location = location;
        this.date = date;
        this.description = description;
        this.hostID = hostID;
        this.hostname = hostname;
        this.endDate = endDate;
    }
    public Date getEndDate(){
        return endDate;
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
