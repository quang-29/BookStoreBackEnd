package org.example.bookstore.service.Interface;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface AwsS3Service {
    String saveImageToS3(MultipartFile file);
}
