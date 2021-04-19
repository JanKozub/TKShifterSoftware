package com.jan.frontend.components.selectPortStage;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

public class StartButton extends Button {
    public StartButton() {
        setText("Start");
        setMinSize(150, 20);
        setLayoutX(25);
        setLayoutY(50);
        setTextAlignment(TextAlignment.CENTER);
    }
}
