package com.jan.frontend.components.alerts;

import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import javafx.stage.Stage;

public class ReadCurrentDataErrorAlert extends UniversalErrorAlert {
    public ReadCurrentDataErrorAlert() {
        setHeaderText("Serial communication error");
        setContentText("Failed to read data from device(probably cable was disconnected)");
    }
}
