package com.jan.frontend.components.mainStage;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

public class MyRadioButton extends RadioButton {
    public MyRadioButton(String s, ToggleGroup toggleGroup, int top) {
        setText(s);
        setToggleGroup(toggleGroup);
        setFont(Font.font(15));
        setLayoutY(top);
    }
}
