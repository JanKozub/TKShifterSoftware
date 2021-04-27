package com.jan.frontend.stages.config;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.config.advanced.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPortException;

public class AdvancedConfigStage extends Stage {
    private final AddressBox addrBox = new AddressBox();
    private final NumberField numberField = new NumberField();
    private volatile String currentOffset;

    public AdvancedConfigStage(SerialService serialService) {
        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                currentOffset = event.getData()[4 + addrBox.getValue()];
            }
        };
        Group root = new Group();

        CenteredLabel offsetLabel = new CenteredLabel("Offset:");

        addrBox.setOnHidden(a -> numberField.setText(currentOffset));
        addrBox.getItems().setAll(1, 2, 3, 4);
        addrBox.setValue(1);

        SetButton setButton = new SetButton();
        setButton.setLayoutX(220);
        setButton.setOnAction(a -> onClick(serialService));

        Group offsetGroup = new Group(addrBox, numberField, setButton);
        offsetGroup.setLayoutY(25);

        CenteredLabel gearInLabel = new CenteredLabel("Neutral Borders:");
        gearInLabel.setLayoutY(75);

        BorderGroup borderGroup = new BorderGroup(serialService);

        root.getChildren().addAll(offsetLabel, offsetGroup, gearInLabel, borderGroup);
        setTitle("Advanced config");
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);

        getIcons().add(ImageService.getLogo());
        setScene(new Scene(root, 320, 200));

        setOnShown(e -> numberField.setText(currentOffset));

        setOnShowing(e -> serialService.addListener(serialServiceListener));
        setOnCloseRequest(e -> serialService.removeListener(serialServiceListener));
    }

    private void onClick(SerialService serialService) {
        try {
            serialService.writeString("o=" + (addrBox.getValue() - 1) + ";" + numberField.getText());
            numberField.clear();
        } catch (SerialPortException ex) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }
}
