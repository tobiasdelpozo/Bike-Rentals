package uk.ac.ed.bikerental;

public class Customer {
    private String firstName;
    private String lastName;
    private Location address;
    private String phoneNumber;
    private String email;
    private String password; // we have not implemented any password validation

    public Customer(String firstName, String lastName, String address, String postCode, String phoneNumber,
            String email, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = new Location(postCode, address);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        BikeRentalSystem.getInstance().addCusomter(this);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Location getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
