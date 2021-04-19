package com.jan.frontend.components.alerts;

public class OpenPortErrorAlert extends UniversalErrorAlert {
    public OpenPortErrorAlert() {
        setHeaderText("Serial port error");
        setContentText("Failed to open selected port");
    }
}
