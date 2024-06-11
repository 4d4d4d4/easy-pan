package com.cj.utils;

import org.apache.commons.io.FileUtils;
import org.aspectj.apache.bcel.classfile.SourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

/**
 * @Classname ScaleFilter
 * @Description ffmpeg工具类
 * @Date 2024/3/16 14:05
 * @Created by 憧憬
 */
public class ScaleFilter {
    private static Logger logger = LoggerFactory.getLogger(ScaleFilter.class);

    /**
     *
     * @param sourceVideo 视频源文件路径
     * @param width 缩略图大小
     * @param targetFile 目标文件路径
     */
    public static void createCover4Video(File sourceVideo, Integer width, File targetFile){
        try {
            String cmd = "ffmpeg -i %s -y -vframes 1 -vf scale=%d:%d/a %s";
            ProcessUtils.executeCommand(String.format(cmd, sourceVideo.getAbsoluteFile(), width, width, targetFile), false);
        }catch (Exception e){
            logger.error("生成视频封面失败");
        }
    }

    /**
     * 图片缩略图
     * @param sourceFile 源文件
     * @param width 压缩的大小
     * @param targetFile 输出文件
     * @param delSource 是否删除源文件
     */
    public static void compressImage(File sourceFile, Integer width, File targetFile, Boolean delSource) {
        try {
            String cmd = "ffmpeg -i %s -vf scale=%d:-1 %s -y";
            ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsoluteFile(), width, targetFile.getAbsoluteFile()), false);
            if (delSource) {
                FileUtils.forceDelete(sourceFile);
            }
        } catch (Exception e) {
            logger.error("压缩图片失败");
        }
    }

    /**
     *
     * @param file
     * @param thumbnailWidth
     * @param targetFile
     * @param delSource
     * @return
     */
    public static Boolean createThumbnailWidthFFmpeg(File file, int thumbnailWidth, File targetFile, Boolean delSource) {
        try {
            BufferedImage src = ImageIO.read(file);
            //thumbnailWidth 缩略图的宽度   thumbnailHeight 缩略图的高度
            int sorceW = src.getWidth();
            int sorceH = src.getHeight();
            //小于 指定高宽不压缩
            if (sorceW <= thumbnailWidth) {
                return false;
            }
            compressImage(file, thumbnailWidth, targetFile, delSource);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param sourceFile
     * @param widthPercentage
     * @param targetFile
     */
    public static void compressImageWidthPercentage(File sourceFile, BigDecimal widthPercentage, File targetFile) {
        try {
            BigDecimal widthResult = widthPercentage.multiply(new BigDecimal(ImageIO.read(sourceFile).getWidth()));
            compressImage(sourceFile, widthResult.intValue(), targetFile, true);
        } catch (Exception e) {
            logger.error("压缩图片失败");
        }
    }
}
