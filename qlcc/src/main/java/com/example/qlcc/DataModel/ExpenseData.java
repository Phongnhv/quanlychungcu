package com.example.qlcc.DataModel;

import java.sql.Date;

public class ExpenseData {
    private Integer no;
    private String name;
    private String Category;
    private int Amount;
    private int maxAmount;
    private String description;

    public ExpenseData( Integer no,
                        String name,
                        String Category,
                        int Amount,
                        int MaxAmount,
                        String description){
        this.no = no;
        this.name = name;
        this.Category = Category;
        this.Amount = Amount;
        this.maxAmount = MaxAmount;
        this.description = description;
    }

    public Integer getNo(){
        return no;
    }
    public String getName(){
        return name;
    }
    public String getCategory(){
        return Category;
    }
    public int getAmount(){
        return Amount;
    }
    public String getDescription(){
        return description;
    }

    public int getMaxAmount(){return maxAmount;}
}
