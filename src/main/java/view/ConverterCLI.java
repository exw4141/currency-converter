package view;

import controller.ConverterController;

import java.util.List;
import java.util.Scanner;

public class ConverterCLI {

    private Scanner scanner;

    /**
     * Constructor for a view.ConverterCLI object
     */
    public ConverterCLI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the directions to enter the amount for conversion
     * @return The amount to convert in the form of a String
     */
    public String getAmountInput() {
        String input;
        System.out.print("Enter amount to convert: ");
        input = this.scanner.nextLine().trim();
        System.out.println();
        return input;
    }

    /**
     * Prints the directions to enter the number corresponding to the currency to convert to
     * @param names Sorted list of currency names
     * @return The index of the currency to convert to
     */
    public int getNewCurrencyIndex(List<String> names) {
        String input;
        int i;
        do {
            input = this.scanner.nextLine().trim();
            i = Integer.parseInt(input);

            if (i > names.size()) {
                System.out.println("The number inputted does not correspond to a currency.");
                System.out.println();
                System.out.println("Select a currency to convert to: ");
                ConverterController.printCurrencyNames(names);
                System.out.print("> ");
            }
        } while(i > names.size());
        return i - 1;
    }
}
