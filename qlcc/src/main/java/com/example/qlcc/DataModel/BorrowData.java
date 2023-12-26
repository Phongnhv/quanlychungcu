package com.example.qlcc.DataModel;

import java.util.Date;

public class BorrowData {
    private int borrowID, activityID, facilityID;
    private String hostID;
    private String FacilityName, ActivityName, HostName, Status;
    private int Amount;
    private Date BorrowingDate, ReturnDate;

    public BorrowData (int BorrowID,
                       String FacilityName,
                       String hostID,
                       Date BorrowingDate,
                       Date ReturnDate,
                       int activityID,
                       int facilityID,
                       int Amount,
                       String Status,
                       String HostName,
                       String ActivityName){
        this.borrowID = BorrowID;
        this.FacilityName = FacilityName;
        this.hostID = hostID;
        this.BorrowingDate = BorrowingDate;
        this.ReturnDate = ReturnDate;
        this.activityID = activityID;
        this.facilityID = facilityID;
        this.Amount = Amount;
        this.Status = Status;
        this.HostName = HostName;
        this.ActivityName = ActivityName;
    }

    public int getBorrowID(){return borrowID;};
    public String getFacilityName(){return FacilityName;}

    public String getHostID(){return hostID;}

    public String getStatus(){return Status;}
    public Date getBorrowingDate(){return BorrowingDate;}

    public Date getReturnDate(){return ReturnDate;}
    public int getAmount(){return Amount;};
    public int getActivityID(){return activityID;}
    public int getFacilityID(){return facilityID;};
    public String getActivityName(){return ActivityName;}
    public String getHostName(){return HostName;}

}
