package com.jan.frontend.components.mainStage;

import javafx.scene.control.Button;
import jssc.SerialPort;
import jssc.SerialPortException;
import com.jan.backend.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;

public class MemoryButton extends Button {
    public MemoryButton(SerialService serialService) {
        setText("Restore calibration to default");
        setMinSize(210, 75);
        setOnAction(event -> onClick(serialService));
        setLayoutY(200);
        setLayoutX(20);
    }

    private void onClick(SerialService serialService) {
        try {
            serialService.writeString("clear");
        } catch (SerialPortException e) {
            new SendErrorAlert().showAndWait();
        }
    }
}
