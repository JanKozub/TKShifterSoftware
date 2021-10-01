package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialPortErrorEvent;
import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.ReadCurrentDataErrorAlert;
import com.jan.frontend.components.mainStage.AdvancedConfigButton;
import com.jan.frontend.components.mainStage.MemoryButton;
import com.jan.frontend.components.mainStage.MyRadioGroup;
import com.jan.frontend.stages.config.AdvancedConfigStage;
import com.jan.frontend.stages.config.HConfigStage;
import com.jan.frontend.stages.config.SeqConfigStage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
            public void onValueUpdate(SerialPortValueEvent event) {
                updateValues(event);
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

        BorderPane pane = new BorderPane();

        Group left = new Group(new MyRadioGroup(serialService, mode), configButton, restoreDefaultButton);
        pane.setPadding(new Insets(15));
        pane.setLeft(left);

        Rectangle center = new Rectangle(350, 350, Paint.valueOf("#a6a6a6"));
        center.getStyleClass().add("center-rect");
        pane.setCenter(center);

        TextField textField = new TextField();
        textField.getStyleClass().setAll("text-field");
        Group right = new Group(textField);
        pane.setRight(right);

        pane.setBottom(infoLabel);
        pane.getStyleClass().add("main-pane");

        setTitle("TK Shifter Calibration software");
        getIcons().add(ImageService.getLogo());
        Scene scene = new Scene(pane, 800, 475);
        scene.getStylesheets().add("/MainPage.css");
        setScene(scene);
        setResizable(false);

//        setOnCloseRequest(windowEvent -> serialService.onClose(serialServiceListener));
    }

    private void showSerialEvent() {
        Platform.runLater(() -> new ReadCurrentDataErrorAlert(serialService, serialServiceListener, this).showAndWait());
    }

    private void updateValues(SerialPortValueEvent event) {
        mode = event.getMode();
        Platform.runLater(() -> infoLabel.setText(event.getMessage()));
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
