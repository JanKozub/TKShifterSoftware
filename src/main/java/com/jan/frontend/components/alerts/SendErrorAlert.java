package com.jan.frontend.components.alerts;

public class SendErrorAlert extends UniversalErrorAlert {
    public SendErrorAlert() {
        setHeaderText("Serial communication error");
        setContentText("Failed to send data to device");
    }
}
