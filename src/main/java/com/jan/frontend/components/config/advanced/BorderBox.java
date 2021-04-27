package com.jan.frontend.components.config.advanced;

import javafx.scene.control.ComboBox;

public class BorderBox extends ComboBox<String> {
    public BorderBox() {
        getItems().setAll("L", "U");
        setValue("L");
        setMinHeight(40);
        setMinWidth(60);
        setLayoutX(40);
    }
}
