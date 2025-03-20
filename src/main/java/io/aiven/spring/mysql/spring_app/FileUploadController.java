package io.aiven.spring.mysql.spring_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class FileUploadController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Define a fixed upload directory inside the project (Better for development)
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/{id}/uploadImage")
    public String uploadImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            // Check if user exists
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isEmpty()) {
                return "User not found";
            }

            User user = optionalUser.get();

            // Create an absolute path for uploads
            String currentDir = System.getProperty("user.dir");
            String absoluteUploadDir = currentDir + File.separator + UPLOAD_DIR;

            // Ensure the upload directory exists
            File directory = new File(absoluteUploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create if not exists
            }

            // Save file using the absolute path
            String fileName = id + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(absoluteUploadDir + fileName);
            file.transferTo(filePath.toFile());

            // Save image URL in database - keep this as a relative URL for serving
            String imageUrl = "/uploads/" + fileName;
            user.setImageUrl(imageUrl);
            userRepository.save(user);

            return "Image uploaded successfully: " + imageUrl;

        } catch (IOException e) {
            return "Error uploading image: " + e.getMessage();
        }
    }
    }
