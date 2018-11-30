package com.drinkbird.restaurant.reviews.api;

import java.util.Base64;

public class ImageBase64 {
    private final String contentType;
    private final String fileExtension;
    private final String content;

    private ImageBase64(String contentType, String fileExtension, String content) {
        this.contentType = contentType;
        this.fileExtension = fileExtension;
        this.content = content;
    }

    // imageBase64 example:
    // data:image/gif;base64,R0lGODl...
    public static ImageBase64 parse(String imageBase64) {
        String[] block = imageBase64.split(";");

        String contentType = block[0].split(":")[1]; // image/gif
        String fileExtension = contentType.split("/")[1]; // gif
        String content = block[1].split(",")[1]; // R0l...

        return new ImageBase64(contentType, fileExtension, content);
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public byte[] getContentBytes() {
        return Base64.getDecoder().decode(content.getBytes());
    }
}
