package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialPortErrorEvent;
import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.ReadCurrentDataErrorAlert;
import com.jan.frontend.components.mainStage.AdvancedConfigButton;
import com.jan.frontend.components.mainStage.MemoryButton;
import com.jan.frontend.components.mainStage.MyRadioGroup;
import com.jan.frontend.stages.config.AdvancedConfigStage;
import com.jan.frontend.stages.config.HConfigStage;
import com.jan.frontend.stages.config.SeqConfigStage;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainStage extends Stage {

    private final ImageView currentGearImage;
    private final Button configButton = new Button();
    private final Label infoLabel = new Label();
//    private final SerialService serialService;
//    private final SerialServiceListener serialServiceListener;

    public MainStage(SerialService serialService, int mode) {
//        this.serialService = serialService;
//
//        SerialServiceListener serialServiceListener = new SerialServiceListener() {
//            @Override
//            public void onSerialPortError(SerialPortErrorEvent event) {
//                showSerialEvent();
//            }
//
//            @Override
//            public void onValueUpdate(SerialPortValueEvent event) {
//                updateValues(event);
//            }
//        };
//        serialService.addListener(serialServiceListener);
//        this.serialServiceListener = serialServiceListener;

        Group root = new Group();

        currentGearImage = new ImageView(ImageService.getImage("/config/N.png"));
        currentGearImage.setLayoutX(275);
        currentGearImage.setLayoutY(100);

        ImageView logo = new ImageView(ImageService.getImage("/icons/logo100x60.png"));
        logo.setLayoutY(375);
        logo.setLayoutX(75);

        Stage stage1 = new HConfigStage(serialService);
        stage1.initOwner(this);
        Stage stage2 = new SeqConfigStage(serialService);
        stage2.initOwner(this);
        Stage stage3 = new AdvancedConfigStage(serialService);
        stage3.initOwner(this);

        configButton.setMinSize(210, 75);
        configButton.setLayoutX(20);
        configButton.setLayoutY(115);
        configButton.setOnAction(event -> onConfigButtonClick(stage1, stage2));

        infoLabel.setLayoutY(455);
        infoLabel.setLayoutX(25);

//        root.getChildren().addAll(new MyRadioGroup(serialService, configButton, mode), currentGearImage, configButton,
//                new MemoryButton(serialService), logo, infoLabel);

        BorderPane pane = new BorderPane();
        Group left = new Group(new MyRadioGroup(serialService, configButton, mode), configButton);
        left.setLayoutX(20);
        left.setLayoutY(20);
        pane.setLeft(left);
        pane.setCenter(currentGearImage);
        pane.setRight(new Rectangle(180, 475, Paint.valueOf("red")));
        pane.getStyleClass().add("main-pane");

        setTitle("TK Shifter Calibration software");
        getIcons().add(ImageService.getLogo());
        Scene scene = new Scene(pane, 800, 475);
        scene.getStylesheets().add("/MainPage.css");
        setScene(scene);
        setResizable(false);

//        setOnCloseRequest(windowEvent -> serialService.onClose(serialServiceListener));
    }

    private void showSerialEvent() {
//        Platform.runLater(() -> new ReadCurrentDataErrorAlert(serialService, serialServiceListener, this).showAndWait());
    }

    private void updateValues(SerialPortValueEvent event) {
        setCurrentGear(event.getCurrentGear());
        Platform.runLater(() -> infoLabel.setText(event.getMessage()));
    }

    private void setCurrentGear(String currentGear) {
        String imgUrl = null;
        if (isReadValid(currentGear)) {
            if (isInHShifterMode()) {
                if (currentGear.equals("0")) {
                    imgUrl = "/config/N.png";
                } else {
                    imgUrl = "/config/" + currentGear + ".png";
                }
            } else if (currentGear.equals("0")) {
                imgUrl = "/config/=.png";
            } else {
                if (currentGear.equals("9"))
                    imgUrl = "/config/+.png";
                else
                    imgUrl = "/config/-.png";
            }
        }

        if (imgUrl != null) {
            String finalImgUrl = imgUrl;
            Platform.runLater(() -> setCurrentGearImage(finalImgUrl));
        }
    }

    private void setCurrentGearImage(String url) {
        currentGearImage.setImage(ImageService.getImage(url));
    }

    private boolean isReadValid(String read) {
        return !read.equals("") && read.length() <= 2;
    }

    private boolean isInHShifterMode() {
        return configButton.getText().equals("H-pattern Shifter calibration");
    }

    private void onConfigButtonClick(Stage stage1, Stage stage2) {
        if (isInHShifterMode()) {
            stage1.show();
        } else
            stage2.show();
    }
}
