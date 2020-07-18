package uk.ac.ed.bikerental;

import java.math.BigDecimal;

public class Deposit implements Deliverable {
    private BigDecimal value;
    private boolean isBeingDelivered;
    private boolean paid;
    private boolean returned;

    public BigDecimal getValue() {
        return value;
    }

    public boolean isBeingDelivered() {
        return isBeingDelivered;
    }

    public boolean isPaid() {
        return paid;
    }

    public boolean isReturned() {
        return returned;
    }

    public Deposit(BigDecimal value, boolean depositPaid, boolean depositReturned) {
        super();
        this.value = value;
        this.isBeingDelivered = false;
        this.paid = depositPaid;
        this.returned = depositReturned;
    }

    public Deposit(String value, boolean depositPaid, boolean depositReturned) {
        super();
        this.value = new BigDecimal(value);
        this.isBeingDelivered = false;
        this.paid = depositPaid;
        this.returned = depositReturned;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isBeingDelivered ? 1231 : 1237);
        result = prime * result + (paid ? 1231 : 1237);
        result = prime * result + (returned ? 1231 : 1237);
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        Deposit other = (Deposit) obj;
        if (isBeingDelivered != other.isBeingDelivered)
            return false;
        if (paid != other.paid)
            return false;
        if (returned != other.returned)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    // When bike provider use delivery system to deliver deposit to customer it
    // means that delivery will collect deposit from customer on drop off.
    // and then it is assumed that delivery will transfer money to bike provider
    // outside the system.
    @Override
    public void onPickup() {
        isBeingDelivered = true;

    }

    @Override
    public void onDropoff() {
        isBeingDelivered = false;
        paid = true;

    }

    public void depositReturn() {
        returned = true;

    }

    public void depositPaid() {
        paid = true;

    }

}
