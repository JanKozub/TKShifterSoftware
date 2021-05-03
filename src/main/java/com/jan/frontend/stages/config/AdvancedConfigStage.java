package com.jan.frontend.stages.config;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.config.advanced.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPortException;

public class AdvancedConfigStage extends Stage {
    private final AddressBox addrBox = new AddressBox();
    private final NumberField numberField = new NumberField();
    private final InvCheckBox invX = new InvCheckBox("X Inverted", 10);
    private final InvCheckBox invY = new InvCheckBox("Y Inverted", 40);
    private final InvCheckBox invT = new InvCheckBox("T Inverted", 70);
    private volatile String lastOffset = "";

    public AdvancedConfigStage(SerialService serialService) {
        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                updateOffset(event.getCurrentOffset(addrBox.getValue()));
                setStates(event.isXInverted(), event.isYInverted(), event.isTInverted());
            }
        };
        Group root = new Group();

        CenteredLabel offsetLabel = new CenteredLabel("Offset:");

        SetButton setButton = new SetButton();
        setButton.setLayoutX(220);
        setButton.setOnAction(a -> onClick(serialService));

        Group offsetGroup = new Group(addrBox, numberField, setButton);
        offsetGroup.setLayoutY(25);

        CenteredLabel gearInLabel = new CenteredLabel("Neutral Borders:");
        gearInLabel.setLayoutY(75);

        BorderGroup borderGroup = new BorderGroup(serialService);

        CenteredLabel invertAxisLabel = new CenteredLabel("Invert Axis:");
        invertAxisLabel.setLayoutY(150);

        Group checkBoxGroup = new Group(invX, invY, invT);
        invX.setOnAction(actionEvent -> onChecked("X", serialService));
        invY.setOnAction(actionEvent -> onChecked("Y", serialService));
        invT.setOnAction(actionEvent -> onChecked("T", serialService));
        checkBoxGroup.setLayoutY(175);
        checkBoxGroup.setLayoutX(122);

        root.getChildren().addAll(offsetLabel, offsetGroup, gearInLabel, borderGroup, invertAxisLabel, checkBoxGroup);
        setTitle("Advanced config");
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);
        getIcons().add(ImageService.getLogo());
        setScene(new Scene(root, 320, 280));

        setOnShowing(e -> serialService.addListener(serialServiceListener));
        setOnCloseRequest(e -> serialService.removeListener(serialServiceListener));
    }

    private void onChecked(String ax, SerialService serialService) {
        try {
            serialService.writeString("inv" + ax);
        } catch (SerialPortException e) {
            Platform.runLater(() -> new SendErrorAlert().showAndWait());
        }
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

    private void setStates(boolean x, boolean y, boolean t) {
        invX.setSelected(x);
        invY.setSelected(y);
        invT.setSelected(t);
    }

    private void updateOffset(String currentOffset) {
        if (!currentOffset.equals(lastOffset)) {
            Platform.runLater(() -> numberField.setText(currentOffset));
            lastOffset = currentOffset;
        }
    }
}
