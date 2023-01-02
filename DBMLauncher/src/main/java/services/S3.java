package services;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3 {

    private final String S3_BUCKET_NAME = "dynamo-db-students-table-actions";

    public String getS3_BUCKET_NAME() {
        return S3_BUCKET_NAME;
    }

    public S3Client authenticateS3(AwsBasicCredentials awsBasicCredentials) {
        return S3Client
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.of(System.getenv("AWS_REGION")))
                .build();
    }

    public String createBucket(S3Client s3Client) {
        ListBucketsResponse s3Response = s3Client.listBuckets();
        for (Bucket bucket : s3Response.buckets()) {
            if (bucket.name().equals(S3_BUCKET_NAME)) {
                return bucket.name() + " table already exists.";
            }
        }

        try {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .build();

            s3Client.createBucket(bucketRequest);
        } catch (S3Exception error) {
            System.err.println(error.getMessage());
            System.exit(1);
        }
        return "Bucket " + S3_BUCKET_NAME + " created.";
    }
}