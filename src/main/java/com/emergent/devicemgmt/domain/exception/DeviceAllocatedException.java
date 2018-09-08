package com.emergent.devicemgmt.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeviceAllocatedException extends RuntimeException {
    public DeviceAllocatedException(String deviceId) {
        super(deviceId+" device allocation exception!");
    }
}
