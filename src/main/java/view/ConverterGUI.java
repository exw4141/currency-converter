package view;

import controller.ConverterController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class ConverterGUI extends Application {

    private ConverterController controller = new ConverterController();

    private TextField amount = new TextField();
    private ObservableList<String> currencies =
            FXCollections.observableArrayList(
                    "Australian Dollar",
                    "Bulgarian Lev",
                    "Brazilian Real",
                    "British Pound",
                    "Canadian Dollar",
                    "Chinese Yuan",
                    "Croatian Kuna",
                    "Czech Koruna",
                    "Danish Krone",
                    "Euro",
                    "Hong Kong Dollar",
                    "Hungarian Forint",
                    "Icelandic Krona",
                    "Indian Rupee",
                    "Indonesian Rupiah",
                    "Isreali New Shekel",
                    "Japanese Yen",
                    "Malaysian Ringgit",
                    "Mexican Peso",
                    "Norwegian Kroner",
                    "New Zealand Dollar",
                    "Philippine Peso",
                    "Polish Zloty",
                    "Romanian New Lei",
                    "Russian Rouble",
                    "Singapore Dollar",
                    "South African Rand",
                    "South Korean Won",
                    "Swedish Krona",
                    "Swiss Franc",
                    "Thai Baht",
                    "Turkish New Lira",
                    "United States Dollar"
            );
    private final ComboBox<String> currencyOptions = new ComboBox<>(currencies);
    private Text newAmount = new Text("New amount: ");
    private Text errorMessage = new Text();

    /**
     * Sets up the components of the GUI
     * @param primaryStage The GUI to display to the user.
     */
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));

        Label baseCurrencyLabel = new Label("Converting from: United States Dollar");
        root.add(baseCurrencyLabel, 0, 0);

        Label convertLabel = new Label("Amount to convert: ");
        root.add(convertLabel, 0, 1);

        this.amount.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    attemptConversion();
                }
            }
        });
        root.add(this.amount, 1, 1);

        Label currencyLabel = new Label("Currency to convert to: ");
        root.add(currencyLabel, 0, 2);

        this.currencyOptions.setValue(this.currencies.get(0));
        root.add(this.currencyOptions, 1, 2);

        Button convertButton = new Button();
        convertButton.setText("Convert");
        convertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                attemptConversion();
            }
        });
        root.add(convertButton, 0, 3);

        root.add(this.newAmount, 1, 3);

        this.errorMessage.setFill(Color.RED);
        this.errorMessage.setWrappingWidth(200);
        root.add(this.errorMessage, 1, 4);

        primaryStage.setTitle("Currency Converter");
        primaryStage.setScene(new Scene(root, 550, 500));
        primaryStage.show();
    }

    private void attemptConversion() {
        try {
            BigDecimal amountToConvert = getAmount();

            String selectedCurrency = getSelectedCurrency();
            BigDecimal rate = controller.getCurrencyRate(selectedCurrency);

            BigDecimal newAmount = controller.convert(amountToConvert, rate);
            setNewAmount(newAmount);
            setErrorMessage("");
        }
        catch (NumberFormatException e) {
            setErrorMessage("The inputted value is not able to be converted");
        }
    }

    private BigDecimal getAmount() {
        String amountInput = this.amount.getText();
        return new BigDecimal(amountInput);
    }

    private String getSelectedCurrency() {
        return currencyOptions.getValue();
    }

    private void setNewAmount(BigDecimal newAmount) {
        String newText = "New amount: " + newAmount.toString();
        this.newAmount.setText(newText);
    }

    private void setErrorMessage(String message) {
        this.errorMessage.setText(message);
    }

    public static void openGUI() {
        launch();
    }
}
