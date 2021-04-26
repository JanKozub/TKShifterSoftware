package com.jan.frontend.stages.config;

import com.jan.backend.ImageService;
import com.jan.backend.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.config.advanced.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPortException;

@SuppressWarnings("BusyWait")
public class AdvancedConfigStage extends Stage {
    private final SerialService serialService;
    private final Label currentOffsetLabel;
    private final AddressBox addrBox;
    private final NumberField numberField;

    public AdvancedConfigStage(SerialService serialService) {
        this.serialService = serialService;
        Group root = new Group();

        CenteredLabel offsetLabel = new CenteredLabel("Offset:");

        currentOffsetLabel = new Label();
        currentOffsetLabel.setFont(Font.font(20));
        currentOffsetLabel.setLayoutX(80);
        currentOffsetLabel.setLayoutY(5);

        addrBox = new AddressBox();
        addrBox.setOnAction(e -> refreshOffsetLabel());
        numberField = new NumberField();
        numberField.setLayoutX(120);

        SetButton setButton = new SetButton();
        setButton.setLayoutX(220);
        setButton.setOnAction(a -> onClick(addrBox.getValue(), numberField.getText()));

        refreshOffsetLabel();

        Group offsetGroup = new Group(addrBox, currentOffsetLabel, numberField, setButton);
        offsetGroup.setLayoutY(25);

        CenteredLabel gearInLabel = new CenteredLabel("Neutral Borders:");
        gearInLabel.setLayoutY(75);

        Group upperGear = new UpperGroup(serialService, "U");

        Group lowerGear = new LowerGroup(serialService, "L");
        lowerGear.setLayoutY(50);

        Group gearInGroup = new Group(upperGear, lowerGear);
        gearInGroup.setLayoutY(100);

        root.getChildren().addAll(offsetLabel, offsetGroup, gearInLabel, gearInGroup);
        setTitle("Advanced config");
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);

        getIcons().add(ImageService.getImage("/icons/logo32x32.png"));
        setScene(new Scene(root, 320, 200));
    }

    private void refreshOffsetLabel() {
        currentOffsetLabel.setText(getCurrentOffset());
    }

    private void onClick(int addr, String value) {
        try {
            serialService.writeString("o=" + (addr - 1) + ";" + value);
            if (!getCurrentOffset().equals(value)) {
                do {
                    Thread.sleep(10);
                } while (!getCurrentOffset().equals(value));
                refreshOffsetLabel();
                numberField.clear();
            }
        } catch (SerialPortException | InterruptedException serialPortException) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }

    private String getCurrentOffset() {
        return serialService.getData()[4 + addrBox.getValue()];
    }
}
