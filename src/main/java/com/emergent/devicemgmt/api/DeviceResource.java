package com.emergent.devicemgmt.api;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.domain.dto.DeviceAllocationDTO;
import com.emergent.devicemgmt.domain.exception.DeviceAllocatedException;
import com.emergent.devicemgmt.domain.exception.DeviceNotFoundException;
import com.emergent.devicemgmt.domain.exception.ExceptionResponse;
import com.emergent.devicemgmt.service.DeviceService;
import com.emergent.devicemgmt.service.UserDetailsImpl;
import com.emergent.devicemgmt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RestController

public class DeviceResource {
    public static final String REST_GET_DEVICES = "/devices";
    public static final String REST_GET_DEVICE = "/device/{id}";
    public static final String REST_ALLOCATE_DEVICE = REST_GET_DEVICE+"/allocate";
    public static final String REST_DEALLOCATE_DEVICE = REST_GET_DEVICE+"/deallocate";
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @GetMapping(path= REST_GET_DEVICES, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> devices(){
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping(path= REST_GET_DEVICE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Optional<Device> device(@PathVariable("id") String uniqueId){
        Optional<Device> deviceOptional = deviceService.getDeviceForUniqueId(uniqueId);
        if(deviceOptional.isPresent())
            return deviceOptional;
        else
            throw new DeviceNotFoundException(uniqueId);
    }

    @PutMapping(path= REST_ALLOCATE_DEVICE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> allocateDevice(
            @NotBlank @PathVariable("id") String uniqueId,
            @Valid @RequestBody DeviceAllocationDTO deviceAllocationDTO){

        // Step 1 Retrieve logged in user information.
        User user = getLoggedInUser();

        // Step 2 check if device NOT allocated to any other user.
        Optional<Device> device =
                deviceService.getDeviceForUniqueId(uniqueId);

        if(device != null && device.isPresent()){
            // Device found
            Set<DeviceUserAssoc> deviceUserAssoc = device.get().getAllocatedToUsersAssocSet();
            if(deviceUserAssoc.isEmpty()){
                // Device not allocated to any user
                DeviceUserAssoc deviceUserAssoc1 = new DeviceUserAssoc();
                deviceUserAssoc1.setUser(user);
                deviceUserAssoc1.setDevice(device.get());
                deviceUserAssoc1.setAllocatedAt(new Date());
                deviceUserAssoc1.setAllocationInterval(deviceAllocationDTO.getAllocationInterval());
                deviceUserAssoc.add(deviceUserAssoc1);
                deviceService.save(deviceUserAssoc1);
                return ResponseEntity.ok().build();
            } else{
                // Check if device is allocated to current user
                for(DeviceUserAssoc deviceUserAssoc1 : deviceUserAssoc){
                    if(deviceUserAssoc1.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
                        // Allocation of this device to this user has been found
                        deviceUserAssoc1.setAllocationInterval(deviceAllocationDTO.getAllocationInterval());
                        deviceUserAssoc1.setAllocatedAt(new Date());
                        deviceUserAssoc1.setDeallocatedAt(null);
                        deviceService.save(deviceUserAssoc1);
                        return ResponseEntity.ok().build();
                    }
                }
                // Device already allocated
                throw new DeviceAllocatedException(uniqueId);
            }
        } else {
            // Device not found raise exception
            throw new DeviceNotFoundException(uniqueId);
        }

    }


    @PutMapping(path=REST_DEALLOCATE_DEVICE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> deallocateDevice(@NotBlank @PathVariable("id") String uniqueId){
        // Step 1 Retrieve logged in user information.
        User user = getLoggedInUser();

        // Step 2 check if device NOT allocated to any other user.
        Optional<Device> device =
                deviceService.getDeviceForUniqueId(uniqueId);
        if(device != null && device.isPresent()){
            // Device found
            Set<DeviceUserAssoc> deviceUserAssoc = device.get().getAllocatedToUsersAssocSet();
            for(DeviceUserAssoc deviceUserAssoc1 : deviceUserAssoc){
                if(deviceUserAssoc1.getUser().getEmail().equalsIgnoreCase(user.getEmail())){
                    // Allocation of this device to this user has been found
                    deviceUserAssoc1.setDeallocatedAt(new Date());
                    deviceService.save(deviceUserAssoc1);
                    return ResponseEntity.ok().build();
                }
            }
            // Device already allocated
            throw new DeviceAllocatedException(uniqueId);
        } else {
            // Device not found raise exception
            throw new DeviceNotFoundException(uniqueId);
        }
    }
    /**
     * User instance of currently logged in user.
     * @return User object of currenty logged in User.
     */
    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl customUser = (UserDetailsImpl)authentication.getPrincipal();
        return userService.findByEmail(customUser.getUsername());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {

        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Request Validation error",
                errorMsg);

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
