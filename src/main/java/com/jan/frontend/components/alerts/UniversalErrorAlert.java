package com.jan.frontend.components.alerts;

import com.jan.backend.ImageService;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class UniversalErrorAlert extends Alert {
    public UniversalErrorAlert() {
        super(AlertType.ERROR);
        setTitle("Error occurred!");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageService.getImage("/icons/logo32x32.png"));
    }
}
