package com.drinkbird.restaurant.reviews.api;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class ImageRepository {
    private final CloudBlobContainer container;

    public ImageRepository(Environment env) {
        String connectionString = Objects.requireNonNull(
            env.getProperty("azure.storage.connectionString"));
        String containerName = Objects.requireNonNull(
            env.getProperty("azure.storage.photosContainer"));

        try {
            CloudStorageAccount account =
                CloudStorageAccount.parse(connectionString);

            CloudBlobClient client = account.createCloudBlobClient();

            CloudBlobContainer container =
                client.getContainerReference(containerName);

            container.createIfNotExists(
                BlobContainerPublicAccessType.BLOB, null, null);

            this.container = container;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public URI Upload(String imageName, ImageBase64 imageBase64) {
        try {
            String blobName =
                String.format("%s.%s", imageName, imageBase64.getFileExtension());

            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
            blob.getProperties().setContentType(imageBase64.getContentType());

            byte[] contentBytes = imageBase64.getContentBytes();
            blob.uploadFromByteArray(contentBytes, 0, contentBytes.length);

            return blob.getUri();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
