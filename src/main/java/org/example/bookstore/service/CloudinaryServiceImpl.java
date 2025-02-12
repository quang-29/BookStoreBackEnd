package org.example.bookstore.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.example.bookstore.payload.response.CloudinaryResponse;
import org.example.bookstore.service.Interface.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) throws FileUploadException {
        try {
            final Map result = cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of("public_id",
                                    "minhquang/"
                                            + fileName));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}