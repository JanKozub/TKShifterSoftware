package com.jan.frontend.components.config.advanced;

import javafx.scene.control.ComboBox;

public class AddressBox extends ComboBox<Integer> {
    public AddressBox() {
        getItems().setAll(1, 2, 3, 4);
        setValue(1);
        setMinHeight(40);
        setMinWidth(50);
        setLayoutX(20);
        setLayoutY(15);
    }
}
