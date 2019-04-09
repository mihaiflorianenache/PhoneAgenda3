package org.fasttrackit.Domain;

public class Agenda {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public int getId(){return id;}

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public void setId(int id){this.id=id;}
}

