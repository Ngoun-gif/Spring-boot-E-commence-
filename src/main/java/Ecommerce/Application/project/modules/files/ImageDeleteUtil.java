package Ecommerce.Application.project.modules.files;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageDeleteUtil {

    private final String uploadDir = "uploads/products/";

    public void deleteImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isBlank()) return;

            // imagePath example:  /uploads/products/abc.jpg
            String filename = imagePath.replace("/uploads/products/", "");

            Path path = Paths.get(uploadDir + filename);

            if (Files.exists(path)) {
                Files.delete(path);
            }

        } catch (Exception e) {
            System.out.println("⚠ Failed to delete image: " + e.getMessage());
        }
    }
}
