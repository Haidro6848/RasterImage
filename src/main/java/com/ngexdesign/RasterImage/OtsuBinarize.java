package com.ngexdesign.RasterImage;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
 
public class OtsuBinarize {
 
    private static BufferedImage original, grayscale, binarized;
 
    public static void main(String[] args) throws IOException {
 
    	String name = "LPimg_20";
        File original_f = new File(name+".jpg");
        original = ImageIO.read(original_f);
        grayscale = toGray(original);
        writeImage(name+"_gray", grayscale);  
        binarized = binarize(grayscale);
        writeImage(name+"_bin", binarized);           
    }
 
    private static void writeImage(String output, BufferedImage img) throws IOException {
        File file = new File(output+".jpg");
        ImageIO.write(img, "jpg", file);
    }
 
    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
 
    // The luminance method
    private static BufferedImage toGray(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage greyImage = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                int grey = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, grey, grey, grey);
 
                // Write pixels into image
                greyImage.setRGB(i, j, newPixel);
 
            }
        }
 
        return greyImage;
    }
 
    // Get binary treshold using Otsu's method
    private static int otsuTreshold(BufferedImage original) {
 
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
 
        return threshold;
 
    }
//    public static byte[][] binarizeImage(BufferedImage bfImage){
//        final int THRESHOLD = 160;
//        int height = bfImage.getHeight();
//        int width = bfImage.getWidth();
//        byte[][] image = new byte[width][height];
//
//        for(int i=0; i<width; i++){
//            for(int j=0; j<height; j++){
//                Color c = new Color(bfImage.getRGB(i,j));
//                int red = c.getRed();
//                int green = c.getGreen();
//                int blue = c.getBlue();
//                if(red<THRESHOLD && green<THRESHOLD && blue<THRESHOLD){
//                    image[i][j] = 1;
//                }else{
//                    image[i][j] = 0;
//                }
//            }
//        }
//        return image;
//    }
    public static void print(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1) {
                    System.out.print("1 ");
                } else if (matrix[i][j] == 0) {
                    System.out.print("  ");
                }
                else{
                	System.out.print("C ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    private static BufferedImage binarize(BufferedImage original) {
 
        int newPixel;
 
        int threshold = otsuTreshold(original);
 
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        int[][] image = new int[original.getHeight()][original.getWidth()];
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels
            	Color c = new Color(original.getRGB(i,j));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                int alpha = c.getAlpha();
                alpha = 255;
                if(red > threshold) {
                    newPixel = 255;
                    image[j][i] = 0;
                }
                else {
                    newPixel = 0;
                    image[j][i] = 1;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel); 
 
            }
        }
        print(image);
        return binarized;
    }
 
    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
}
