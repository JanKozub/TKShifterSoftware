package com.jan.frontend.components.main;

import com.jan.backend.ImageService;
import javafx.stage.Stage;

public class MyStage extends Stage {
    public MyStage(String title) {
        setTitle(title);
        getIcons().add(ImageService.getLogo());
        setResizable(false);
    }
}
