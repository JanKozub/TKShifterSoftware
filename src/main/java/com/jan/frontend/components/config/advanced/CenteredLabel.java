package com.jan.frontend.components.config.advanced;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class CenteredLabel extends Label {
    public CenteredLabel(String s) {
        super(s);
        setFont(Font.font(15));
        setTextAlignment(TextAlignment.CENTER);
        setAlignment(Pos.CENTER);
        setMinSize(320, 20);
    }
}
