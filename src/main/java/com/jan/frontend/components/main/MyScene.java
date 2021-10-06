package com.jan.frontend.components.main;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class MyScene extends Scene {
    public MyScene(Parent parent, double v, double v1, String stylePath) {
        super(parent, v, v1);

        if (stylePath != null)
            getStylesheets().add(stylePath);
    }
}
