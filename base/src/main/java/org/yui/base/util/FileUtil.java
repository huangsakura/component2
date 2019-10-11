package org.yui.base.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文件工具类
 */
@Log4j2
public abstract class FileUtil {

    /**
     * 创建空白文件
     * @param fileAbsolutePath
     * @return
     */
    @Nullable
    public static Path createEmptyFile(String fileAbsolutePath) {

        Path path = FileSystems.getDefault().getPath(fileAbsolutePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("删除文件时出错:{}",e.getMessage());
            return null;
        }

        /**
         * 先创建文件夹
         */
        if (!createDirectoryQuietly(path.getParent())) {
            return null;
        }
        /**
         * 再创建文件
         */
        try {
            return Files.createFile(path);
        } catch (IOException e) {
            log.error("创建文件时出错:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 根据文件获取文件名
     * @param path
     */
    public static String getFileName(@NotNull Path path) {
        int nameCount = path.getNameCount();
        return path.subpath(nameCount - 1,nameCount).toString();
    }

    /**
     * 创建文件夹
     * @param directoryPath
     * @throws IOException
     */
    public static void createDirectory(Path directoryPath) throws IOException {

        Optional<Path> pathOptional = Optional.ofNullable(directoryPath);
        if (pathOptional.isPresent() && !Files.exists(pathOptional.get())) {
            Files.createDirectories(pathOptional.get());
        }
    }

    public static boolean createDirectoryQuietly(Path directoryPath) {
        try {
            createDirectory(directoryPath);
            return true;
        } catch (IOException e) {
            log.warn("创建文件夹出错:{}",e);
            return false;
        }
    }

    /**
     * 给定一个文件夹，列出里面所有的文件
     * @param file
     * @param recursive
     * @param filenameFilter
     * @return
     */
    @Nullable
    public static List<File> list(@NotNull File file, boolean recursive,FilenameFilter filenameFilter) {

        Validate.isTrue(file.isDirectory());

        List<File> fileList = null;

        File[] files = null;
        if (null != filenameFilter) {
            files = file.listFiles(filenameFilter);
        } else {
            files = file.listFiles();
        }

        if (null != files) {

            fileList = new ArrayList<>();

            for (File file1 : files) {
                if (file1.isDirectory()) {
                    if (recursive) {
                        List<File> fileList1 = list(file1,true,filenameFilter);
                        if (null != fileList1) {
                            fileList.addAll(fileList1);
                        }
                    }
                } else {
                    fileList.add(file1);
                }
            }
        }
        return fileList;
    }

    @SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
    public static void main(String[] args) {
        //createEmptyFile("D:/cc/ss/dd.txt");

        /*
        Path path = Paths.get("D:/cc/ss/dd.txt");
        System.out.println(path.toString());
        System.out.println(path.getNameCount());
        System.out.println(path.subpath(0,1));
        System.out.println(path.subpath(1,2));
        System.out.println(path.subpath(2,3));
        System.out.println(getFileName(path));
        */

        Optional.ofNullable(list(Paths.get("D:\\tomcat\\tomcat\\webapps\\docs\\appdev\\sample").toFile(),
                true,null)).ifPresent((x) -> {
                    x.forEach((y) -> {
                        System.out.println(y.getAbsolutePath());
                    });
        });
    }
}
