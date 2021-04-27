package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialPortErrorEvent;
import com.jan.backend.serial.SerialPortValueEvent;
import com.jan.backend.serial.SerialService;
import com.jan.backend.serial.SerialServiceListener;
import com.jan.frontend.components.alerts.ClosePortErrorAlert;
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
import javafx.stage.Stage;
import jssc.SerialPortException;

public class MainStage extends Stage {

    private final ImageView currentGearImage;
    private final Button configButton = new Button();
    private final Label infoLabel = new Label();
    private final SerialService serialService;

    public MainStage(SerialService serialService, int mode) {
        this.serialService = serialService;

        SerialServiceListener serialServiceListener = new SerialServiceListener() {
            @Override
            public void onSerialPortError(SerialPortErrorEvent event) {
                showSerialEvent();
            }

            @Override
            public void onValueUpdate(SerialPortValueEvent event) {
                updateValues(event.getData());
            }
        };
        serialService.addListener(serialServiceListener);

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

        Button advancedConfig = new AdvancedConfigButton();
        advancedConfig.setOnAction(e -> stage3.show());

        infoLabel.setLayoutY(455);
        infoLabel.setLayoutX(25);

        root.getChildren().addAll(new MyRadioGroup(serialService, configButton, mode), currentGearImage, configButton,
                new MemoryButton(serialService), advancedConfig, logo, infoLabel);

        setTitle("TK Shifter Calibration software");
        getIcons().add(ImageService.getLogo());
        setScene(new Scene(root, 500, 475));
        setResizable(false);

        setOnCloseRequest(windowEvent -> onClose(serialServiceListener));
    }

    private void showSerialEvent() {
        Platform.runLater(() -> new ReadCurrentDataErrorAlert().showAndWait());
    }

    private void updateValues(String[] data) {
        setCurrentGear(data[1]);

        Platform.runLater(() ->
                infoLabel.setText("mode= " + data[0] +
                        ", CG=" + data[1] +
                        ", X=" + data[2] +
                        ", Y=" + data[3] +
                        ", T=" + data[4] +
                        ", offset=[" + data[5] +
                        ", " + data[6] +
                        ", " + data[7] +
                        ", " + data[8] + "]" +
                        ", UCHP=" + data[9] +
                        ", LCHP=" + data[10]
                ));
    }

    private void setCurrentGear(String currentGear) {
        String imgUrl = null;
        if (isReadValid(currentGear)) {
            if (isInHShifterMode())
                if (currentGear.equals("0"))
                    imgUrl = "/config/N.png";
                else
                    imgUrl = "/config/" + currentGear + ".png";
            else if (currentGear.equals("0"))
                imgUrl = "/config/=.png";
            else
                imgUrl = "/config/" + currentGear + ".png";
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
        return !read.equals("") && read.length() == 1;
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

    private void onClose(SerialServiceListener serialServiceListener) {
        try {
            serialService.removeListener(serialServiceListener);
            serialService.onClose();
        } catch (SerialPortException ex) {
            new ClosePortErrorAlert().showAndWait();
        }
    }
}
