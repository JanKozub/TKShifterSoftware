package com.jan.backend;

import javafx.scene.image.Image;
import java.io.InputStream;

public class ImageService {

    public static Image getImage(String url) {
        Image image;
        try {
            InputStream is = ImageService.class.getResourceAsStream(url);
            assert is != null;
            image = new Image(is);
        } catch (NullPointerException e) {
            System.out.println("Resource not found! " + url);
            return null;
        }
        return image;
    }

    public static Image getLogo() {
        return getImage("/icons/logo32x32.png");
    }
}
