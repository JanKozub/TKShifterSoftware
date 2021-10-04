package com.jan.frontend.components.bordersConfig;

import com.jan.backend.serial.SerialService;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class BordersConfig extends Group {
    public BordersConfig(SerialService serialService) {
        Region background = new Region();
        background.getStyleClass().add("borders-background");

        Label offsetLabel = new Label("Offset:");
        offsetLabel.getStyleClass().add("offset-label");
        Label neutralLabel = new Label("Neutral Borders:");
        neutralLabel.getStyleClass().add("neutral-label");

        BorderField borderField1 = new BorderField(serialService, "1");
        BorderField borderField2 = new BorderField(serialService, "2");
        BorderField borderField3 = new BorderField(serialService, "3");
        BorderField borderField4 = new BorderField(serialService, "4");

        BorderField borderFieldU = new BorderField(serialService, "U");
        BorderField borderFieldL = new BorderField(serialService, "L");

        Button defaultButton = new Button("Set Default");
        defaultButton.getStyleClass().addAll("border-button", "border-default-button");

        getChildren().addAll(
                offsetLabel, background, borderField1, borderField2, borderField3,
                neutralLabel, borderFieldU, borderFieldL, borderField4, defaultButton
        );
    }
}
