package com.jan.backend;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageService {

    public static Image getImage(String url) {
        Image image;
        try {
            InputStream is = ImageService.class.getResourceAsStream(url);
            image = new Image(is);
        } catch (NullPointerException e) {
            System.out.println("Resource not found! " + url);
            return null;
        }
        return image;
    }
}
