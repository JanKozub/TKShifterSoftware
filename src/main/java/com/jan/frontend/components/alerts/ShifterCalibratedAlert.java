package com.jan.frontend.components.alerts;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import com.jan.backend.ImageService;

public class ShifterCalibratedAlert extends Alert {
    public ShifterCalibratedAlert() {
        super(AlertType.INFORMATION);
        setTitle("Success!");
        setHeaderText("Your shifter is now calibrated in selected mode! All data has been saved.");
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageService.getLogo());
    }
}
