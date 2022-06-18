package com.web.util;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class FFmpegUtil {

    public static void fetchFrameToPath (String stream, String imagePath) throws Exception {
        File targetFile = new File(imagePath+ "/" + UUID.randomUUID().toString() +".jpg");
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(stream);
        ff.start();
        int i = 0;
        Frame frame = null;
        while (i < 10) {
            // 过滤前5帧, 避免出现全黑的图片, 依自己情况而定
            frame = ff.grabFrame();
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        ff.stop();
    }

    public static ByteArrayOutputStream fetchFrameToStream (String stream) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(stream);
        ff.start();
        int i = 0;
        Frame frame = null;
        while (i < 10) {
            // 过滤前5帧, 避免出现全黑的图片, 依自己情况而定
            frame = ff.grabFrame();
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(bi, "jpg", bos);
        ff.stop();
        return bos;
    }

    public static void main(String[] args) throws Exception {
        fetchFrameToPath("https://hls.cntv.myalicdn.com/asp/hls/1200/0303000a/3/default/6b9f0e72554949d99d5c03ad5b16ab2f/1.ts", "d:\\");
    }
}
