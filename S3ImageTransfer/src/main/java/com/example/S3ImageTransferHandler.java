package com.example;

/**
 * AWS Lambda
 *
 */
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;

public class S3ImageTransferHandler implements RequestHandler<S3Event, String> {

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private static final String DESTINATION_BUCKET = "s3-destination-bucket-mannat";

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        s3Event.getRecords().forEach(record -> {
            context.getLogger().log("Inside handleRequest function");
            String sourceBucket = record.getS3().getBucket().getName();
            String sourceKey = record.getS3().getObject().getKey();

            // Log source bucket and key
            context.getLogger().log("Source Bucket: " + sourceBucket);
            context.getLogger().log("Source Key: " + sourceKey);

            try {
                // Try to copy the object and log success
                CopyObjectRequest copyObjRequest = new CopyObjectRequest(sourceBucket, sourceKey, DESTINATION_BUCKET, sourceKey);
                s3Client.copyObject(copyObjRequest);
                context.getLogger().log("Successfully copied " + sourceKey + " from " + sourceBucket + " to " + DESTINATION_BUCKET);
            } catch (Exception e) {
                // Catch and log any errors that occur
                context.getLogger().log("Error copying object: " + e.getMessage());
            }
        });

        return "Image transfer complete";
    }
}
