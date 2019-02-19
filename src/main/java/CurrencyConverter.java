import com.google.gson.Gson;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * This is the main class of the currency converter.
 */
public class CurrencyConverter implements java.io.Serializable {

    private String storedBase;
    private LocalDateTime storedDate;
    private HashMap<String, BigDecimal> storedRates;

    private LocalDateTime updateDateTime;
    private boolean justUpdated;
    private HashMap<String, String> currencyNames;

    /**
     * Constructor for the currency converter
     * Initially sets most state to null when running application for the first time.
     * The state is updated after the JSON currency data is updated
     */
    public CurrencyConverter() {
        this.storedBase = null;
        this.storedDate = null;
        this.storedRates = null;

        this.updateDateTime = null;
        this.justUpdated = false;
        this.currencyNames = createCurrencyNames();
    }

    /**
     * Establishes a HashMap of all currencies supported by the API with their abbreviations as their values
     * @return HashMap of currencies
     */
    private static HashMap<String, String> createCurrencyNames() {
        HashMap<String, String> currencyNames = new HashMap<>();
        currencyNames.put("Australian Dollar", "AUD");
        currencyNames.put("Bulgarian Lev", "BGN");
        currencyNames.put("Brazilian Real", "BRL");
        currencyNames.put("Canadian Dollar", "CAD");
        currencyNames.put("Swiss Franc", "CHF");
        currencyNames.put("Chinese Yuan", "CNY");
        currencyNames.put("Czech Koruna", "CZK");
        currencyNames.put("Danish Krone", "DKK");
        currencyNames.put("Euro", "EUR");
        currencyNames.put("British Pound", "GBP");
        currencyNames.put("Hong Kong Dollar", "HKD");
        currencyNames.put("Croatian Kuna", "HRK");
        currencyNames.put("Hungarian Forint", "HUF");
        currencyNames.put("Indonesian Rupiah", "IDR");
        currencyNames.put("Israeli New Shekel", "ILS");
        currencyNames.put("Indian Rupee", "INR");
        currencyNames.put("Icelandic Krona", "ISK");
        currencyNames.put("Japanese Yen", "JPY");
        currencyNames.put("South Korean Won", "KRW");
        currencyNames.put("Mexican Peso", "MXN");
        currencyNames.put("Malaysian Ringgit", "MYR");
        currencyNames.put("Norwegian Kroner", "NOK");
        currencyNames.put("New Zealand Dollar", "NZD");
        currencyNames.put("Philippine Peso", "PHP");
        currencyNames.put("Polish Zloty", "PLN");
        currencyNames.put("Romanian New Lei", "RON");
        currencyNames.put("Russian Rouble", "RUB");
        currencyNames.put("Swedish Krona", "SEK");
        currencyNames.put("Singapore Dollar", "SGD");
        currencyNames.put("Thai Baht", "THB");
        currencyNames.put("Turkish New Lira", "TRY");
        currencyNames.put("United States Dollar", "USD");
        currencyNames.put("South African Rand", "ZAR");
        return currencyNames;
    }

    /**
     * Updates the currency used by the converter.
     * It currently updates in 2 situations:
     *  1. When the API has updated the currency exchange rates (10:00 EST every working day)
     *  2. When there is no serialized file due to either running the program for the first time or the serialized file
     *  being deleted.
     */
    private void updateCurrency() {
        if (this.storedBase == null || this.storedDate == null || this.storedRates == null ||
                LocalDateTime.now().isAfter(this.updateDateTime)) {
            String json = GetJSON.getJSON();
            this.storeCurrency(json);
            this.justUpdated = true;
        }
    }

    /**
     * Stores the data from the API into the converter
     * @param json JSON string returned from the GET request executed to retrieve currency exchange rates
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

    /**
     * Prints the names of all currencies supported by the foreign currency exchange rate API
     * @param names List of all currency names
     */
    private void printCurrencyNames(List<String> names) {
        int count = 0;
        for (String name : names) {
            count++;
            System.out.println(count + ". " + name);
        }
    }

    /**
     * Performs deserialization to recreate the converter object from the serialized file
     * @return Recreated currency converter that was stored in the serialized file
     */
    private static CurrencyConverter deserialize() {
        CurrencyConverter converter = null;

        String fileName = "converter_info.ser";
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(inputStream);

            converter = (CurrencyConverter) in.readObject();

            in.close();
            inputStream.close();
        }
        catch (IOException e) {
            converter = new CurrencyConverter();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return converter;
    }

    /**
     * Stores the currency converter in a serialized file so it may persist beyond the execution of the program.
     * The serialization will allow API calls to stay at a minimal amount.
     * @param converter The currency converter to be stored in the serialized file
     */
    private static void serialize(CurrencyConverter converter) {
        String fileName = "converter_info.ser";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);

            out.writeObject(converter);

            out.close();
            outputStream.close();
        }
        catch (IOException e) {
            System.out.println("File not found");
        }
    }

    public static void main(String[] args) {
        CurrencyConverter converter = deserialize();
        converter.updateCurrency();
        if (converter.justUpdated) {
            converter.justUpdated = false;
            serialize(converter);
        }

        Scanner scanner = new Scanner(System.in);

        String input;
        BigDecimal amount = null;
        while (amount == null) {
            System.out.print("Enter amount to convert: ");
            input = scanner.nextLine();
            System.out.println();
            try {
                amount = new BigDecimal(input);
            }
            catch (NumberFormatException e) {
                System.out.println("Given input is not valid.");
            }
        }

        System.out.println("Select currency to convert to: ");

        Set<String> keys = converter.currencyNames.keySet();
        List<String> names = new ArrayList<>(keys);
        Collections.sort(names);

        converter.printCurrencyNames(names);
        System.out.print("> ");

        int i;
        do {
            input = scanner.nextLine().trim();
            i = Integer.parseInt(input);

            if (i > names.size()) {
                System.out.println("The number inputted does not correspond to a currency.");
                System.out.println();
                System.out.println("Select a currency to convert to: ");
                converter.printCurrencyNames(names);
                System.out.print("> ");
            }
        } while(i > names.size());

        int index = i - 1;
        String selectedName = names.get(index);
        String selectedCurrency = converter.currencyNames.get(selectedName);
        BigDecimal multiplier = converter.storedRates.get(selectedCurrency);

        BigDecimal convertedAmount = amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        System.out.println(convertedAmount);
    }
}
