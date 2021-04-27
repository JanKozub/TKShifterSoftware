package com.jan.frontend.components.mainStage;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

public class AdvancedConfigButton extends Button {
    public AdvancedConfigButton() {
        setText("Advanced Config\n(edit on your own risk)");
        setTextAlignment(TextAlignment.CENTER);
        setMinSize(210, 75);
        setLayoutX(20);
        setLayoutY(285);
    }
}
