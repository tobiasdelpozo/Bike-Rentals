package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Quote {
    private BikeProvider bikeProvider;
    private DateRange dateRange;
    private BigDecimal totalPrice;
    private Deposit deposit;
    private Collection<Bike> bikes;

    public Quote(BikeProvider bikeProvider, DateRange dateRange, BigDecimal totalPrice, BigDecimal deposit,
            Collection<Bike> bikes) {
        this.bikeProvider = bikeProvider;
        this.dateRange = dateRange;
        this.totalPrice = totalPrice;
        this.deposit = new Deposit(deposit, false, false);
        this.bikes = bikes;
    }

    public boolean reserveBikes() {
        if (isEmpty()) {
            return false;
        } else {
            for (Bike bike : bikes) {
                bike.reserve(dateRange);
            }
            return true;
        }

    }

    public boolean isEmpty() {
        return bikes.isEmpty();
    }

    public void registerReturnOfBikes() {
        for (Bike bike : bikes) {
            bike.setInStore(true);
        }
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public Collection<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(Collection<Bike> bikes) {
        this.bikes = bikes;
    }

    public BikeProvider getBikeProvider() {
        return bikeProvider;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bikeProvider == null) ? 0 : bikeProvider.hashCode());
        result = prime * result + ((dateRange == null) ? 0 : dateRange.hashCode());
        result = prime * result + ((totalPrice == null) ? 0 : totalPrice.unscaledValue().shortValue());
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
        Quote other = (Quote) obj;
        if (bikeProvider == null) {
            if (other.bikeProvider != null)
                return false;
        } else if (!bikeProvider.equals(other.bikeProvider))
            return false;
        if (dateRange == null) {
            if (other.dateRange != null)
                return false;
        } else if (!dateRange.equals(other.dateRange))
            return false;
        if (totalPrice == null) {
            if (other.totalPrice != null)
                return false;
        } else if (totalPrice.compareTo(other.totalPrice) != 0)
            return false;
        if (!this.bikeQuantities().equals(other.bikeQuantities())) {
            return false;
        }
        if (this.getDeposit().getValue().compareTo(other.getDeposit().getValue()) != 0) {
            return false;
        }
        return true;
    }

    private Map<BikeType, Integer> bikeQuantities() {
        HashMap<BikeType, Integer> result = new HashMap<BikeType, Integer>();
        for (Bike bike : bikes) {
            if (result.keySet().contains(bike.getType())) {
                result.put(bike.getType(), result.get(bike.getType()) + 1);
            } else {
                result.put(bike.getType(), 1);
            }
        }
        return result;
    }

    public Deposit getDeposit() {
        return deposit;
    }
}
