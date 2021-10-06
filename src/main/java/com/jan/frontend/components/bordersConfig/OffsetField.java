package com.jan.frontend.components.bordersConfig;

import com.jan.backend.serial.SerialService;
import javafx.scene.Group;

public class OffsetField extends Group {
    private final NumberField numberField = new NumberField();

    public OffsetField(SerialService serialService, String value) {
        numberField.getStyleClass().addAll("text-field", "border-field");
//        numberField.setText(serialService.getSerialData().getCurrentOffset(Integer.parseInt(value)));

        numberField.setLayoutX((Integer.parseInt(value) * 44) - 50);

//        Button setButton = new Button("Set");
//        setButton.getStyleClass().add("border-set-button");
//        setButton.setOnAction(e -> {
//            try {
//                if (inputType) {
//                    serialService.writeString(value + "=" + numberField.getText());
//                } else {
//                    serialService.writeString("o=" + (Integer.parseInt(value) - 1) + ";" + numberField.getText());
//                }
//            } catch (SerialPortException ex) {
//                Platform.runLater(() -> new SendErrorAlert().showAndWait());
//            }
//        });

        getChildren().addAll(numberField);
    }

    public int getValue() {
        return Integer.parseInt(numberField.getText());
    }
}
