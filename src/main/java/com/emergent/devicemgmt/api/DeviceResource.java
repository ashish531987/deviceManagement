package com.emergent.devicemgmt.api;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.domain.exception.DeviceAlreadyAllocatedException;
import com.emergent.devicemgmt.domain.exception.DeviceNotFoundException;
import com.emergent.devicemgmt.repository.DeviceUserAssocJpaRepository;
import com.emergent.devicemgmt.service.DeviceService;
import com.emergent.devicemgmt.service.UserDetailsImpl;
import com.emergent.devicemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class DeviceResource {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @GetMapping(path="/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Device> devices(){
        return  deviceService.getAllDevices();
    }

    @GetMapping(path="/device/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Device> device(@PathVariable("id") String uniqueId){
        Optional<Device> deviceOptional = deviceService.getDeviceForUniqueId(uniqueId);
        if(deviceOptional.isPresent())
            return deviceOptional;
        else
            throw new DeviceNotFoundException(uniqueId);
    }

    @PostMapping(path="/device/{id}/allocate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> allocateDevice(@PathVariable("id") String uniqueId,
                                                 @RequestParam Map<String, Object> payload){
        System.out.println(payload);
        // Step 1 Retrieve logged in user information.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl customUser = (UserDetailsImpl)authentication.getPrincipal();
        User user = userService.findByEmail(customUser.getUsername());

        // Step 2 check if device NOT allocated to any other user.
        Optional<Device> device =
                deviceService.getDeviceForUniqueId(uniqueId);

        if(device != null && device.isPresent()){
            // Device found
            Set<DeviceUserAssoc> deviceUserAssoc = device.get().getDeviceUserAssocSet();
            if(deviceUserAssoc.isEmpty()){
                // Device not allocated
                DeviceUserAssoc deviceUserAssoc1 = new DeviceUserAssoc();
                deviceUserAssoc1.setUser(user);
                deviceUserAssoc1.setDevice(device.get());
                deviceUserAssoc1.setAllocatedAt(new Date());
                deviceUserAssoc.add(deviceUserAssoc1);
                deviceService.save(deviceUserAssoc1);
                return ResponseEntity.accepted().build();
            } else{
                // Device already allocated
                throw new DeviceAlreadyAllocatedException(uniqueId);
            }
        } else {
            // Device not found raise exception
            throw new DeviceNotFoundException(uniqueId);
        }

    }
}
