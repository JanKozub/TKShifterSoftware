package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialData;
import com.jan.backend.serial.SerialPortErrorEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.bordersConfig.BordersConfig;
import com.jan.frontend.components.FlagsGroup;
import com.jan.frontend.components.alerts.ReadCurrentDataErrorAlert;
import com.jan.frontend.components.mainStage.MyRadioGroup;
import com.jan.frontend.stages.config.HConfigStage;
import com.jan.frontend.stages.config.SeqConfigStage;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainStage extends Stage {

    private final Label infoLabel = new Label();
    private final SerialService serialService;
    private final SerialServiceListener serialServiceListener;
    private int mode;

    public MainStage(SerialService serialService) {
        this.serialService = serialService;

        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onSerialPortError(SerialPortErrorEvent event) {
                showSerialEvent();
            }

            @Override
            public void onValueUpdate(SerialData data) {
                updateValues(data);
            }
        };
        serialService.addListener(serialServiceListener);
        this.serialServiceListener = serialServiceListener;

        ImageView logo = new ImageView(ImageService.getImage("/icons/logo100x60.png"));
        logo.getStyleClass().add("logo");

        Stage stage1 = new HConfigStage(serialService);
        stage1.initOwner(this);
        Stage stage2 = new SeqConfigStage(serialService);
        stage2.initOwner(this);

        Button configButton = new Button("Calibrate");
        configButton.getStyleClass().add("main-button");
        configButton.setOnAction(event -> onConfigButtonClick(stage1, stage2));

        Button restoreDefaultButton = new Button("Restore Default");
        restoreDefaultButton.getStyleClass().addAll("main-button", "default-button");

        Group left = new Group(new MyRadioGroup(serialService, mode), configButton, restoreDefaultButton, new FlagsGroup(), logo);
        left.setLayoutX(15);

        Rectangle center = new Rectangle(350, 350, Paint.valueOf("#111C29"));
        center.getStyleClass().add("center-rect");

        Group right = new Group(new BordersConfig(serialService));
        right.setLayoutX(605);

        infoLabel.getStyleClass().add("info-label");

        setTitle("TK Shifter Calibration software");
        getIcons().add(ImageService.getLogo());
        Group layout = new Group(left, center, right, infoLabel);
        layout.setLayoutY(15);
        Scene scene = new Scene(layout, 800, 426);
        scene.getStylesheets().add("/MainPage.css");
        setScene(scene);
        setResizable(false);

        setOnCloseRequest(windowEvent -> serialService.onClose(serialServiceListener));
    }

    private void showSerialEvent() {
        Platform.runLater(() -> new ReadCurrentDataErrorAlert(serialService, serialServiceListener, this).showAndWait());
    }

    private void updateValues(SerialData data) {
        mode = data.getMode();
        Platform.runLater(() -> infoLabel.setText(data.getMessage()));
    }

    private void onConfigButtonClick(Stage stage1, Stage stage2) {
        if (isInHMode())
            stage1.show();
        else
            stage2.show();
    }

    private boolean isInHMode() {
        return mode == 1;
    }
}
