package com.jan.frontend.components.alerts;

public class ReadCurrentDataErrorAlert extends UniversalErrorAlert{
    public ReadCurrentDataErrorAlert() {
        setHeaderText("Serial communication error");
        setContentText("Failed to read current gear from device(probably cable was disconnected)");
    }
}
