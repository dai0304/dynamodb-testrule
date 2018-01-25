package jp.xet.dynamodbtestrule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

/**
 * Test for {@link DynamoDbLocalRule}.
 *
 * @author daisuke
 * @since #version#
 */
public class DynamoDbLocalRuleTest {
	
	@Rule
	public DynamoDbLocalRule dynamoDbLocalRule = new DynamoDbLocalRule();
	
	@Test
	public void test() {
		DynamoDB dynamoDb = new DynamoDB(dynamoDbLocalRule.getAmazonDynamoDB());
		Table table = dynamoDb.createTable(new CreateTableRequest()
				.withTableName("TestTable")
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
				.withKeySchema(new KeySchemaElement("id", KeyType.HASH))
				.withAttributeDefinitions(new AttributeDefinition("id", ScalarAttributeType.S)));
		table.putItem(new Item()
				.withString("id", "aaa")
				.withString("value", "AAA"));
		table.putItem(new Item()
				.withString("id", "bbb")
				.withString("value", "BBB"));
		Item item = table.getItem("id", "bbb");
		assertThat(item.getString("value"), is("BBB"));
	}
}
