package com.jan.frontend.components.mainStage;

import com.jan.backend.I18N;
import com.jan.backend.ImageService;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Locale;

public class FlagsGroup extends Group {

    public FlagsGroup() {
        ImageView plFlag = new ImageView(ImageService.getImage("/images/pl_flag.png"));
        plFlag.getStyleClass().add("flag");
        plFlag.setLayoutX(20);
        plFlag.setLayoutY(10);
        plFlag.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            I18N.setLocale(Locale.GERMAN);
            event.consume();
        });

        ImageView enFlag = new ImageView(ImageService.getImage("/images/en_flag.png"));
        enFlag.getStyleClass().add("flag");
        enFlag.setLayoutX(108);
        enFlag.setLayoutY(10);
        enFlag.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            I18N.setLocale(Locale.ENGLISH);
            event.consume();
        });

        Rectangle background = new Rectangle(180, 68, Paint.valueOf("#111C29"));
        background.getStyleClass().add("flags-group");

        setLayoutY(215);

        getChildren().addAll(background, plFlag, enFlag);
    }
}
