package com.jan.frontend.components.selectPortStage;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import com.jan.backend.ImageService;

public class RefreshButton extends Button {
    public RefreshButton() {
        ImageView imageView = new ImageView(ImageService.getImage("/icons/refresh20x20.png"));
        setGraphic(imageView);
        setMinWidth(25);
        setLayoutY(20);
        setLayoutX(139);
    }
}
