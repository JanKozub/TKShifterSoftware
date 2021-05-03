package com.jan.frontend.components.config.advanced;

import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.SendErrorAlert;
import javafx.application.Platform;
import javafx.scene.Group;
import jssc.SerialPortException;

public class BorderGroup extends Group {

    private final NumberField numberField = new NumberField();
    private final BorderBox borderBox = new BorderBox();
    private volatile String label = "";

    public BorderGroup(SerialService serialService) {

        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                if (borderBox.getValue().equals("U")) {
                    label = event.getUCHP();
                } else {
                    label = event.getLCHP();
                }
            }
        };
        serialService.addListener(serialServiceListener);

        borderBox.setOnHidden(a -> numberField.setText(label));

        SetButton setButton = new SetButton();
        setButton.setOnAction(e -> onClick(serialService));
        setButton.setLayoutX(220);

        getChildren().addAll(borderBox, numberField, setButton);

        Platform.runLater(() -> numberField.setText(label));
        setLayoutY(100);
    }

    public void onClick(SerialService serialService) {
        try {
            serialService.writeString(borderBox.getValue() + "=" + numberField.getText());
        } catch (SerialPortException e) {
            Platform.runLater(() -> new SendErrorAlert().showAndWait());
        }
        numberField.clear();
    }

}
