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
    private volatile String lastLabel = "var";

    public BorderGroup(SerialService serialService) {

//        SerialServiceListener serialServiceListener = new SerialServiceListener() {
//            @Override
//            public void onValueUpdate(SerialPortValueEvent event) {
//                updateLabel(event);
//            }
//        };
//        serialService.addListener(serialServiceListener);

        borderBox.setOnAction(a -> lastLabel = "");

        SetButton setButton = new SetButton();
        setButton.setOnAction(e -> onClick(serialService));
        setButton.setLayoutX(220);

        getChildren().addAll(borderBox, numberField, setButton);

        setLayoutY(155);
    }

    private void onClick(SerialService serialService) {
        try {
            serialService.writeString(borderBox.getValue() + "=" + numberField.getText());
        } catch (SerialPortException e) {
            Platform.runLater(() -> new SendErrorAlert().showAndWait());
        }
        numberField.clear();
    }

    private void updateLabel(SerialPortValueEvent event) {
        String label = borderBox.getValue().equals("U") ? event.getUCHP() : event.getLCHP();
        if (!lastLabel.equals(label)) {
            Platform.runLater(() -> numberField.setText(label));
            lastLabel = label;
        }
    }
}
