package com.jan.frontend.components.bordersConfig;

import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jssc.SerialPortException;

public class BorderField extends Group {
    private final NumberField numberField = new NumberField();
    private boolean inputType = false;

    public BorderField(SerialService serialService, String value) {
        Label numberLabel = new Label(value + ":");
        numberLabel.getStyleClass().add("border-label");

        numberField.getStyleClass().addAll("text-field", "border-field");

        if (value.equals("U")) {
            inputType = true;
            numberField.setText(serialService.getSerialData().getUCHP());
            setLayoutY(230);
        } else if (value.equals("L")) {
            inputType = true;
            numberField.setText(serialService.getSerialData().getLCHP());
            setLayoutY(270);
        } else {
            numberField.setText(serialService.getSerialData().getCurrentOffset(Integer.parseInt(value)));
            setLayoutY(Integer.parseInt(value) * 40);
        }

        Button setButton = new Button("Set");
        setButton.getStyleClass().add("border-set-button");
        setButton.setOnAction(e -> {
            try {
                if (inputType) {
                    serialService.writeString(value + "=" + numberField.getText());
                } else {
                    serialService.writeString("o=" + (Integer.parseInt(value) - 1) + ";" + numberField.getText());
                }
            } catch (SerialPortException ex) {
                Platform.runLater(() -> new SendErrorAlert().showAndWait());
            }
        });

        getChildren().addAll(numberLabel, numberField, setButton);
    }
}
