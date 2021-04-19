package com.jan.frontend.components.alerts;

public class WrongPortError extends UniversalErrorAlert {
    public WrongPortError() {
        setHeaderText("Serial selection error");
        setContentText("Wrong port was selected");
        showAndWait();
    }
}
