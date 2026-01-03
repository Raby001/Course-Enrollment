package enroll_management.enroll_management.services.common;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IOException("Image file is empty");
        }

        // Upload to Cloudinary
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader()
                .upload(imageFile.getBytes(), ObjectUtils.asMap(
                        "folder", "student_profiles",
                        "resource_type", "image"
                ));

        // Return secure URL
        return (String) uploadResult.get("secure_url");
    }
}