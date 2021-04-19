package com.jan.frontend.stages.config;

import javafx.scene.image.ImageView;
import jssc.SerialPort;
import jssc.SerialPortException;
import com.jan.backend.ImageService;
import com.jan.backend.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.alerts.ShifterCalibratedAlert;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqConfigStage extends ConfigStage {

    public SeqConfigStage(SerialService serialService) {
        super(serialService, "/config/+.png");
    }

    @Override
    public void onClick(SerialService serialService, AtomicInteger gear, ImageView imageView) {
        try {
            if (gear.get() == -2) {
                serialService.writeString("setPlusGear");
                imageView.setImage(ImageService.getImage("/config/-.png"));
            }
            if (gear.get() == -1) {
                serialService.writeString("setMinusGear");
                gear.set(-2);
                close();
                new ShifterCalibratedAlert();
                imageView.setImage(ImageService.getImage("/config/+.png"));
            } else
                gear.getAndIncrement();
        } catch (SerialPortException e) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }
}
