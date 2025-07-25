package com.example.autowire.constructor;

public class Car {
    private Specification specification;

    public Car(Specification specification){
        this.specification = specification;
    }

//    public void setSpecification(Specification specification) {
//        System.out.println("The setter of the bean specification called");
//        this.specification = specification;
//    }

    public void displayCarDetails(){
        System.out.println("Car details: " + specification.toString());
    }
}
