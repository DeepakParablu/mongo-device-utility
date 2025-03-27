/*
 * Copyright 2025
 * ParaBlu Systems Private Limited
 * All Rights Reserved
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of ParaBlu Systems Private Limited and its suppliers, if any.  
 * The intellectual and technical concepts contained herein are proprietary 
 * to ParaBlu Systems Private Limited and its suppliers and may be covered by 
 * Indian, US and Foreign Patents, patents in process, and are protected by 
 * trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from ParaBlu Systems Private Limited.
 * 
 */

package com.parablu.mongo_device_utility.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.parablu.mongo_device_utility.utils.FileReaderUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MongoConnectionManager {

	private static final Logger logger = LoggerFactory.getLogger(MongoConnectionManager.class);

	private final Map<String, MongoTemplate> mongoTemplates = new ConcurrentHashMap<>();
	private final Map<String, String> connectionDetails;

	public MongoConnectionManager(ApplicationArguments args) throws Exception {

		// Default file path for connection details
		String filePath = "src/main/resources/connections.txt";

		if (args.containsOption("connection.file.path")) {
			filePath = args.getOptionValues("connection.file.path").get(0);
		}

		// Check if the file exists and is readable
		File file = new File(filePath);
		if (!file.exists() || !file.canRead()) {
			logger.info("Connection file {} does not exist or is not readable.", filePath);
			throw new IllegalArgumentException("Invalid connection file path.");
		}

		// Load connection details from the file
		this.connectionDetails = FileReaderUtil.readConnectionDetails(filePath);

		// Check if connection details are loaded properly
		if (this.connectionDetails.isEmpty()) {
			logger.info("No valid connection details found in file: {}", filePath);
			throw new IllegalArgumentException("No valid connection details found.");
		}

		logger.info("Connection details successfully loaded from file: {}", filePath);
	}

	public MongoTemplate getMongoTemplateForClient(String clientName) {
		return mongoTemplates.computeIfAbsent(clientName, key -> {
			try {

				String host = connectionDetails.get(clientName + ".host");
				String port = connectionDetails.get(clientName + ".port");
				String username = connectionDetails.get(clientName + ".username");
				String password = connectionDetails.get(clientName + ".password");
				String dbName = connectionDetails.get(clientName + ".db");

				if (host == null || port == null || username == null || password == null || dbName == null) {
					logger.info("Missing connection details for client: {}", clientName);
					throw new IllegalArgumentException("Missing connection details for client: " + clientName);
				}

				// Build connection URI
				String uri = String.format("mongodb://%s:%s@%s:%s/%s", username, password, host, port, dbName);

				ConnectionString connectionString = new ConnectionString(uri);
				MongoClient mongoClient = MongoClients.create(connectionString);

				return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, dbName));

			} catch (Exception e) {
				logger.info("Failed to create MongoTemplate for client: {}", clientName, e);
				throw new RuntimeException("Failed to create MongoTemplate for client: " + clientName, e);
			}
		});
	}
}
