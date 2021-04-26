package com.jan.frontend.stages.config;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.SendErrorAlert;
import com.jan.frontend.components.alerts.ShifterCalibratedAlert;
import javafx.scene.image.ImageView;
import jssc.SerialPortException;

import java.util.concurrent.atomic.AtomicInteger;

public class HConfigStage extends ConfigStage {
    public HConfigStage(SerialService serialService) {
        super(serialService, "/config/UCHP.png");
    }

    @Override
    public void onClick(SerialService serialService, AtomicInteger gear, ImageView imageView) {
        try {
            if (gear.get() == 7) {
                serialService.writeString("8");
                close();
                new ShifterCalibratedAlert().showAndWait();
                gear.set(-2);
                imageView.setImage(ImageService.getImage("/config/UCHP.png"));
            } else {
                String url = String.valueOf(gear.get() + 2);
                if (gear.get() == -2) {
                    serialService.writeString("setUCHP");
                    url = "LCHP";
                } else if (gear.get() == -1) {
                    serialService.writeString("setLCHP");
                } else {
                    serialService.writeString(String.valueOf(gear.get() + 1));
                }
                ClassLoader.getSystemClassLoader().getResource("");
                imageView.setImage(ImageService.getImage("/config/" + url + ".png"));
                gear.getAndIncrement();
            }
        } catch (SerialPortException e) {
            new SendErrorAlert().showAndWait();
            this.close();
        }
    }
}
