package org.example.bookstore.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.springframework.web.multipart.MultipartFile;
import lombok.experimental.UtilityClass;
@UtilityClass
public class FileUploadUtil {
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024;   // 5 MB

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(final String fileName, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) throws FileUploadException {
        final long size = file.getSize();
        if (size > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_UPLOAD_SIZE);
        }

        final String fileName = file.getOriginalFilename();
        if (!isAllowedExtension(fileName, pattern)) {
            throw new AppException(ErrorCode.FILE_UPLOAD_SIZE);
        }
    }

    public static String getFileName(final String name) {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, name, date);
    }
}
