package com.jan.frontend.components.config.advanced;

import javafx.scene.control.CheckBox;

public class InvCheckBox extends CheckBox {
    public InvCheckBox(String s, double layoutY) {
        setText(s);
        setScaleX(1.15);
        setScaleY(1.15);
        setLayoutY(layoutY);
    }
}
