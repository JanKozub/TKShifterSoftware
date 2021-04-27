package com.jan.frontend.components.config.advanced;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class NumberField extends TextField {

    public NumberField() {
        textProperty().addListener(this::changed);
        setMinHeight(40);
        setFont(Font.font(15));
        setMaxWidth(100);
        setLayoutX(110);
    }

    private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            setText(newValue.replaceAll("[^\\d]", ""));
        }
    }
}
