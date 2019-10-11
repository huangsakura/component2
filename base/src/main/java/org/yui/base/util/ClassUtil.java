package org.yui.base.util;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.constant.OtherConstant;
import org.yui.base.constant.StringConstant;
import org.yui.base.enums.Enums;
import sun.awt.OSInfo;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类相关的工具类
 *
 * @author huangjinlong
 */
@Log4j2
public abstract class ClassUtil {

    @Getter
    enum ClassFileProtocol implements Enums {
        /**
         *
         */
        FILE("FILE","文件"),
        JAR("JAR","jar包");

        private final String code;
        private final String message;

        ClassFileProtocol(String code,String message) {
            this.code = code;
            this.message = message;
        }
    }

    /**
     *
     * @param packageName
     * @return
     */
    @Nullable
    public static synchronized Set<Class<?>> listClass(@NotBlank String packageName) {
        log.debug("根据包路径扫描class文件,路径:{}",packageName);

        String warpedPackageName = packageName.replace(StringConstant.POINT,StringConstant.SLASH);
        /**
         * 首先，根据package名称找到package的绝对路径
         */
        Enumeration<URL> urlEnumeration = null;
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            urlEnumeration = classLoader.getResources(warpedPackageName);
        } catch (IOException e) {
            log.warn("包路径不存在:{}",e.getMessage());
            return null;
        }

        if (null != urlEnumeration) {

            Set<Class<?>> classSet = Collections.synchronizedSet(new LinkedHashSet<>());

            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                /**
                 * 获取协议
                 */
                String protocol = url.getProtocol();
                log.debug("协议为:{}",protocol);
                if (ClassFileProtocol.FILE.getCode().equalsIgnoreCase(protocol)) {
                    parseClassWithFile(classSet,url.getFile(),packageName);
                } else if (ClassFileProtocol.JAR.getCode().equalsIgnoreCase(protocol)) {
                    parseClassWithJar(classSet,url,warpedPackageName);
                } else {
                    log.warn("出现其他类型的协议:{}",protocol);
                }
            }
            return classSet;
        }
        return null;
    }


    /**
     *
     * @param classSet
     * @param path
     * @param packageName
     */
    private static void parseClassWithFile(Set<Class<?>> classSet,String path,String packageName) {
        /**
         * 取得包的绝对路径
         */
        String filePath = UrlUtil.decode(path);
        log.debug("绝对路径为:{}",filePath);

        OSInfo.OSType osType = OSInfo.getOSType();
        if (OSInfo.OSType.WINDOWS.equals(osType)) {
            if (filePath.startsWith(StringConstant.SLASH)) {
                filePath = filePath.substring(1);
            }
        }
        /**
         * 遍历绝对路径下的所有以.class结尾的文件
         */
        Optional<List<File>> optionalFileList = Optional.ofNullable(FileUtil.list(Paths.get(filePath).toFile(),
                true,(dir, name) -> (dir.isDirectory() || name.endsWith(OtherConstant.CLASS_FILE_TAIL))));
        optionalFileList.ifPresent(x -> {
            x.forEach(y -> {
                if (y.isDirectory()) {
                    /**
                     * 递归
                     */
                    parseClassWithFile(classSet,path + StringConstant.SLASH + y.getName(),
                            packageName + StringConstant.POINT + y.getName());
                } else {
                    try {
                        String className = y.getName().substring(0, y.getName().length() - OtherConstant.CLASS_FILE_TAIL.length());
                        classSet.add(Class.forName(packageName + StringConstant.POINT + className));
                    } catch (ClassNotFoundException e) {
                        log.warn("{}类不存在",y.getAbsolutePath());
                    }
                }
            });
        });
    }

    /**
     *
     * @param classSet
     * @param url
     * @param warpedPackageName
     */
    private static void parseClassWithJar(Set<Class<?>> classSet,URL url,String warpedPackageName) {
        JarFile jarFile = null;
        try {
            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
        } catch (IOException e) {
            log.warn("获取jar包失败:{}",e.getMessage());
        }
        if (null != jarFile) {
            /**
             * 获取枚举
             */
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            /**
             * 遍历
             */
            while (jarEntryEnumeration.hasMoreElements()) {
                /*
                 * 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                 */
                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                String name = jarEntry.getName();
                /**
                 * 如果第一个字符是斜杠，则去除
                 */
                if (name.startsWith(StringConstant.SLASH)) {
                    name = name.substring(1);
                }

                if (name.startsWith(warpedPackageName)) {
                    int index = name.lastIndexOf(StringConstant.SLASH);
                    /**
                     * 移除最后一个斜杠之后的文本
                     */
                    if (index != -1) {
                        String packageName = name.substring(0, index).replace(StringConstant.SLASH, StringConstant.POINT);

                        if (name.endsWith(OtherConstant.CLASS_FILE_TAIL) && !jarEntry.isDirectory()) {

                            String className = name.substring(packageName.length() + 1,
                                    name.length() - OtherConstant.CLASS_FILE_TAIL.length());

                            String fullName = packageName + StringConstant.POINT + className;
                            log.debug("当前包路径存在的class文件:{}",fullName);

                            try {
                                classSet.add(Class.forName(fullName));
                            } catch (ClassNotFoundException e) {
                                log.warn("{}类加载失败",fullName);
                            }
                        }
                    }
                }
            }
        }
    }
}