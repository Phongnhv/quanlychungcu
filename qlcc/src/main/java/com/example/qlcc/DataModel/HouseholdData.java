package com.example.qlcc.DataModel;

public class HouseholdData {
    private final int HouseID;
    private final int residentNum;
    private final String HouseOwner;
    private final String HouseName;
    private final String HouseOwnerID;
    public HouseholdData (int HouseID, int residentNum, String HouseOwner, String HouseName, String HouseOwnerID){
        this.HouseID = HouseID;
        this.residentNum = residentNum;
        this.HouseOwner = HouseOwner;
        this.HouseName = HouseName;
        this.HouseOwnerID = HouseOwnerID;
    }

    public int getHouseID(){return HouseID;}

    public String getHouseOwnerID(){return HouseOwnerID;}

    public int getResidentNum(){return residentNum;}

    public String getHouseOwner(){return HouseOwner;}

    public String getHouseName(){return HouseName;}
}
