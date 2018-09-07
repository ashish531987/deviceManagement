package com.emergent.devicemgmt.api;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeviceResource {
    @Autowired
    private DeviceService deviceService;
    @GetMapping(path="/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Device> devices(){
        return  deviceService.getAllDevices();
    }
    @GetMapping(path="/devices/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Device device(@PathVariable Long deviceId){
        return  deviceService.getDeviceForId(deviceId);
    }
    @GetMapping(path="/devices/{uniqueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Device device(@PathVariable String uniqueId){
        return  deviceService.getDeviceForUniqueId(uniqueId);
    }
}
