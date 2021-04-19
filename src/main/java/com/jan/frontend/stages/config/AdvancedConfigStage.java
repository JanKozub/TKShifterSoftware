package com.jan.frontend.stages.config;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;
import com.jan.backend.ImageService;
import com.jan.backend.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.config.advanced.AddressBox;
import com.jan.frontend.components.config.advanced.NumberField;
import com.jan.frontend.components.config.advanced.SetButton;

public class AdvancedConfigStage extends Stage {
    public AdvancedConfigStage(SerialService serialService) {
        Group root = new Group();

        AddressBox addrBox = new AddressBox();
        NumberField numberField = new NumberField();
        SetButton setButton = new SetButton();
        setButton.setOnAction(a -> onClick(serialService, addrBox.getValue(), numberField.getText()));

        root.getChildren().addAll(addrBox, numberField, setButton);
        setTitle("Offset config");
        getIcons().add(ImageService.getImage("/icons/logo32x32.png"));
        setScene(new Scene(root, 260, 70));
        setResizable(false);
    }

    private void onClick(SerialService serialService, int addr, String value) {
        close();
        try {
            serialService.writeString("o=" + (addr - 1) + ";" + value);
        } catch (SerialPortException serialPortException) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }
}
