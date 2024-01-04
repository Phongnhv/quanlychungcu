package com.example.qlcc.DataModel;

public class ExpenseData {
    private final Integer no;
    private final String name;
    private final String Category;
    private final int Amount;
    private final int maxAmount;
    private final String description;

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
