package org.fasttrackit.Domain;

public class MyException extends Exception {

    private String message;

    MyException(String message){
        this.message=message;
    }

    public String toString(){
        return message;
    }
}
