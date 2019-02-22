package controller;

import model.StoredCurrency;
import view.ConverterCLI;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * The ConverterController class handles the interactions between the model and view.
 */
public class ConverterController {

    private StoredCurrency storedCurrency;
    private ConverterCLI cli;
    private HashMap<String, String> currencyNames = createCurrencyNames();

    /**
     * Constructor for a ConverterController object.
     * The currency stored in the serialized file is updated within the constructor.
     */
    public ConverterController() {
        this.storedCurrency = deserialize();
        this.storedCurrency = updateCurrency();
        this.cli = new ConverterCLI();
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
     * Updates the data of storedCurrency if needed.
     * If updated, the object is serialized in the file.
     * @return The possibly updated StoredCurrency object
     */
    private StoredCurrency updateCurrency() {
        storedCurrency.update();
        if (storedCurrency.getJustUpdated()) {
            storedCurrency.setJustUpdated(false);
            serialize(storedCurrency);
        }
        return storedCurrency;
    }

    /**
     * Converts the inputted monetary amount to the selected currency
     * @param amount The amount to convert to the new currency
     * @param rate The rate of conversion from the original currency to the new currency
     * @return The converted monetary amount
     */
    public BigDecimal convert(BigDecimal amount, BigDecimal rate) {
        BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        System.out.println(convertedAmount);
        return convertedAmount;
    }

    /**
     * Takes the user's requested amount to convert and creates a BigDecimal object out of it.
     * If the input is not in the form of a decimal number, the user will be requested to enter a valid amount.
     * @return The amount to convert in the form of a BigDecimal
     */
    public BigDecimal requestAmountInput() {
        String input;
        BigDecimal amount = null;
        while (amount == null) {
            input = this.cli.getAmountInput();
            try {
                amount = new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Given input is not valid.");
            }
        }
        return amount;
    }

    /**
     * Takes the user's requested numerical option and finds the corresponding currency
     * @return The currency corresponding to the number inputted by the user
     */
    public String requestNewCurrency() {
        System.out.println("Select currency to convert to: ");

        Set<String> keys = currencyNames.keySet();
        List<String> names = new ArrayList<>(keys);
        Collections.sort(names);

        printCurrencyNames(names);
        System.out.print("> ");

        int index = this.cli.getNewCurrencyIndex(names);
        return names.get(index);
    }

    /**
     * Finds the rate of conversion corresponding to the user's requested currency.
     * @param newCurrency The abbreviation of the currency to convert to
     * @return The rate required to convert from the original currency to the new currency.
     */
    public BigDecimal getCurrencyRate(String newCurrency) {
        String abbreviation = this.currencyNames.get(newCurrency);
        return this.storedCurrency.getRate(abbreviation);
    }

    /**
     * Prints the names of all currencies supported by the foreign exchange rate API
     * @param names List of all currency names
     */
    public static void printCurrencyNames(List<String> names) {
        int count = 0;
        for (String name : names) {
            count++;
            System.out.println(count + ". " + name);
        }
    }

    /**
     * Performs deserialization to recreate the StoredCurrency object from the serialized file
     * @return Recreated StoredCurrency object that was stored in the serialized file
     */
    private static StoredCurrency deserialize() {
        StoredCurrency storedCurrency = null;

        String fileName = "stored_currency.ser";
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(inputStream);

            storedCurrency = (StoredCurrency) in.readObject();

            in.close();
            inputStream.close();
        }
        catch (IOException e) {
            storedCurrency = new StoredCurrency();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return storedCurrency;
    }

    /**
     * Stores the current currency data in a serialized file so it may persist beyond the execution of the program.
     * The serialization will allow API calls to stay at a minimal amount.
     * @param currency The currency to be stored for future use
     */
    private static void serialize(StoredCurrency currency) {
        String fileName = "stored_currency.ser";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);

            out.writeObject(currency);

            out.close();
            outputStream.close();
        }
        catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
