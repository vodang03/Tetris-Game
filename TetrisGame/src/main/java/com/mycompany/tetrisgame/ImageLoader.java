/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetrisgame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author B2110075-Võ Đặng-CT276_02
 */
public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File("data" + path));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
}
