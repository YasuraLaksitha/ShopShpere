package com.shopsphere.service.impl;

import com.shopsphere.service.FileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public @NotNull String uploadImage(final @NotNull MultipartFile productImage, final String imageDirName) throws IOException {
        final String originalFilename = productImage.getOriginalFilename();

        final String fileName =
                UUID.randomUUID().toString().concat(Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf('.')));

        final File folder = new File(imageDirName);
        if (!folder.exists())
            folder.mkdirs();

        final String filePath = imageDirName + File.separator + fileName;
        Files.copy(productImage.getInputStream(), Paths.get(filePath));

        return filePath;
    }
}
