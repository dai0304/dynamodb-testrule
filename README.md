# DynamoDB JUnit test rule (dynamodb-testrule)

This is JUnit 4 [TestRule](http://junit.org/junit4/javadoc/4.12/org/junit/rules/TestRule.html) to setup embedded DynamoDBLocal before a test.

[Amazon DynamoDB](https://aws.amazon.com/dynamodb/) is one of the NoSQL database implementation on AWS cloud.
[DynamoDBLocal](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) is the downloadable version of DynamoDB, which can run in local environment.

DynamoDBLocal depends on [sqlite4java](https://bitbucket.org/almworks/sqlite4java) and it depends on native library for each target platform/architecture -- `*.dll` `*.so` `*.jnilib` artifacts.  This native library must be placed on local file system and call `SQLite.setLibraryPath()` before working with sqlite4java.

This test rule act as following:

1. creates temporary directory and write native libraries in it.
2. call `SQLite.setLibraryPath()`
3. instantiate `DynamoDBEmbedded` for each test.
4. and provide access to `AmazonDynamoDB` and `AmazonDynamoDBStreams` clients.

## Example

see [test](src/test/java/jp/xet/dynamodbtestrule/DynamoDbLocalRuleTest.java)
