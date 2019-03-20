package com.feihua.utils.graphic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.*;

import com.feihua.utils.string.StringUtils;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.RandomUtils;

public class ImageUtils {

	public static String IMAGE_TYPE_GIF = "gif"; // 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg"; // 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg"; // 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp"; // 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png"; // 可移植网络图形

	/**
	 * 创建图片
	 * @return
	 */
	public final static BufferedImage createImage(int width,int height){
		BufferedImage image = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		return image;
	}
	/**
	 * 创建带背景颜色图片
	 * @return
	 */
	public final static BufferedImage createImage(int width,int height,Color bgColor){
		BufferedImage image = createImage(width,height);
		Graphics2D g = image.createGraphics();
		g.setColor(bgColor);
		//g.drawRect(0, 0, image.getWidth(), image.getHeight());
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.dispose();
		return image;
	}
	
	/**
	 * 根据图片路径读取图片
	 * @param imagePath
	 * @return
	 * @throws IOException
	 */
	public final static BufferedImage createImage(String imagePath) throws IOException{
		return createImage (new File(imagePath)); // 读入文件
	}
	/**
	 * 根据文件读取图片
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	public final static BufferedImage createImage(File imageFile) throws IOException{
		BufferedImage image = ImageIO.read(imageFile); // 读入文件
		return image;
	}
	public final static BufferedImage createImage(InputStream inputStream) throws IOException{
		BufferedImage image = ImageIO.read(inputStream); // 读入流
		return image;
	}
	/**
	 * 缓冲图片对象转像对象
	 * @param image
	 * @return
	 */
	public final static Image BufferedImageToImage(BufferedImage image){
		return image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT);
	}
	/**
	 * 按比例缩放
	 * @param image 图像
	 * @param scale 比例，如果缩小请用小数
	 */
	public final static BufferedImage zoomImage(BufferedImage image,double scale) {
			int width = (int) (image.getWidth() * scale); // 得到源图宽
			int height = (int) (image.getHeight() * scale); // 得到源图长
			//缩放后的图像
			Image scaleImage = image.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			//创建一个新图像
			BufferedImage resultImage = createImage(width, height);
			Graphics g = resultImage.getGraphics();
			g.drawImage(scaleImage, 0, 0, null); // 绘制缩放后的图
			g.dispose();
			return resultImage;
	}
	/**
	 * 按高度和宽度缩放
	 * @param image
	 * @param width
	 * @return 
	 */
	public final static BufferedImage zoomEqualRatioImageByWidth(BufferedImage image, int width) {
			double ratio = (new Integer(width)).doubleValue()/image.getWidth();

			return zoomImage(image,ratio);
	}
	/**
	 * 按高度和宽度缩放
	 * @param image
	 * @param height
	 * @return
	 */
	public final static BufferedImage zoomEqualRatioImageByHeight(BufferedImage image,int height) {
		double ratio = (new Integer(height)).doubleValue()/ image.getHeight();

		return zoomImage(image,ratio);
	}
	/**
	 * 旋转图像
	 * @param bufferedimage
	 * @param degree
	 * @return
	 */
	public static BufferedImage rotateImage(BufferedImage bufferedimage,int degree) {
		// 得到图片宽度。
		int w = bufferedimage.getWidth();
		// 得到图片高度。
		int h = bufferedimage.getHeight();
		
		// 空的图片。
		BufferedImage img = createImage(w, h);
		// 空的画笔。
		Graphics2D graphics2d = img.createGraphics();
		// 旋转，degree是整型，度数，比如垂直90度。
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
		// 从bufferedimagecopy图片至img，0,0是img的坐标。
		graphics2d.drawImage(bufferedimage, 0, 0, null);
		graphics2d.dispose();
		// 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
		return img;
	}
	/**
	 * 水平翻转
	 * @param bufferedimage
	 * @return
	 */
	public static BufferedImage flipImageH(BufferedImage bufferedimage) {
		// 得到宽度。
		int w = bufferedimage.getWidth();
		// 得到高度。
		int h = bufferedimage.getHeight();
		return cutImage(bufferedimage,w, 0, 0, h);
	}
	/**
	 * 垂直翻转
	 * @param bufferedimage
	 * @return
	 */
	public static BufferedImage flipImageV(BufferedImage bufferedimage) {
		// 得到宽度。
		int w = bufferedimage.getWidth();
		// 得到高度。
		int h = bufferedimage.getHeight();
		return cutImage(bufferedimage,0, h, w, 0);
	}
	/**
	 * 按指定起点坐标和宽高切割
	 * @param bufferedimage
	 * @param dx
	 * @param dy
	 * @param width
	 * @param height
	 */
	public final static BufferedImage cutImage(BufferedImage bufferedimage, int dx,
			int dy, int width, int height) {
			int srcWidth = bufferedimage.getWidth(); // 源图宽度
			int srcHeight = bufferedimage.getHeight(); // 源图高度
			BufferedImage resultImage = null;
			int dwidth = Math.abs(width-dx);
			int dheight = Math.abs(height-dy);
					
			if (srcWidth > 0 && srcHeight > 0) {
				resultImage = createImage(width, height);
				Graphics g = resultImage.getGraphics();
				g.drawImage(bufferedimage, 0, 0, width,height ,dx,dy,width,height, null); // 绘制切割后的图
				g.dispose();
			}

			return resultImage;
	}
	/**
	 * 指定切片的行数和列数
	 * @param bufferedimage
	 * @param rows
	 * @param cols
	 */
	public final static BufferedImage[][] cutImageByRC(BufferedImage bufferedimage, int rows, int cols) {
		BufferedImage[][] result = new BufferedImage[rows][cols];
			// 读取源图像
			int srcWidth = bufferedimage.getWidth(); // 源图宽度
			int srcHeight = bufferedimage.getHeight(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % cols == 0) {
					destWidth = srcWidth / cols;
				} else {
					destWidth = (int) Math.floor(srcWidth / cols) + 1;
				}
				if (srcHeight % rows == 0) {
					destHeight = srcHeight / rows;
				} else {
					destHeight = (int) Math.floor(srcHeight / rows) + 1;
				}
				// 循环建立切片
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						int sx = j * destWidth;
						int sy = i* destHeight;
						BufferedImage resultImage = cutImage(bufferedimage,sx, sy,sx + destWidth,sy + destHeight);
						result[i][j] = resultImage;
					}
				}
			}
			return result;
	}
	/**
	 * 指定切片的宽度和高度
	 * @param bufferedimage
	 * @param width
	 * @param height
	 */
	public final static BufferedImage[][] cutImageByHW(BufferedImage bufferedimage, int width, int height) {
			// 读取源图像
			int srcWidth = bufferedimage.getWidth(); // 源图宽度
			int srcHeight = bufferedimage.getHeight(); // 源图高度
			int cols = 0; // 切片横向数量
			int rows = 0; // 切片纵向数量
			if (srcWidth > width && srcHeight > height) {
				// 计算切片的横向和纵向数量
				if (srcWidth % width == 0) {
					cols = srcWidth / width;
				} else {
					cols = (int) Math.floor(srcWidth / width) + 1;
				}
				if (srcHeight % height == 0) {
					rows = srcHeight / height;
				} else {
					rows = (int) Math.floor(srcHeight / height) + 1;
				}
				
			}
			return cutImageByRC(bufferedimage,rows,cols);
	}
	/**
	 * 变黑白
	 * @param bufferedimage
	 */

	public final static BufferedImage gray(BufferedImage bufferedimage) {
			BufferedImage src = bufferedimage;
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			return src;
	}
	/**
	 * 给图片添加文字水印
	 * @param bufferedimage
	 * @param pressText
	 * @param fontName
	 * @param fontStyle
	 * @param color
	 * @param fontSize
	 * @param x
	 * @param y
	 * @param alpha
	 * @return
	 */
	public final static BufferedImage pressText(BufferedImage bufferedimage,String pressText,
			String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) {
		
			BufferedImage image = bufferedimage;
			Graphics2D g = image.createGraphics();
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText,  x, y);
			g.dispose();
			return image;
	}
	/**
	 * 给图片添加图片水印
	 * @param bufferedimage
	 * @param pressImg
	 * @param x
	 * @param y
	 * @param alpha
	 * @return
	 */
	public final static BufferedImage pressImage(BufferedImage bufferedimage,Image pressImg,  int x, int y, float alpha) {
			int width = bufferedimage.getWidth();
			int height = bufferedimage.getHeight();
			Graphics2D g = bufferedimage.createGraphics();
			// 水印文件
			int width_biao = pressImg.getWidth(null);
			int height_biao = pressImg.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			g.drawImage(pressImg, x,y,null);
			// 水印文件结束
			g.dispose();
			return bufferedimage;
	}
	/**
	 * 输出图像文件
	 * @param bufferedimage
	 * @param targetPath
	 * @throws IOException
	 */
	public final static void outPutImage(BufferedImage bufferedimage ,String formatName,String targetPath) throws IOException{
		String _formatName = formatName;
		if(_formatName == null) {
			_formatName = IMAGE_TYPE_JPEG;
		}
		 ImageIO.write(bufferedimage,  _formatName, new File(targetPath));
	}

	/**
	 *
	 * @param byteArrayInputStream
	 * @return
	 * @throws IOException
	 */
	public final static BufferedImage inputStreamToBufferedImage(ByteArrayInputStream byteArrayInputStream) throws IOException {
		return ImageIO.read(byteArrayInputStream);
	}

	/**
	 *
	 * @param bufferedImage
	 * @return
	 * @throws IOException
	 */
	public final static InputStream bufferedImageToInputStream(BufferedImage bufferedImage,String formatName) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, formatName, os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}
	/**
	 * 图片质量压缩
	 * @param bufferedImage
	 * @param quality
	 * @param formatName 注意这个参数现在不支持png
	 * @return
	 */
	public final static ByteArrayInputStream compressImage(BufferedImage bufferedImage, String formatName, float quality) throws IOException {
		String _formatName = formatName;
		if(_formatName == null) {
			 _formatName = IMAGE_TYPE_JPEG;
		}
		// 得到指定Format图片的writer
		Iterator<ImageWriter> iter = ImageIO
				.getImageWritersByFormatName(_formatName);// 得到迭代器
		ImageWriter writer = iter.next(); // 得到writer
		// 得到指定writer的输出参数设置(ImageWriteParam )
		ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
		if(imageWriteParam.canWriteCompressed()){
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
			imageWriteParam.setCompressionQuality(quality); // 设置压缩质量参数
		}

		imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		imageWriteParam.setDestinationType(new ImageTypeSpecifier(colorModel,
				colorModel.createCompatibleSampleModel(16, 16)));
		IIOImage iIamge = new IIOImage(bufferedImage, null, null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
			writer.setOutput(ImageIO
                    .createImageOutputStream(byteArrayOutputStream));
			writer.write(null, iIamge, imageWriteParam);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			return byteArrayInputStream;

	}

	/**
	 * 生成验证码
	 * @param width 宽
	 * @param height 高
	 * @param bgColor 背景色
	 * @param fontSize 字体大小
	 * @param text 内容
	 * @param randomLineNum 混淆画线次数
	 * @return
	 */
	public final static BufferedImage createSimpleCaptchaImage(int width,int height,Color bgColor,int fontSize,String text[],int randomLineNum){
		int padding = 2;

		BufferedImage imag = createImage(width,height,bgColor);
		Graphics g = imag.createGraphics();
		Color lineColor = null;
		for (int i = 0; i < randomLineNum; i++) {
			lineColor = new Color(RandomUtils.nextInt(0,256), RandomUtils.nextInt(0,256), RandomUtils.nextInt(0,256));
			g.setColor(lineColor);
			int x1 = RandomUtils.nextInt(0,width + 1);
			int y1 = RandomUtils.nextInt(0,height + 1);
			int x2 = RandomUtils.nextInt(0,width + 1);
			int y2 = RandomUtils.nextInt(0,height + 1);
			g.drawLine(x1, y1, x2, y2);
		}
		Color textColor = null;
		// 添加水印
		for (int i = 0; i < text.length; i++) {
			textColor = new Color(RandomUtils.nextInt(0,256), RandomUtils.nextInt(0,256), RandomUtils.nextInt(0,256));
			int x = padding + i * width/text.length;
			int y = RandomUtils.nextInt(padding,height-fontSize-padding);
			System.out.println(y);
			pressText(imag,text[i],"宋体",Font.BOLD,textColor,fontSize,x,y+fontSize,0.9f);
		}

		g.dispose();

		return imag;
	}

	/**
	 * 生成二维码
	 * @param qrCodeSize 大小像素单位
	 * @param content 内容
	 * @param character_set 编码 utf-8等
	 * @param margin 白边边距
	 * @param bgColor 背景色
	 * @param frontColor 前景色
	 * @return
	 * @throws WriterException
	 */
	public static final BufferedImage createQrCode(int qrCodeSize, String content,String character_set,int margin, Color bgColor, final Color frontColor) throws WriterException {
		return createQrCode(qrCodeSize, content,character_set,margin,ErrorCorrectionLevel.L,bgColor, new QrcodeColorGenerator() {
			@Override
			public Color generate(int i, int j) {
				return frontColor;
			}
		});
	}

	/**
	 * 生成二维码
	 * @param qrCodeSize 大小像素单位
	 * @param content 内容
	 * @param character_set 编码 utf-8等
	 * @param margin 白边边距
	 * @param errorCorrectionLevel 容错率，越高点数越多
	 * @param bgColor 背景色
	 * @param qrcodeColorGenerator 前景色生成器
	 * @return
	 * @throws WriterException
	 */
	public static final BufferedImage createQrCode(int qrCodeSize,String content,String character_set,int margin,ErrorCorrectionLevel errorCorrectionLevel,Color bgColor,QrcodeColorGenerator qrcodeColorGenerator) throws WriterException {

		//设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);  // 矫错级别
		hintMap.put(EncodeHintType.CHARACTER_SET, character_set);
		hintMap.put(EncodeHintType.MARGIN, margin);
		MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
		//创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		int matrixHeight = byteMatrix.getWidth();

		BufferedImage imag = createImage(matrixWidth,matrixHeight,bgColor);
		Graphics g = imag.createGraphics();
		Color _frontColor = null;
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					_frontColor = qrcodeColorGenerator.generate(i,j);
					g.setColor(_frontColor);
					g.fillRect(i, j, 1, 1);
				}
			}
		}
		g.dispose();
		return imag;
	}

	/**
	 * 读取二维码内容
	 * @param image
	 * @param character_set
	 * @return
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static final String readQrCode(BufferedImage image,String character_set) throws IOException, NotFoundException {
		MultiFormatReader formatReader = new MultiFormatReader();
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, character_set);
		Result result = formatReader.decode(binaryBitmap, hints);
		return result.getText();
	}
}