package com.example.sherlockescape.utils;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


@RequiredArgsConstructor
@Component
public class CommonUtils {

    private final AmazonS3Client amazonS3Client;
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "Server";
    private static final String TIME_SEPARATOR = "." ;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //S3 사진 저장이름 생성 메서드
    public String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    }

    //S3 사진 올리기 기능 util 메서드
    public String createAll(String originalFileName, String contentType, InputStream inputStream) throws IOException {
            String fileName = buildFileName(Objects.requireNonNull(originalFileName));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

}