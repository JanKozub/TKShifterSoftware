package com.jan.frontend.components.alerts;

public class ReadCurrentGearErrorAlert extends UniversalErrorAlert{
    public ReadCurrentGearErrorAlert() {
        setHeaderText("Serial communication error");
        setContentText("Failed to read current gear from device(probably cable was disconnected)");
    }
}
