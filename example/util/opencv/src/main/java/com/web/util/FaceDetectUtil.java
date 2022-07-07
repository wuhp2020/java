package com.web.util;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import javax.swing.*;
import java.io.File;

public class FaceDetectUtil {

    private static File faceFile;

    static {
//        faceFile = new File(ClassLoader.getSystemClassLoader().getResource("face/lbpcascade_frontalface_improved.xml").getFile());
        faceFile = new File(ClassLoader.getSystemClassLoader().getResource("face/lbpcascade_frontalface.xml").getFile());
    }

    public static void detect(String winTitle, FrameGrabber grabber) throws Exception {
        OpenCVFrameConverter.ToMat convertToMat = new OpenCVFrameConverter.ToMat();
        CascadeClassifier face_cascade = new CascadeClassifier(faceFile.getAbsolutePath());
        RectVector faces = new RectVector();
        //新建一个窗口
        CanvasFrame canvas = new CanvasFrame(winTitle,1);
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isVisible()) {
                break;
            }
            Frame frame = grabber.grab();
            Mat mat = convertToMat.convert(frame);
            if (mat.empty()) {
                continue;
            }
            Mat videoMatGray = new Mat();
            opencv_imgproc.cvtColor(mat, videoMatGray, opencv_imgproc.COLOR_BGRA2GRAY);
            opencv_imgproc.equalizeHist(videoMatGray, videoMatGray);
            //int[] rejectLevels = new int[0];
            //double[] levelWeights = new double[0];
            face_cascade.detectMultiScale(videoMatGray, faces);
            for (int i = 0; i < faces.size(); i++) {
                Rect face = faces.get(i);
                opencv_imgproc.rectangle(mat, face, Scalar.RED, 4, 8, 0);
            }

            //opencv_highgui.imshow(winTitle, mat);
            //opencv_highgui.waitKey(30);
            canvas.showImage(convertToMat.convert(mat));
            Thread.sleep(30);//50毫秒刷新一次图像
        }
    }

    public static void detectByFrame(Frame frame) throws Exception {
        OpenCVFrameConverter.ToMat convertToMat = new OpenCVFrameConverter.ToMat();
        CascadeClassifier face_cascade = new CascadeClassifier(faceFile.getAbsolutePath());
        RectVector faces = new RectVector();
        //新建一个窗口
        CanvasFrame canvas = new CanvasFrame("face",1);
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isVisible()) {
                break;
            }
            Mat mat = convertToMat.convert(frame);
            if (mat.empty()) {
                continue;
            }
            Mat videoMatGray = new Mat();
            opencv_imgproc.cvtColor(mat, videoMatGray, opencv_imgproc.COLOR_BGRA2GRAY);
            opencv_imgproc.equalizeHist(videoMatGray, videoMatGray);
            //int[] rejectLevels = new int[0];
            //double[] levelWeights = new double[0];
            face_cascade.detectMultiScale(videoMatGray, faces);
            for (int i = 0; i < faces.size(); i++) {
                Rect face = faces.get(i);
                opencv_imgproc.rectangle(mat, face, Scalar.RED, 4, 8, 0);
            }

            //opencv_highgui.imshow(winTitle, mat);
            //opencv_highgui.waitKey(30);
            canvas.showImage(convertToMat.convert(mat));
            Thread.sleep(30);//50毫秒刷新一次图像
        }
    }

    public static void main(String[] args) throws Exception {
        Frame frame = FFmpegUtil.fetchFrame("https://hls.cntv.myalicdn.com/asp/hls/1200/0303000a/3/default/6b9f0e72554949d99d5c03ad5b16ab2f/1.ts");
        FaceDetectUtil.detectByFrame(frame);
    }
}
