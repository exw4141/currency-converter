import java.math.BigDecimal;
import java.util.HashMap;

/**
 * The Currency class is modeled after the data retrieved from the exchange rate API.
 * This will allow the JSON string from the API to be converted to a POJO via GSON.
 */
public class Currency {

    private String base;
    private String date;
    private HashMap<String, BigDecimal> rates;

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
