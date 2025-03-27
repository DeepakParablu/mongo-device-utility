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

package com.parablu.mongo_device_utility.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "DEVICE_BACKUP_OVERVIEW")
public class DeviceBackupOverview {
    
    @Id
    private String id;
    private String deviceUUID;
    private boolean isDeviceBolcked;
    private boolean isDeviceDeleted;
}