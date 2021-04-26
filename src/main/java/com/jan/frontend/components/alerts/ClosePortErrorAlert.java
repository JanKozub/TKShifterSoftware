package com.jan.frontend.components.alerts;

public class ClosePortErrorAlert extends UniversalErrorAlert {
    public ClosePortErrorAlert() {
        setHeaderText("Serial port error");
        setContentText("Error occurred while closing the port(Might be not closed properly)");
        showAndWait();
    }
}
