package com.example.jwt.service.helper;

import cn.hutool.core.io.FileTypeUtil;
import com.alibaba.druid.util.StringUtils;
import com.example.jwt.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;


/**
 * 文件操作辅助类
 */
@Slf4j
public class FileHelper {

    private static final Set<String> SUPPORT_IMAGE_VIDEO_FORMAT = new HashSet<String>() {{
        add("jpg");
        add("jpeg");
        add("png");
        add("mp4");
        add("mov");
    }};

    private static final Set<String> SUPPORT_IMAGE_FORMAT = new HashSet<String>() {{
        add("jpg");
        add("jpeg");
        add("png");
    }};

    private static final Set<String> SUPPORT_MUSIC_FORMAT = new HashSet<String>() {{
        add("mp3");
    }};

    private static final Set<String> SUPPORT_VOICE_FORMAT = new HashSet<String>() {{
        add("mp3");
        add("wav");
        add("aac");
        add("wma");
        add("ogg");
        add("m4a");
        add("m3u8");
    }};

    private static final Set<String> SUPPORT_SVGA_FORMAT = new HashSet<String>() {{
        add("svga");
    }};

    /**
     * 客户端日志上传支持文件类型
     */
    private static final Set<String> SUPPORT_LOG_FORMAT = new HashSet<String>() {{
        add("zip");
    }};

    private static final String DEFAULT_IMAGE_FORMAT = "jpeg";

    /**
     * 校验图片文件格式
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String checkImage(MultipartFile multipartFile) throws IOException {
        String fileFormat = getFormat(multipartFile);
        if (SUPPORT_IMAGE_FORMAT.contains(fileFormat)) {
            return fileFormat;
        }
        return null;
    }

    public static String checkImageAndVideo(MultipartFile multipartFile) throws IOException {
        String fileFormat = getFormat(multipartFile);
        if (SUPPORT_IMAGE_VIDEO_FORMAT.contains(fileFormat)) {
            return fileFormat;
        }
        return null;
    }

    public static String checkSvga(MultipartFile multipartFile) throws IOException {
        String fileFormat = getFormat(multipartFile);
        if (SUPPORT_SVGA_FORMAT.contains(fileFormat)) {
            return fileFormat;
        }
        return "";
    }

    public static String checkMusic(MultipartFile multipartFile) throws IOException {
        String suffix = getFormat(multipartFile);
        if (SUPPORT_MUSIC_FORMAT.contains(suffix)) {
            return suffix;
        }
        return "";
    }

    public static String checkVoice(MultipartFile multipartFile) throws IOException {
        String fileFormat = getFormat(multipartFile);
        if (SUPPORT_VOICE_FORMAT.contains(fileFormat)) {
            return fileFormat;
        }
        return "";
    }

    public static String checkLog(MultipartFile multipartFile) throws IOException {
        String fileFormat = getFormat(multipartFile);
        if (SUPPORT_LOG_FORMAT.contains(fileFormat)) {
            return fileFormat;
        }
        return "";
    }

    public static boolean containImage(String fileFormat) {
        return SUPPORT_IMAGE_FORMAT.contains(fileFormat);
    }

    public static void bufferedMultipartFileToLocalFile(MultipartFile multipartFile, String localPath) throws IOException {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(multipartFile.getInputStream());

            fos = new FileOutputStream(localPath);
            bos = new BufferedOutputStream(fos);

            int size = 0;
            byte[] buffer = new byte[8192];
            while ((size = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, size);
            }
            //刷新此缓冲的输出流，保证数据全部都能写出
            bos.flush();
        } catch (Exception e) {
            log.error(">>>>> bufferedMultipartFileToLocalFile error[Exception]! multipartFile:{}, localPath:{}, {}", multipartFile, localPath, e);
        } finally {
            if (null != bis) {
                bis.close();
            }
            if (null != bos) {
                bos.close();
            }
        }
    }

    /**
     * 存储文件到本地
     *
     * @param multipartFile
     * @param localPath     本地文件路径
     * @return
     * @throws IOException
     */
    public static void multipartFileToLocalFile(MultipartFile multipartFile, String localPath) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File localFile = null;
        int bufferSize = 8192;
        try {
            // MultipartFile -> File
            inputStream = multipartFile.getInputStream();
            localFile = new File(localPath);
            outputStream = new FileOutputStream(localFile);
            int bytesRead;
            byte[] buffer = new byte[bufferSize];
            while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error(">>>>> multipartFileToFile error[Exception]! multipartFile:{}, localPath:{}, {}", multipartFile, localPath, e);
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }


    /*
     * 根据url下载图片到本地
     *
     * @param url
     * @param localPath
     * @throws IOException
     */
    public static void downloadFromUrl(String url, String localPath) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new URL(url).openStream();
            byte[] bytes = new byte[1024];
            os = new FileOutputStream(localPath);
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        } catch (IOException e) {
            log.error(">>>>> downloadFromUrl error[Exception]! url:{}, localPath:{} {}", url, localPath, e);
        } finally {
            if (null != os) {
                os.close();
            }
            if (null != is) {
                is.close();
            }
        }
    }

    /**
     * 根据url获取图片格式
     *
     * @param url
     * @return
     */
    public static String getFormatFromUrl(String url) {
        String format = DEFAULT_IMAGE_FORMAT;
        try (InputStream is = new URL(url).openStream(); BufferedInputStream bis = new BufferedInputStream(is)) {
            String contentType = HttpURLConnection.guessContentTypeFromStream(bis);
            if (StringUtils.isEmpty(contentType)) {
                log.error(">>>>> getFormatFromUrl failed! url:{}", url);
                return format;
            }

            String[] split = contentType.split("/");
            return split[1];
        } catch (IOException e) {
            log.error(">>>>> getFormatFromUrl error! url:{}, {}", url, e);
            return format;
        }
    }



    /**
     * 删除本地文件
     *
     * @param localPath 本地文件路径
     * @return 删除结果
     */
    public static boolean removeLocalFile(String localPath) {
        File localFile = new File(localPath);
        return removeLocalFile(localFile);
    }


    /**
     * 删除本地文件
     *
     * @param localFile 本地文件对象
     * @return
     */
    public static boolean removeLocalFile(File localFile) {

        if (null == localFile) {
            log.error(">>>>> local file is null! remove error!");
            return false;
        }
        if (!localFile.exists()) {
            log.warn("local file:{} not exists! remove failed!", localFile.getAbsoluteFile());
            return false;
        }
        return localFile.delete();
    }

    /**
     * 创建存储路径
     *
     * @param userId
     * @param fileFormat
     * @param prefix
     * @param parentPath
     * @return
     */
    public static String createPath(Long userId, String fileFormat, String prefix, String parentPath) {
        String key = prefix + userId + "-" + System.currentTimeMillis() + "." + fileFormat;
        String subPath1 = String.valueOf(userId % 100);
        String subPath2 = String.valueOf((userId / 100) % 100);
        return new StringJoiner(CommonConstants.PATH_SPLIT)
                .add(parentPath)
                .add(subPath2)
                .add(subPath1)
                .add(key)
                .toString();
    }

    private static String getFormat(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        log.debug("[file_helper] getFormat originalFilename:{}", originalFilename);

        String format = FileTypeUtil.getType(multipartFile.getInputStream());
        if (!StringUtils.isEmpty(format)) {
            return format.toLowerCase();
        }

        int index;
        if (!StringUtils.isEmpty(originalFilename) && (index = originalFilename.lastIndexOf(CommonConstants.POINT)) != -1) {
            return originalFilename.substring(index + 1).toLowerCase();
        }
        return "";
    }
}
