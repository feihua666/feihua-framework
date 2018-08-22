package com.feihua.utils.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.Test;

public class TestImageUtils {

	@Test
	public void simpleTest() throws IOException, WriterException, NotFoundException {
		
		//String intpuPath = "d:/oa/";
		//String intpuPath = "d:/金菱/";
		//String outputPath = "d:\\image\\";
		
		//BufferedImage image = ImageUtils.createImage(20,10, Color.white);
		//BufferedImage[][] images = ImageUtils.cutImageByRC(image, 5, 6);
		//BufferedImage cutImage = ImageUtils.cutImage(image, 10, 10, image.getWidth()-10, image.getHeight()-10);
		//ImageUtils.outPutImage(ImageUtils.rotateImage(image,90), outputPath+"text.jpg");
		//String text[]  = new  String[]{"我","是","验","证","码","呀"};
		//String text[]  = new  String[]{"我","是","验"};
		//String text[]  = new  String[]{"5","+","6","=","?"};
		String text[]  = new  String[]{"r","t","g","j","d","n"};
		//BufferedImage image = ImageUtils.createSimpleCaptchaImage(100,30,Color.white,20,text,10);
		BufferedImage image = ImageUtils.createQrCode(500,"165316513213","utf-8",0,Color.white,Color.black);
		//ImageUtils.outPutImage(image, "d://CaptchaImage.jpg");
		ImageUtils.outPutImage(image, "d://qrcode.jpg");
		System.out.println(ImageUtils.readQrCode(image,"utf-8"));
	}
}
