package com.jan.frontend.stages.config;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.main.MyScene;
import com.jan.frontend.components.main.MyStage;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConfigStage extends MyStage {

    private final AtomicInteger gear = new AtomicInteger(-2);

    public ConfigStage(SerialService serialService, String initialUrl) {
        super("Calibration");
        Group root = new Group();

        Label label = new Label("Hold lever in position\nshown in the picture\nand click next");
        label.getStyleClass().add("config-label");

        ImageView imageView = new ImageView(ImageService.getImage(initialUrl));
        imageView.setLayoutX(200);

        Button nextButton = new Button("Next");
        nextButton.getStyleClass().add("next-button");
        nextButton.setOnAction(e -> onClick(serialService, gear, imageView));

        root.getChildren().addAll(label, nextButton, imageView);

        setScene(new MyScene(root, 400, 200, "/ConfigStage.css"));
        initModality(Modality.WINDOW_MODAL);

        setOnCloseRequest(windowEvent -> {
            gear.set(-2);
            imageView.setImage(ImageService.getImage(initialUrl));
        });
    }

    public abstract void onClick(SerialService serialService, AtomicInteger gear, ImageView imageView);
}
