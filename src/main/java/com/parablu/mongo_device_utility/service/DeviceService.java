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

package com.parablu.mongo_device_utility.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.parablu.mongo_device_utility.config.MongoConnectionManager;
import com.parablu.mongo_device_utility.model.Device;
import com.parablu.mongo_device_utility.model.DeviceBackupOverview;

@Service
public class DeviceService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	private MongoConnectionManager connectionManager;

	// Counters
	private int totalUpdatedDevices = 0;
	private int totalUpdatedBlockedFields = 0;
	private int totalUpdatedDeletedFields = 0;

	public void syncDevicesData(String connectionFilePath) {
		long startTime = System.currentTimeMillis();
		logger.info("STARTED: Syncing devices data with connection file:{}", connectionFilePath);

		try {
			MongoTemplate mongoTemplate = connectionManager.getMongoTemplateForClient("mongotemplate");
			
			List<Device> devices = mongoTemplate.findAll(Device.class);

			if (devices.isEmpty()) {
				logger.error("No devices found in the DEVICE collection.");
				return;
			} 
			
			devices.forEach(device -> {
				// Query the DEVICE_BACKUP_OVERVIEW collection for the specific deviceUUID
				Query query = new Query(Criteria.where("deviceUUID").is(device.getDeviceUUID()));
				DeviceBackupOverview backupOverview = mongoTemplate.findOne(query, DeviceBackupOverview.class);
				
				if (backupOverview == null) {
					logger.error("No backup overview found for device UUID: {}", device.getDeviceUUID());
					return;
				}
				
				Update update = new Update();
				boolean updated = false;

				// Update only relevant fields if necessary
				if (device.isBlocked() != backupOverview.isDeviceBolcked()) {
					update.set("isDeviceBolcked", device.isBlocked());
					updated = true;
					logger.info(
					    "Updated DEVICE_BACKUP_OVERVIEW for UUID: {} | isDeviceBlocked field updated from: {} to: {} (in DEVICE: {})",
					    device.getDeviceUUID(),
					    backupOverview.isDeviceBolcked(),    // Previous value in DEVICE_BACKUP_OVERVIEW
					    device.isBlocked(),          // Updated value from DEVICE
					    device.isBlocked()           // Showing the updated value again for clarity
					);
				}

				if (device.isDeleted() != backupOverview.isDeviceDeleted()) {
					update.set("isDeviceDeleted", device.isDeleted());
					updated = true;
					logger.info(
					    "Updated DEVICE_BACKUP_OVERVIEW for UUID: {} | isDeviceDeleted field updated from: {} to: {} (in DEVICE: {})",
					    device.getDeviceUUID(),
					    backupOverview.isDeviceDeleted(),
					    device.isDeleted(),
					    device.isDeleted()
					);
				}

				// Update the database only if there are changes
				if (updated) {
					// Update the relevant device backup overview record
					mongoTemplate.updateFirst(query, update, DeviceBackupOverview.class);
					
					// After updating the database, update the counters
					if (device.isBlocked() != backupOverview.isDeviceBolcked()) {
						totalUpdatedBlockedFields++;
					}
					if (device.isDeleted() != backupOverview.isDeviceDeleted()) {
						totalUpdatedDeletedFields++;
					}
					totalUpdatedDevices++;
				}
			});

		} catch (Exception e) {
			logger.error("Error occurred while syncing data", e);
		}
		long endTime = System.currentTimeMillis();
		logger.info("COMPLETED: Syncing devices data | Time taken: {} seconds", (endTime - startTime) / 1000);
		logger.info("Total devices updated: {}", totalUpdatedDevices);
		logger.info("Total fields updated, Blocked: {} and Deleted: {}", totalUpdatedBlockedFields, totalUpdatedDeletedFields);
	}
}