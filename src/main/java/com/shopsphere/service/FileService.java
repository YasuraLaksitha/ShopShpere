package com.shopsphere.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    @NotNull String uploadImage(@NotNull MultipartFile productImage, String imageDirName) throws IOException;
}
