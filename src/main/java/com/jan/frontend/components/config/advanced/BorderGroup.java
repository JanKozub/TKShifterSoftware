package com.jan.frontend.components.config.advanced;

import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.SendErrorAlert;
import javafx.application.Platform;
import javafx.scene.Group;
import jssc.SerialPortException;

public abstract class BorderGroup extends Group {

    private final NumberField numberField = new NumberField();
    private volatile String label;

    public BorderGroup(SerialService serialService, String letter) {
        int id = 9;
        if (letter.equals("L"))
            id = 10;

        int finalId = id;
        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                label = event.getData()[finalId];
            }
        };
        serialService.addListener(serialServiceListener);

        numberField.setLayoutX(40);
        numberField.setMaxWidth(170);

        SetButton setUpper = new SetButton();
        setUpper.setOnAction(e -> onClick(serialService, letter));
        setUpper.setLayoutX(220);

        getChildren().addAll(numberField, setUpper);

        Platform.runLater(() -> numberField.setText(label));
    }

    public void onClick(SerialService serialService, String letter) {
        try {
            serialService.writeString(letter + "=" + numberField.getText());
        } catch (SerialPortException e) {
            Platform.runLater(() -> new SendErrorAlert().showAndWait());
        }
        numberField.clear();
    }

}
