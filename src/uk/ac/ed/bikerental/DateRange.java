package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * Represents a range of dates by containing a start and an end date.
 * 
 * @author Tobias & Michal
 */

public class DateRange {
    private LocalDate start, end;

    /**
     * Creates a date range with the specified start and end dates.
     * 
     * @param start The date range's start date.
     * @param end   The date range's end date.
     */

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the date range's start date.
     * 
     * @return a LocalDate representing the start date.
     */

    public LocalDate getStart() {
        return this.start;
    }

    /**
     * Gets the date range's end date.
     * 
     * @return a LocalDate representing the end date.
     */

    public LocalDate getEnd() {
        return this.end;
    }

    /**
     * Calculates the number of years between the start and end date of the date
     * range. That is, it converts the size of the date range into years.
     * 
     * @return A long representing the number of years between the start and end
     *         dates.
     */

    public long toYears() {
        return ChronoUnit.YEARS.between(this.getStart(), this.getEnd());
    }

    /**
     * Calculates the number of days between the start and end date of the date
     * range. That is, it converts the size of the date range into days.
     * 
     * @return A long representing the number of days between the start and end
     *         dates.
     */

    public long toDays() {
        return ChronoUnit.DAYS.between(this.getStart(), this.getEnd());
    }

    /**
     * This method is used to check whether or not two date ranges overlap. It does
     * this by checking four conditions which represent all possible ways 2 dates
     * can overlap. This means that if two dates do overlap or meet one of the
     * criteria for overlapping, then the function returns true. If it has gone
     * through all of the criteria and not found an overlap then it return false.
     * JUST EXPLAIN AN OVERVIEW OF THE FUNCTION, input and output
     * 
     * @param other Another DateRange instance to check for overlap
     * @return Boolean A boolean value representing whether or not the dates
     *         overlap. True if they do, false if they don't
     */

    public Boolean overlaps(DateRange other) {
        LocalDate otherStart = other.getStart();
        LocalDate otherEnd = other.getEnd();
        if (!start.isAfter(otherStart) && !end.isBefore(otherStart)) {
            return true;
        }

        if (!otherStart.isAfter(start) && !otherEnd.isBefore(start)) {
            return true;
        }

        if (!start.isAfter(otherEnd) && !end.isBefore(otherEnd)) {
            return true;
        }

        if (!otherStart.isAfter(end) && !otherEnd.isBefore(end)) {
            return true;
        }
        return false;
    }

    /**
     * This method returns the hash code value for the object on which this method
     * is invoked.
     * 
     * @return int Represents the hash code value for the object on which hashCode()
     *         is invoked.
     */

    @Override
    public int hashCode() {
        // hashCode method allowing use in collections
        return Objects.hash(end, start);
    }

    /**
     * This method checks if some other Object passed to it as an argument is equal
     * to the Object on which it is invoked. This provides a deep comparison of the
     * two objects.
     * 
     * @param obj The Object to which we are comparing to Object which calls the
     *            equals() method.
     * @return boolean Represents whether or not the two objects are equal. Returns
     *         true if they are and false if they are not.
     */

    @Override
    public boolean equals(Object obj) {
        // equals method for testing equality in tests
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateRange other = (DateRange) obj;
        return Objects.equals(end, other.end) && Objects.equals(start, other.start);
    }

    // You can add your own methods here
}
