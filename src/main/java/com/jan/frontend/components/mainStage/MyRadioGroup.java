package com.jan.frontend.components.mainStage;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import jssc.SerialPortException;
import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;

public class MyRadioGroup extends Group {
    private final SerialService serialService;
    private final Button button;

    public MyRadioGroup(SerialService serialService, Button button, int mode) {
        this.serialService = serialService;
        this.button = button;

        Label topSelectLabel = new Label("Select your top plate:");
        topSelectLabel.setFont(Font.font(15));
        topSelectLabel.setLayoutY(20);

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rb1 = new MyRadioButton("H-pattern Shifter", toggleGroup, 50);
        RadioButton rb2 = new MyRadioButton("Sequential", toggleGroup, 80);

        if (mode == 1) {
            rb1.setSelected(true);
            button.setText("H-pattern Shifter calibration");
        } else {
            rb2.setSelected(true);
            button.setText("Sequential calibration");
        }

        rb1.setOnAction(event -> onRb1Select());
        rb2.setOnAction(event -> onRb2Select());

        getChildren().addAll(topSelectLabel, rb1, rb2);
        setLayoutX(20);
    }

    private void onRb2Select() {
        try {
            serialService.writeString("enableSeq");
            button.setText("Sequential calibration");
        } catch (SerialPortException e) {
            new SendErrorAlert().showAndWait();
        }
    }

    private void onRb1Select() {
        try {
            serialService.writeString("enableDefault");
            button.setText("H-pattern Shifter calibration");
        } catch (SerialPortException e) {
            new SendErrorAlert();
        }
    }
}
