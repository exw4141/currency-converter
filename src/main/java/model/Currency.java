package model;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The Currency class is modeled after the data retrieved from the exchange rate API.
 * This will allow the JSON string from the API to be converted to a POJO via GSON.
 * The data of the Currency POJO will be stored within a StoredCurrency object which is serialized.
 */
public class Currency {

    private String base;
    private String date;
    private HashMap<String, BigDecimal> rates;

    /**
     * Constructor of a Currency object.
     * @param base The currency to convert from which always USD (U.S. Dollar) at this time
     * @param date The date of currency data retrieval
     * @param rates The rates required to convert to other currencies
     */
    public Currency(String base, String date, HashMap<String, BigDecimal> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return this.base;
    }

    public String getDate() {
        return this.date;
    }

    public HashMap<String, BigDecimal> getRates() {
        return this.rates;
    }
}
