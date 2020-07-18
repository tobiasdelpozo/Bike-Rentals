package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class implements PricingPolicy allowing bike providers to offer discounts
 * based on length of the booking
 * 
 *
 */
public class MultidayDiscountsPolicy implements PricingPolicy {

    private Map<BikeType, BigDecimal> prices;
    private SortedMap<Integer, BigDecimal> discounts;

    @Override
    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
        prices.put(bikeType, dailyPrice);

    }

    @Override
    public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
        BigDecimal price = new BigDecimal(0);
        long numOfDays = duration.toDays() + 1;
        BigDecimal percentage = chooseDiscount(numOfDays);
        for (Bike bike : bikes) {
            BigDecimal priceOfBike = prices.get(bike.getType());
            price = price.add(priceOfBike);
        }
        BigDecimal multiplyFactor = new BigDecimal(1);
        multiplyFactor = multiplyFactor.subtract(percentage); // multiply factor is 1 - percentage
        price = price.multiply(multiplyFactor);
        price = price.multiply(new BigDecimal(numOfDays));
        return price;
    }

    public MultidayDiscountsPolicy(Map<Integer, BigDecimal> discounts, Map<BikeType, BigDecimal> prices) {
        this.prices = prices;
        this.discounts = new TreeMap<Integer, BigDecimal>();
    }

    public MultidayDiscountsPolicy() {
        this.prices = new HashMap<BikeType, BigDecimal>();
        this.discounts = new TreeMap<Integer, BigDecimal>();
    }

    /**
     * Allows user to specify/update their pricing policy
     *
     * @param minNumOfDays minimum duration to qualify for a discount
     * @param percentage   the value of discount e.g. for 5% off use "0.05"
     */
    public void setDiscount(Integer minNumOfDays, String percentage) {
        BigDecimal percentageDec = new BigDecimal(percentage);
        discounts.put(minNumOfDays, percentageDec);
    }

    /**
     * Deletes all discounts which allows user to set policy again
     */
    public void resetDiscounts() {
        discounts.clear();
    }

    /**
     * Calculates discount amount for given duration
     *
     * @param numOfDays length in days of hire
     * 
     */
    public BigDecimal chooseDiscount(long numOfDays) {
        BigDecimal result = new BigDecimal(0);
        for (Map.Entry<Integer, BigDecimal> entry : discounts.entrySet()) {
            // it works because map is sorted
            if (numOfDays >= entry.getKey()) {
                result = entry.getValue();
            } else {
                break;
            }
        }
        return result;
    }

}
