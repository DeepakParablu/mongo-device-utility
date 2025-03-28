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

package com.parablu.mongo_device_utility.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReaderUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);

	public static Map<String, String> readConnectionDetails(String filePath) throws IOException {
		Map<String, String> connectionDetails = new HashMap<>();

		try {
			Files.lines(Paths.get(filePath)).forEach(line -> {
				if (!line.trim().isEmpty() && line.contains("=")) {
					String[] parts = line.split("=", 2);
					if (parts.length == 2) {
						connectionDetails.put(parts[0].trim(), parts[1].trim());
					} else {
						logger.error("Invalid line format in connection file: {}", line);
					}
				}
			});
		} catch (IOException e) {
			logger.error("Failed to read connection details from file: {}", filePath, e);
			throw e;
		}

		return connectionDetails;
	}
}
