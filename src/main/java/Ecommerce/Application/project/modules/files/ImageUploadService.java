package Ecommerce.Application.project.modules.files;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadService {

    private final String uploadDir = "uploads/products/";

    // Allowed file extensions
    private final String[] allowedExt = {"png", "jpg", "jpeg", "webp"};

    public String upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new FileValidationException("File is empty.");
        }

        // Validate extension
        String originalName = file.getOriginalFilename();
        String ext = getExtension(originalName).toLowerCase();

        if (!isAllowed(ext)) {
            throw new FileValidationException("Only PNG, JPG, JPEG, WEBP are allowed.");
        }

        // Create upload directory if not exists
        Files.createDirectories(Paths.get(uploadDir));

        // Unique file name
        String newName = UUID.randomUUID() + "." + ext;

        // Save file
        Path filePath = Paths.get(uploadDir + newName);
        Files.write(filePath, file.getBytes());

        // Return public URL (static resource)
        return "/uploads/products/" + newName;
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private boolean isAllowed(String ext) {
        for (String a : allowedExt) {
            if (a.equals(ext)) return true;
        }
        return false;
    }
}
