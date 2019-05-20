package com.feihua.utils.graphic;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TestImageUtils {


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
		//String text[]  = new  String[]{"r","t","g","j","d","n"};
		//BufferedImage image = ImageUtils.createSimpleCaptchaImage(100,30,Color.white,20,text,10);
		//BufferedImage image = ImageUtils.createQrCode(500,"165316513213","utf-8",0,Color.white,Color.black);
		//ImageUtils.outPutImage(image, "d://CaptchaImage.jpg");
		//ImageUtils.outPutImage(image, "d://qrcode.jpg");
		//System.out.println(ImageUtils.readQrCode(image,"utf-8"));

		// 图片质量压缩
		//String intpuPath = "d:/84aa7c08-5bf8-4d40-8748-0480291ca0f2.jpg";
		//String intpuPath = "d:/C67233EA-1577-41A1-B524-E7DF792207FA.jpeg";
		//String intpuPath = "d:/21e7c24a-af8d-4712-8cbe-1a2d465657a8.gif";
		String intpuPath = "d:/84aa7c08-5bf8-4d40-8748-0480291ca0f2.bmp";

		String formatName = "jpg";
		BufferedImage image = ImageUtils.createImage(intpuPath);
		ByteArrayInputStream byteArrayInputStream = ImageUtils.compressImage(image,formatName, (float) 0.25);
		image = ImageUtils.inputStreamToBufferedImage(byteArrayInputStream);
		//image = compress(image, (float) 0.3);
		//ImageUtils.outPutImage(image, "d://test.jpg");
		ImageUtils.outPutImage(image,formatName, "d://test.jpg");

	}

	public void testQrCode() throws IOException, WriterException {

		BufferedImage bg = ImageUtils.createImage("C:\\Users\\Lenovo\\Desktop\\111.jpg");
		BufferedImage logo = ImageUtils.createImage("C:\\Users\\Lenovo\\Desktop\\logo.png");
		logo = ImageUtils.zoomEqualRatioImageByWidth(logo,50);
		BufferedImage qrcode = ImageUtils.createQrCodeWithLogo(150,
				"http://api.51match.cn/uni-app/#/pages/detail/detail?wwdUserId=xxxxxxxx",
				"gbk",1,
				Color.white, Color.BLACK, logo);

		int offset = 20;
		qrcode = ImageUtils.cutImage(qrcode,offset,offset,qrcode.getWidth()-offset,qrcode.getHeight()-offset);
		String formatName = "png";

		int margin = 30;
		BufferedImage r = ImageUtils.pressImage(bg,qrcode,bg.getWidth() - qrcode.getWidth() - margin,bg.getHeight() - qrcode.getHeight() - margin,1.0f);
		ImageUtils.outPutImage(r,formatName, "d://test.png");

		ImageUtils.outPutImage(qrcode,formatName, "d://qrcode.png");
		ImageUtils.outPutImage(logo,formatName, "d://logo.png");
	}
	@Test
	public void testZoom() throws IOException {
		BufferedImage logo = ImageUtils.createImage("C:\\Users\\Lenovo\\Desktop\\logo.png");

		logo = ImageUtils.zoomEqualRatioImageByWidth(logo,150);
		String formatName = "png";
		ImageUtils.outPutImage(logo,formatName, "d://logo.png");
	}
}
