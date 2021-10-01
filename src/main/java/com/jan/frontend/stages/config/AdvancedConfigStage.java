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
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPortException;

public class AdvancedConfigStage extends Stage {
    private final SerialService serialService;
    private final AddressBox addrBox = new AddressBox();
    private final NumberField numberField = new NumberField();
    private final InvCheckBox invX = new InvCheckBox("X Inverted", 10);
    private final InvCheckBox invY = new InvCheckBox("Y Inverted", 40);
    private final InvCheckBox invT = new InvCheckBox("T Inverted", 70);
    private volatile String lastOffset = "";
    private volatile SerialPortValueEvent data;

    public AdvancedConfigStage(SerialService serialService) {
        this.serialService = serialService;
//        SerialServiceListener serialServiceListener = new SerialServiceListener() {
//            @Override
//            public void onValueUpdate(SerialPortValueEvent event) {
//                data = event;
//                updateOffset(event.getCurrentOffset(addrBox.getValue()));
//                setStates(event.isXInverted(), event.isYInverted(), event.isTInverted());
//            }
//        };
        Group root = new Group();

        CenteredLabel offsetLabel = new CenteredLabel("Offset:");

        SetButton setButton = new SetButton();
        setButton.setLayoutX(220);
        setButton.setOnAction(a -> onClick());

        Group offsetGroup = new Group(addrBox, numberField, setButton);
        offsetGroup.setLayoutY(25);

        Button setDefaultButton = new Button("Set default offset");
        setDefaultButton.setOnAction(click -> onSetDefaultClick());
        setDefaultButton.setMinSize(240, 50);
        setDefaultButton.setLayoutY(75);
        setDefaultButton.setLayoutX(40);

        CenteredLabel bordersLabel = new CenteredLabel("Neutral Borders:");
        bordersLabel.setLayoutY(130);

        BorderGroup borderGroup = new BorderGroup(serialService);

        CenteredLabel invertAxisLabel = new CenteredLabel("Invert Axis:");
        invertAxisLabel.setLayoutY(200);

        Group checkBoxGroup = new Group(invX, invY, invT);
        invX.setOnAction(actionEvent -> onChecked("X", serialService));
        invY.setOnAction(actionEvent -> onChecked("Y", serialService));
        invT.setOnAction(actionEvent -> onChecked("T", serialService));
        checkBoxGroup.setLayoutY(225);
        checkBoxGroup.setLayoutX(122);

        root.getChildren().addAll(offsetLabel, offsetGroup, setDefaultButton, bordersLabel, borderGroup, invertAxisLabel, checkBoxGroup);
        setTitle("Advanced config");
        initModality(Modality.WINDOW_MODAL);
        setResizable(false);
        getIcons().add(ImageService.getLogo());
        setScene(new Scene(root, 320, 330));

//        setOnShowing(e -> serialService.addListener(serialServiceListener));
//        setOnCloseRequest(e -> serialService.removeListener(serialServiceListener));
    }

    private void onSetDefaultClick() {
        try {
            for (int i = 0; i < 4; i++) checkAndSetOffset(i);
        } catch (SerialPortException | InterruptedException ex) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }

    @SuppressWarnings("BusyWait")
    private void checkAndSetOffset(int addr) throws InterruptedException, SerialPortException {
        String[] values = {"15", "80", "30", "15"};
        if (!data.getCurrentOffset(addr + 1).equals(values[addr])) {
            serialService.writeString("o=" + addr +";" + values[addr]);
            while (!data.getCurrentOffset(addr + 1).equals(values[addr])) {
                Thread.sleep(10);
            }
        }
    }

    private void onChecked(String ax, SerialService serialService) {
        try {
            serialService.writeString("inv" + ax);
        } catch (SerialPortException e) {
            Platform.runLater(() -> new SendErrorAlert().showAndWait());
        }
    }

    private void onClick() {
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
