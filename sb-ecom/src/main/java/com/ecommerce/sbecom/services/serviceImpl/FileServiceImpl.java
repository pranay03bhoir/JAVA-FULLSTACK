package com.ecommerce.sbecom.services.serviceImpl;

import com.ecommerce.sbecom.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId
                .concat(originalFileName.substring(originalFileName.lastIndexOf(".")));

        // Create directory if it doesn't exist
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create full file path
        String filePath = path + File.separator + fileName;

        // Save the file
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
