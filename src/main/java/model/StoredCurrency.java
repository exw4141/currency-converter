package model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.HashMap;

/**
 * The StoredCurrency class contains the data taken from the API.
 * More specifically, this class copies the data from the Currency object created by GSON.
 * StoredCurrency stores the date of data retrieval as a LocalDateTime rather than a String so the next time to request
 * data from the API is calculated more easily.
 */
public class StoredCurrency implements Serializable {

    private String storedBase;
    private LocalDateTime storedDate;
    private HashMap<String, BigDecimal> storedRates;

    private LocalDateTime updateDateTime;
    private boolean justUpdated;

    /**
     * Constructor for a StoredCurrency object.
     */
    public StoredCurrency() {
        this.storedBase = null;
        this.storedDate = null;
        this.storedRates = null;

        this.updateDateTime = null;
        this.justUpdated = false;
    }

    /**
     * Updates the currency used by the converter.
     * It currently updates in 2 situations:
     *  1. When the API has updated the currency exchange rates (10:00 EST every working day)
     *  2. When there is no serialized file due to either running the program for the first time or the serialized file
     *  being deleted.
     */
    public void update() {
        if (this.storedBase == null || this.storedDate == null || this.storedRates == null ||
                LocalDateTime.now().isAfter(this.updateDateTime)) {
            String json = GetJSON.getJSON();
            this.storeCurrency(json);
            this.justUpdated = true;
        }
    }

    /**
     * Stores the data from the API.
     * Converts the date field of the Currency object from a String to a LocalDateTime object.
     * @param json JSON string returned from the GET request sent to retrieve currency exchange rates
     */
    private void storeCurrency(String json) {
        Currency currency = createCurrency(json);
        this.storedBase = currency.getBase();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.storedDate = LocalDate.parse(currency.getDate(), dateTimeFormatter).atStartOfDay();
        if (this.storedDate.get(ChronoField.DAY_OF_WEEK) == 5) {
            this.updateDateTime = this.storedDate.plusDays(3).plusHours(10);
        }
        else {
            this.updateDateTime = this.storedDate.plusDays(1).plusHours(10);
        }

        this.storedRates = currency.getRates();
    }

    /**
     * Converts JSON returned from the API to a Currency POJO
     * @param json The JSON string retrieved from the currency exchange rate API
     * @return A currency object containing the data stored in the JSON string
     */
    private static Currency createCurrency(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Currency.class);
    }

    public BigDecimal getRate(String selectedCurrency) {
        return this.storedRates.get(selectedCurrency);
    }

    public boolean getJustUpdated() {
        return this.justUpdated;
    }

    public void setJustUpdated(boolean justUpdated) {
        this.justUpdated = justUpdated;
    }
}
