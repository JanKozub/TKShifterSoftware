package com.jan.frontend.components.config.advanced;

import javafx.scene.control.ComboBox;

public class AddressBox extends ComboBox<Integer> {
    public AddressBox() {
        getItems().setAll(1, 2, 3, 4);
        setValue(1);
        setMinHeight(40);
        setMinWidth(60);
        setLayoutX(40);
    }
}
