package services;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.*;

public class ApiGateway {

    /**
     * Authenticate to the API Gateway client using the AWS user's credentials.
     * @param awsCredentials The AWS Access Key ID and Secret Access Key are credentials that are used to securely sign requests to AWS services.
     * @return Service client for accessing API Gateway.
     */
    public static ApiGatewayClient authenticateApiGateway(AwsBasicCredentials awsCredentials, Region appRegion) {
        return ApiGatewayClient
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(appRegion)
                .build();
    }

    /**
     * Create a REST API served by API Gateway.
     * @param apiGatewayClient Client for accessing Amazon API Gateway.
     * @return The API's identifier.
     */
    public static String createAPI(ApiGatewayClient apiGatewayClient) {
        EndpointType endpointType = EndpointType.REGIONAL;

        EndpointConfiguration endpointConfiguration = EndpointConfiguration
                .builder()
                .types(endpointType)
                .build();

        try {
            CreateRestApiRequest request = CreateRestApiRequest.builder()
                    .name("database-manager-rest-api")
                    .description("Created using the Gateway Java API")
                    .endpointConfiguration(endpointConfiguration)
                    .build();

            CreateRestApiResponse response = apiGatewayClient.createRestApi(request);
            System.out.println("The id of the new api is " + response.id());
            return response.id();
        } catch (ApiGatewayException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }

    /**
     * Requests API Gateway to create a resource.
     * @param apiGatewayClient Client for accessing Amazon API Gateway.
     * @param apiId The string identifier of the associated RestApi.
     * @return The parent resource's identifier.
     */
    public static String createResource(ApiGatewayClient apiGatewayClient, String apiId, int parentResourceIndex) {
        GetResourcesRequest getResourcesRequest = GetResourcesRequest
                .builder()
                .restApiId(apiId)
                .build();

        GetResourcesResponse getResourcesResponse = apiGatewayClient.getResources(getResourcesRequest);

        CreateResourceRequest createResourceRequest = CreateResourceRequest
                .builder()
                .parentId(getResourcesResponse.items().get(parentResourceIndex).id())
                .restApiId(apiId)
                .pathPart("get-all-table-items")
                .build();

        CreateResourceResponse createResourceResponse = apiGatewayClient.createResource(createResourceRequest);
        return createResourceResponse.id();
    }

    /**
     * Requests API Gateway to create a method.
     * @param apiGatewayClient Client for accessing Amazon API Gateway.
     * @param apiId The string identifier of the associated RestApi.
     * @param resourceId The Resource identifier for the new Method resource.
     * @return The method request's HTTP method type.
     */
    public static String createMethod(ApiGatewayClient apiGatewayClient, String apiId, String resourceId, String httpMethod) {
        PutMethodRequest putMethodRequest = PutMethodRequest
                .builder()
                .restApiId(apiId)
                .resourceId(resourceId)
                .httpMethod(httpMethod)
                .authorizationType("NONE")
                .build();

        PutMethodResponse putMethodResponse = apiGatewayClient.putMethod(putMethodRequest);

        PutIntegrationRequest putIntegrationRequest = PutIntegrationRequest
                .builder()
                .restApiId(apiId)
                .resourceId(resourceId)
                .httpMethod("HTTP")
                .type(IntegrationType.AWS)
                .build();

        PutIntegrationResponse putIntegrationResponse = apiGatewayClient.putIntegration(putIntegrationRequest);

        return putMethodResponse.httpMethod();
    }
}