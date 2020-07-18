package uk.ac.ed.bikerental;

import java.util.Collection;

public class Booking {
    private long bookingId;
    private Quote quote;
    private Customer customer;
    private boolean storeCollection; // all operation related of delivery happens in BikeRentalSystem, but good to
                                     // keep this information
    private String orderSummary;
    private boolean completed;
    private Deposit deposit;
    Location customerAddress;

    public Booking(long bookingId, Quote quote, Customer customer, boolean storeCollection, Location customerAddress,
            String orderSummary) {
        super();
        this.bookingId = bookingId;
        this.quote = quote;
        this.deposit = quote.getDeposit();
        this.customer = customer;
        this.storeCollection = storeCollection;
        this.orderSummary = orderSummary;
        this.completed = false;
        this.customerAddress = customerAddress;

    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void registerDepositReturn() {
        deposit.depositReturn();
    }

    public void makeCompleted() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String toString() {
        String stringOut = "";

        stringOut += "BookingId: " + bookingId + "\nCustomer Name: " + customer.getFirstName() + " "
                + customer.getLastName() + "\n";
        stringOut += "Order Summary: " + orderSummary + "\n";
        // stringOut += "Completed: " + completed;

        return stringOut;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void sendConfirmation() {
        // TODO supposed to send some confirmation but given that we don't have anything
        // to send the confirmation to we have left it empty
    }

    public Quote getQuote() {
        return quote;
    }

    public void depositPaid() {
        this.deposit.depositPaid();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customer == null) ? 0 : customer.hashCode());
        result = prime * result + ((orderSummary == null) ? 0 : orderSummary.hashCode());
        result = prime * result + ((quote == null) ? 0 : quote.hashCode());
        result = prime * result + (storeCollection ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Booking other = (Booking) obj;
        if (customer == null) {
            if (other.customer != null)
                return false;
        } else if (!customer.equals(other.customer))
            return false;
        if (orderSummary == null) {
            if (other.orderSummary != null)
                return false;
        } else if (!orderSummary.equals(other.orderSummary))
            return false;
        if (quote == null) {
            if (other.quote != null)
                return false;
        } else if (!quote.equals(other.quote))
            return false;
        if (storeCollection != other.storeCollection)
            return false;
        return true;
    }

    public void bikesGivenToCustomer() {
        Collection<Bike> bikes = quote.getBikes();
        for (Bike bike : bikes) {
            bike.setInStore(false);
        }
    }

}
