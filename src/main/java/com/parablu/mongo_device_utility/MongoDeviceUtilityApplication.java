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

package com.parablu.mongo_device_utility;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.parablu.mongo_device_utility.service.DeviceService;

@SpringBootApplication
public class MongoDeviceUtilityApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MongoDeviceUtilityApplication.class);

	@Autowired
	private DeviceService deviceService;

	public static void main(String[] args) {
		SpringApplication.run(MongoDeviceUtilityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Starting MongoDeviceUtilityApplication...");

		// Retrieve the connection file path from the command-line arguments
		String connectionFilePath = null;
		for (String arg : args) {
			if (arg.startsWith("--connection.file.path=")) {
				connectionFilePath = arg.split("=")[1];
			}
		}

		if (connectionFilePath == null || connectionFilePath.trim().isEmpty()) {
			logger.info(
					"Connection file path is not provided. Please provide a valid file path using --connection.file.path argument.");
			throw new IllegalArgumentException("Connection file path is required.");
		}

		File connectionFile = new File(connectionFilePath);
		if (!connectionFile.exists()) {
			logger.info("Connection file does not exist at path: {}", connectionFilePath);
			throw new IllegalArgumentException("Connection file not found.");
		}

		if (!connectionFile.canRead()) {
			logger.info("Connection file at path {} is not readable.", connectionFilePath);
			throw new IllegalArgumentException("Cannot read the connection file.");
		}

		logger.info("Connection file path validated: {}", connectionFilePath);

		// Proceed with syncing the data
		try {
			deviceService.syncDevicesData(connectionFilePath);
		} catch (Exception e) {
			logger.info("An error occurred during the sync process.", e);
		}
	}
}
