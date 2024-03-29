package com.example.qlcc.DataModel;

import java.util.Date;

public class ResidentData {
    private final String residentID;
    private final String name;
    private final String hometown;
    private final String permanentAddress;
    private final Date birth;
    private final String gender;
    private final String nation;
    private final String role;
    private final int HouseID;
    private final String HouseName;
    private final String Image;

    public ResidentData(String residentID, String name, String hometown, String permanentAddress,
                        Date birth, String gender, String nation, String role, int houseID, String image, String houseName){
        this.residentID = residentID;
        this.name= name;
        this.hometown = hometown;
        this.permanentAddress = permanentAddress;
        this.birth = birth;
        this.gender = gender;
        this.nation = nation;
        this.role = role;
        this.HouseID = houseID;
        this.Image = image;
        this.HouseName = houseName;
    }

    public String getHouseName(){return HouseName;}
    public String getResidentID(){return residentID;}

    public String getName(){return name;}
    public String getHometown(){return hometown;}
    public String getPermanentAddress(){return permanentAddress;}
    public Date getBirth(){return birth;}
    public String getGender(){return gender;}
    public String getNation(){return nation;}
    public String getRole(){return role;}
    public int getHouseID(){return HouseID;}
    public String getImage(){return Image;}
}
