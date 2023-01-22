package com.pramod.aws.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pramod.aws.dto.Order;

public class CreateOrderLamda {

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order =objectMapper.readValue(requestEvent.getBody(), Order.class);
        DynamoDB dynamodb = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
        Table table=dynamodb.getTable(System.getenv("ORDER_TABLE"));
        Item item = new Item().withPrimaryKey("id",order.id)
                .withString("itemName",order.itemName)
                .withInt("quantity",order.quantity);
        table.putItem(item);
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Order ID :"+order.id);
    }
}
