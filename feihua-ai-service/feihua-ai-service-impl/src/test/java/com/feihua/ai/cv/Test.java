package com.feihua.ai.cv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by yangwei
 * Created at 2019/5/22 10:57
 */
public class Test {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(final String[] args) {
        // test_1();
        // test_2();
        new Test().test_3();
    }

    /**
     * 来自网络
     * 矩阵输出，不知道干嘛的
     */
    public static void test_1(){
        Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
        System.out.println( "mat = " + mat.dump() );
    }

    /**
     * 来自网络
     * 好像是图片灰度处理
     */
    public static void test_2(){
        Mat image = Imgcodecs.imread("D:\\card.png");
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 10);
        Imgcodecs.imwrite("D:\\card1.png", image);
    }

    /**
     * 来自官方示例
     * 人脸检测
     */
    public void test_3() {
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier("D:\\program-files\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
        Mat image = Imgcodecs.imread("C:\\Users\\Lenovo\\Desktop\\lena.png");

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces",
                faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = "d:/faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
    }
}
