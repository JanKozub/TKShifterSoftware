package com.jan.frontend.components.mainStage;

import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import jssc.SerialPortException;

public class MyRadioGroup extends Group {
    private final SerialService serialService;

    public MyRadioGroup(SerialService serialService, int mode) {
        this.serialService = serialService;

        Label topSelectLabel = new Label("Select your top plate:");
        topSelectLabel.setFont(Font.font(15));

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rb1 = new MyRadioButton("H-pattern Shifter", toggleGroup, 30);
        RadioButton rb2 = new MyRadioButton("Sequential", toggleGroup, 60);

        if (mode == 1)
            rb1.setSelected(true);
        else
            rb2.setSelected(true);

        rb1.setOnAction(event -> onRb1Select());
        rb2.setOnAction(event -> onRb2Select());

        getChildren().addAll(topSelectLabel, rb1, rb2);
    }

    private void onRb2Select() {
        try {
            serialService.writeString("enableSeq");
        } catch (SerialPortException e) {
            new SendErrorAlert().showAndWait();
        }
    }

    private void onRb1Select() {
        try {
            serialService.writeString("enableDefault");
        } catch (SerialPortException e) {
            new SendErrorAlert();
        }
    }
}
