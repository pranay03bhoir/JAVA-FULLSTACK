package example.car.setter.injection;

public class Car {
    private Specification specification;

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public void displayCarDetails(){
        System.out.println("Car details: " + specification.toString());
    }
}
