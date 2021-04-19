package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.SerialService;
import com.jan.frontend.components.alerts.ClosePortErrorAlert;
import com.jan.frontend.components.alerts.ReadCurrentGearErrorAlert;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

@SuppressWarnings("BusyWait")
public class MainStage extends Stage {

    private final ImageView currentGearImage;
    private final Button configButton;
    private final Label infoLabel;
    private final SerialService serialService;

    public MainStage(SerialService serialService, int mode) {
        this.serialService = serialService;

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

        configButton = new Button();
        configButton.setMinSize(210, 75);
        configButton.setLayoutX(20);
        configButton.setLayoutY(115);
        configButton.setOnAction(event -> onConfigButtonClick(stage1, stage2));

        Button advancedConfig = new Button("Advanced Config\n(do not touch)");
        advancedConfig.setTextAlignment(TextAlignment.CENTER);
        advancedConfig.setMinSize(210, 75);
        advancedConfig.setLayoutX(20);
        advancedConfig.setLayoutY(285);
        advancedConfig.setOnAction(e -> new AdvancedConfigStage(serialService).show());

        infoLabel = new Label();
        infoLabel.setLayoutY(455);
        infoLabel.setLayoutX(150);

        root.getChildren().addAll(new MyRadioGroup(serialService, configButton, mode), currentGearImage, configButton,
                new MemoryButton(serialService), advancedConfig, logo, infoLabel);

        setTitle("TK Shifter Calibration software");
        getIcons().add(ImageService.getImage("/icons/logo32x32.png"));
        setScene(new Scene(root, 500, 475));
        setResizable(false);

        Thread thread = new Thread(this::getDataFromShifter);
        thread.start();

        setOnCloseRequest(windowEvent -> onClose(thread));
    }

    private void getDataFromShifter() {
        try {
            while (serialService.getSerialPort().isOpened() && !Thread.currentThread().isInterrupted()) {
                String[] data = serialService.getDataFromShifter();

                if (data != null) {
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
                                    ", " + data[8] + "]"
                            ));
                }
                Thread.sleep(10);
            }
        } catch (SerialPortException | SerialPortTimeoutException e) {
            Platform.runLater(() -> {
                if (serialService.getSerialPort().isOpened())
                    new ReadCurrentGearErrorAlert().showAndWait();
                this.close();
            });

        } catch (InterruptedException ignored) {

        }
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

    private void onClose(Thread thread) {
        try {
            serialService.getSerialPort().closePort();
            thread.interrupt();
        } catch (SerialPortException e) {
            new ClosePortErrorAlert();
        }
    }
}
