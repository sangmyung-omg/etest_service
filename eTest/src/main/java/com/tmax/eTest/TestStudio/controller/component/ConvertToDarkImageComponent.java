//package com.tmax.eTest.TestStudio.controller.component;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import studio.psce.api.component.pngencoder.PngEncoder;
//
//public class ConvertToDarkImageComponent {
//    
//	/* 생성: 이진형 연구원
//	 * 수정: 21.07.19 임창언 연구원 changeonlim@tmax.co.kr
//	 * 수정사항: imgFile 경로에 dark_+imageName 생성(위치지정),
//	 *        성공/실패 output 리턴
//	 */
//    public static Boolean convertImage(String dirPath, String imageName) {
//
//        BufferedImage inputFile = null;
//        try {
//            inputFile = ImageIO.read(new File(dirPath+File.separator+imageName));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int[][] colorfrom = {{255,255,255},{0,0,0},{34,30,31},{35,24,21},{35,31,32},{204,204,204},{229,229,229},{237,237,237},{231,243,241},{220,238,235},{208,232,228},
//        		{184,220,215},{161,208,201},{253,234,237},{253,224,229},{252,214,220},{250,193,202},{249,173,185},{254,247,229},{254,244,217},{253,239,204},{253,240,204},
//        		{253,232,178},{252,225,153},{240,49,79},{51,51,51},{204,235,224},{208,231,228},{161,207,201},{248,173,185},{251,224,153}};
//        int[] colorto   = {24,255,255,255,255,204,70,70,100,70,70,38,38,100,70,70,38,38,100,70,70,70,38,38,255,255,70,70,38,38,38};
//        int length = colorfrom.length;
//        
//        for (int x = 0; x < inputFile.getWidth(); x++) {
//            for (int y = 0; y < inputFile.getHeight(); y++) {
//                Color col = new Color(inputFile.getRGB(x, y), true);
//                int red = col.getRed();
//                int green = col.getGreen();
//                int blue = col.getBlue();
//                int i = 0;
//                for( ; i < length ; i++) {
//                	if(red == colorfrom[i][0] && green == colorfrom[i][1] && blue == colorfrom[i][2]) {
//                		col = new Color(colorto[i], colorto[i], colorto[i]);
//                		break;
//                	}
//                }
//                int average = 255 - ( red + green + blue ) / 3;
//                if(i == length) col = new Color(average, average, average);
//                inputFile.setRGB(x, y, col.getRGB());
//            }
//        }
//
//        try {
//            File outputFile = new File(dirPath+File.separator+"dark_"+imageName);
//            new PngEncoder().withBufferedImage(inputFile).toFile(outputFile); 	// ImageIO.write(inputFile, "png", outputFile); 대체
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//}