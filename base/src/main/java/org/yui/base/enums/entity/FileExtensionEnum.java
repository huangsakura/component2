package org.yui.base.enums.entity;

import lombok.Getter;

/**
 *
 */
@Getter
public enum FileExtensionEnum implements EntityEnum {


    JPG("JPG","JPG格式"),
    JPEG("JPEG","JPEG格式"),
    BMP("BMP","BMP格式"),
    GIF("GIF","GIF格式"),
    PNG("PNG","PNG格式"),

    TXT("TXT","TXT格式"),
    DOC("DOC","DOC格式"),
    DOCX("DOCX","DOCX格式"),
    PDF("PDF","PDF格式"),
    PPT("PPT","PPT格式"),
    PPTX("PPTX","PPTX格式"),
    XLS("XLS","XLS格式"),
    XLSX("XLSX","XLSX格式"),

    RAR("RAR","RAR格式"),
    ZIP("ZIP","ZIP格式"),
    APK("APK","APK格式"),

    OTHER("OTHER","其他格式");

    private final String code;
    private final String dbCode;
    private final String message;

    private FileExtensionEnum(String code, String message) {
        this.code = code;
        this.dbCode = code;
        this.message = message;
    }

    /**
     * 根据文件扩展名找到对应的枚举。
     * 文件名必须是全小写
     * @param realExtension
     * @return
     */
    public static FileExtensionEnum findByRealExtension(String realExtension) {

        for (FileExtensionEnum fileExtensionEnum : values()) {
            if (fileExtensionEnum.getDbCode().equalsIgnoreCase(realExtension)) {
                return fileExtensionEnum;
            }
        }
        return FileExtensionEnum.OTHER;
    }
}
