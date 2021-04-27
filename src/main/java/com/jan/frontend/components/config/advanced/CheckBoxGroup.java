package com.jan.frontend.components.config.advanced;

import javafx.scene.Group;

public class CheckBoxGroup extends Group {
    public CheckBoxGroup() {
        InvCheckBox invX = new InvCheckBox("X Inverted", 10);
        InvCheckBox invY = new InvCheckBox("Y Inverted", 40);
        InvCheckBox invT = new InvCheckBox("T Inverted", 70);
        getChildren().setAll(invX, invY, invT);
        setLayoutY(175);
        setLayoutX(122);
    }
}
