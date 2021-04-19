package com.jan.frontend.stages.config;

import com.jan.backend.SerialService;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jssc.SerialPort;
import com.jan.backend.ImageService;
import com.jan.frontend.components.config.ConfigLabel;
import com.jan.frontend.components.config.NextButton;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConfigStage extends Stage {

    private final AtomicInteger gear = new AtomicInteger(-2);

    public ConfigStage(SerialService serialService, String initialUrl) {
        Group root = new Group();

        Label label = new ConfigLabel("Hold lever in position\nshown in the picture\nand click next");

        ImageView imageView = new ImageView(ImageService.getImage(initialUrl));
        imageView.setLayoutX(200);

        Button nextButton = new NextButton();
        nextButton.setOnAction(e -> onClick(serialService, gear, imageView));

        root.getChildren().addAll(label, nextButton, imageView);

        var scene = new Scene(root, 400, 200);

        setTitle("Calibration");
        getIcons().add(ImageService.getImage("/icons/logo32x32.png"));
        setScene(scene);
        setResizable(false);
        initModality(Modality.WINDOW_MODAL);

        setOnCloseRequest(windowEvent -> {
            gear.set(-2);
            imageView.setImage(ImageService.getImage(initialUrl));
        });
    }

    public abstract void onClick(SerialService serialService, AtomicInteger gear, ImageView imageView);
}
