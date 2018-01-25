/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.xet.dynamodbtestrule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.rules.ExternalResource;

import com.almworks.sqlite4java.SQLite;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;

/**
 * JUnit test rule to setup / tear down {@link DynamoDBEmbedded}.
 *
 * @author daisuke
 * @since #version#
 */
public class DynamoDbLocalRule extends ExternalResource {
	
	private static final String[] NATIVE_FILENAMES = {
			"libsqlite4java-linux-amd64-1.0.392.so",
			"libsqlite4java-linux-i386-1.0.392.so",
			"libsqlite4java-osx-1.0.392.dylib",
			"sqlite4java-win32-x64-1.0.392.dll",
			"sqlite4java-win32-x86-1.0.392.dll"
	};
	
	static {
		try {
			Path targetDir = Files.createTempDirectory("sqlite4java-natives");
			for (String nativeFilename : NATIVE_FILENAMES) {
				String classpath = "/sqlite4java-natives/" + nativeFilename;
				try (InputStream in = DynamoDbLocalRule.class.getResourceAsStream(classpath)) {
					Path targetFile = targetDir.resolve(nativeFilename);
					Files.copy(in, targetFile);
				}
			}
			SQLite.setLibraryPath(targetDir.toString());
			targetDir.toFile().deleteOnExit();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private final File file;
	
	private AmazonDynamoDB amazonDynamoDb;
	
	private AmazonDynamoDBStreams amazonDynamoDBStreams;
	
	public DynamoDbLocalRule() {
		this(null);
	}
	
	public DynamoDbLocalRule(File file) {
		this.file = file;
	}
	
	
	@Override
	protected void before() throws Throwable {
		AmazonDynamoDBLocal amazonDynamoDBLocal = DynamoDBEmbedded.create(file);
		amazonDynamoDb = amazonDynamoDBLocal.amazonDynamoDB();
		amazonDynamoDBStreams = amazonDynamoDBLocal.amazonDynamoDBStreams();
	}
	
	@Override
	protected void after() {
		amazonDynamoDb = null;
		amazonDynamoDBStreams = null;
	}
	
	public AmazonDynamoDB getAmazonDynamoDB() {
		return amazonDynamoDb;
	}
	
	public AmazonDynamoDBStreams getAmazonDynamoDBStreams() {
		return amazonDynamoDBStreams;
	}
}
