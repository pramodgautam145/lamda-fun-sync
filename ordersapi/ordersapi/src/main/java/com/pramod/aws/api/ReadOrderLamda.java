package com.pramod.aws.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pramod.aws.dto.Order;

import java.util.List;
import java.util.stream.Collectors;

public class ReadOrderLamda {

    public APIGatewayProxyResponseEvent readOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
       // Order order = new Order(123,"Macbook Pro",100);
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        ScanResult orders_table = amazonDynamoDB.scan(new ScanRequest().withTableName(System.getenv("ORDER_TABLE")));
        List<Order> order = orders_table.getItems().stream().
                map(item -> new Order(Integer.parseInt(item.get("id").getN()), item.get("itemName").getS(), Integer.parseInt(item.get("quantity").getN()))).collect(Collectors.toList());
        String jsonOutput = objectMapper.writeValueAsString(order);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(jsonOutput);
    }
}
