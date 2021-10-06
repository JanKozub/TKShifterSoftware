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

        OffsetField borderField1 = new OffsetField(serialService, "1");
        OffsetField borderField2 = new OffsetField(serialService, "2");
        OffsetField borderField3 = new OffsetField(serialService, "3");
        OffsetField borderField4 = new OffsetField(serialService, "4");
//
//        BorderField borderFieldU = new BorderField(serialService, "U");
//        BorderField borderFieldL = new BorderField(serialService, "L");

        Button defaultButton = new Button("Set Default");
        defaultButton.getStyleClass().addAll("border-button", "border-default-button");

        getChildren().addAll(
                offsetLabel, background, borderField1, borderField2, borderField3, borderField4, defaultButton
        );
    }
}
