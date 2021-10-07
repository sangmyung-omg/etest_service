package com.tmax.eTest.TestStudio.controller.component;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ConvertToDarkImageComponentTs {
    
	/* 생성: 이진형 연구원
	 * 수정: 21.07.19 임창언 연구원 changeonlim@tmax.co.kr
	 * 수정사항: imgFile 경로에 dark_+imageName 생성(위치지정),
	 *        성공/실패 output 리턴
	 */
	/*
    public static Boolean convertImage(String dirPath, String imageName) {

        BufferedImage inputFile = null;
        try {
            inputFile = ImageIO.read(new File(dirPath+File.separator+imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int H = inputFile.getHeight();
        int W = inputFile.getWidth();
        int red, green, blue;

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Color col = new Color(inputFile.getRGB(x, y), true);
                red = col.getRed();
                green = col.getGreen();
                blue = col.getBlue();
                inputFile.setRGB(x, y, new Color(255-red*231/255, 255-green*231/255, 255-blue*231/255).getRGB());
            }
        }

        try {
            File outputFile = new File(dirPath+File.separator+"dark_"+imageName);
            new PngEncoder().withBufferedImage(inputFile).toFile(outputFile); 	// ImageIO.write(inputFile, "png", outputFile); 대체
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }

    }
    */
}
