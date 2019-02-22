import controller.ConverterController;
import view.ConverterGUI;

import java.math.BigDecimal;

/**
 * This is the main class of the currency converter.
 */
public class CurrencyConverter {

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 1) {
            System.out.println("usage: java CurrencyConverter client_mode");
        }
        else if (args[0].equalsIgnoreCase("CLI")) {
            ConverterController controller = new ConverterController();

            BigDecimal amount = controller.requestAmountInput();
            String newCurrency = controller.requestNewCurrency();
            BigDecimal rate = controller.getCurrencyRate(newCurrency);

            controller.convert(amount, rate);
        }
        else if (args[0].equalsIgnoreCase("GUI")) {
            ConverterGUI.openGUI();
        }
        else {
            System.out.println("usage: java CurrencyConverter client_mode");
        }
    }
}
