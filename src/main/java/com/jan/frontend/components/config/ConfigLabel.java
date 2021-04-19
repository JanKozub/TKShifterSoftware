package com.jan.frontend.components.config;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ConfigLabel extends Label {
    public ConfigLabel(String s) {
        setText(s);
        setFont(Font.font(20));
        setLayoutX(10);
        setAlignment(Pos.CENTER);
        setTextAlignment(TextAlignment.CENTER);
        setMinSize(200, 50);
        setLayoutY(35);
        setWrapText(true);
    }
}
