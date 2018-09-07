package com.emergent.devicemgmt.api;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.domain.dto.DeviceAllocationDTO;
import com.emergent.devicemgmt.domain.exception.DeviceAlreadyAllocatedException;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @PutMapping(path="/device/{id}/allocate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
            Set<DeviceUserAssoc> deviceUserAssoc = device.get().getDeviceUserAssocSet();
            if(deviceUserAssoc.isEmpty()){
                // Device not allocated
                DeviceUserAssoc deviceUserAssoc1 = new DeviceUserAssoc();
                deviceUserAssoc1.setUser(user);
                deviceUserAssoc1.setDevice(device.get());
                deviceUserAssoc1.setAllocatedAt(new Date());
                deviceUserAssoc1.setAllocationInterval(deviceAllocationDTO.getAllocationInterval());
                deviceUserAssoc.add(deviceUserAssoc1);
                deviceService.save(deviceUserAssoc1);
                return ResponseEntity.ok().build();
            } else{
                // Device already allocated
                throw new DeviceAlreadyAllocatedException(uniqueId+" device is already allocated to some other user!");
            }
        } else {
            // Device not found raise exception
            throw new DeviceNotFoundException(uniqueId+" device not found. Please register this device first and try again!");
        }

    }


    @PutMapping(path="/device/{id}/deallocate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deallocateDevice(@PathVariable("id") String uniqueId){
        // Step 1 Retrieve logged in user information.
        User user = getLoggedInUser();

        // Step 2 check if device NOT allocated to any other user.
        Optional<Device> device =
                deviceService.getDeviceForUniqueId(uniqueId);
        return ResponseEntity.ok().build();

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
