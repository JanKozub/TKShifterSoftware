package com.jan.frontend.components.bordersConfig;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class NumberField extends TextField {

    public NumberField() {
        textProperty().addListener(this::changed);
        setAlignment(Pos.CENTER);
    }

    private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            setText(newValue.replaceAll("[^\\d]", ""));
        }

        if (newValue.length() > 4) {
            setText(newValue.substring(0,4));
        }
    }
}
