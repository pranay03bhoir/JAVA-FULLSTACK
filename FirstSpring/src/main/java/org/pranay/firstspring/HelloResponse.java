package org.pranay.firstspring;

public class HelloResponse {
    private String message;

    public HelloResponse(String message) {
        this.message = message;
    }

    public void setMessage (String message){
        this.message = message;
    }

    public Object getMessage (){
        return this.message;
    }
}
