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
    private volatile String currentOffset;
    private final InvCheckBox invX = new InvCheckBox("X Inverted", 10);
    private final InvCheckBox invY = new InvCheckBox("Y Inverted", 40);
    private final InvCheckBox invT = new InvCheckBox("T Inverted", 70);

    public AdvancedConfigStage(SerialService serialService) {
        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                currentOffset = event.getData()[4 + addrBox.getValue()];
                setStates(event.getData()[11], event.getData()[12], event.getData()[13]);
            }
        };
        Group root = new Group();

        CenteredLabel offsetLabel = new CenteredLabel("Offset:");

        addrBox.setOnHidden(a -> numberField.setText(currentOffset));

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

        setOnShown(e -> numberField.setText(currentOffset));
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

    private void setStates(String x, String y, String t) {
        invX.setSelected(x.equals("1"));
        invY.setSelected(y.equals("1"));
        invT.setSelected(t.equals("1"));
    }
}
