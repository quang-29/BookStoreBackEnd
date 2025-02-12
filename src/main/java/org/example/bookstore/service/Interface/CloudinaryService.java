package org.example.bookstore.service.Interface;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) throws FileUploadException;
}
