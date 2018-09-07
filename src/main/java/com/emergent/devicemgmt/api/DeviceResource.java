package com.emergent.devicemgmt.api;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DeviceResource {
    @Autowired
    private DeviceService deviceService;
    @GetMapping(path="/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Device> devices(){
        return  deviceService.getAllDevices();
    }
    @GetMapping(path="/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Device> device(@PathVariable("id") long uniqueId){
        return  deviceService.getDeviceForUniqueId(String.valueOf(uniqueId));
    }
}
