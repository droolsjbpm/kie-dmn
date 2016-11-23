package test;

import org.kie.dmn.feel.lang.FEELAccessor;

public class Person {
    private String firstName;
    private String lastName;
    private Address homeAddress;
    
    public Person(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public Person(String firstName, String lastName, Address homeAddress) {
        this(firstName, lastName);
        this.homeAddress = homeAddress;
    }
    
    @FEELAccessor("first name")
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @FEELAccessor("last name")
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;   
    }
    
    @FEELAccessor("home address")
    public Address getHomeAddress() {
        return homeAddress;
    }
    
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person [firstName=").append(firstName).append(", lastName=").append(lastName).append("]");
        return builder.toString();
    }
    
}
