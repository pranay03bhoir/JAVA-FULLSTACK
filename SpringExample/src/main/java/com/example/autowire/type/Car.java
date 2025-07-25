package com.example.autowire.type;

public class Car {
    private Specification specification;

    public void setSpecification(Specification specification) {
        System.out.println("The setter of the bean specification called");
        this.specification = specification;
    }

    public void displayCarDetails(){
        System.out.println("Car details: " + specification.toString());
    }
}
