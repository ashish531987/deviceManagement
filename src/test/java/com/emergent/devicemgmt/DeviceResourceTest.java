package com.emergent.devicemgmt;

import com.emergent.devicemgmt.api.DeviceResource;
import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.domain.dto.DeviceAllocationDTO;
import com.emergent.devicemgmt.service.DeviceService;
import com.emergent.devicemgmt.service.UserDetailsImpl;
import com.emergent.devicemgmt.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DeviceResource.class, secure = false)
public class DeviceResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DeviceService deviceService;

    @MockBean
    UserService userService;

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    List<Device> mockDeviceList = new ArrayList<>();
    User mockUser1 = new User();
    User mockUser2 = new User();
    DeviceUserAssoc mockDeviceUserAssoc = new DeviceUserAssoc();
    Set<DeviceUserAssoc> allocatedToUsersAssocSet = new HashSet<>();

    @Before
    public void setup() {
        mockUser1.setEmail("ashish@anoosmar.com");
        mockUser1.setPassword("pass@123");

        mockUser2.setEmail("mangesh@vaultize.com");
        mockUser2.setPassword("vault@123");

        Device device1 = new Device();
        device1.setId(7L);
        device1.setDeviceName("Mock device 1");
        device1.setUniqueId("123456789");
        mockDeviceList.add(device1);

        Device device2 = new Device();
        device2.setId(8L);
        device2.setDeviceName("Mock device 2");
        device2.setUniqueId("987654321");
        mockDeviceList.add(device2);
    }


    @Test
    public void verifySuccessDeviceList() throws Exception{

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);

        Mockito.when(deviceService.getAllDevices()).thenReturn(mockDeviceList);

        mockMvc.perform(MockMvcRequestBuilders.get(DeviceResource.REST_GET_DEVICES).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(mockDeviceList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(mockDeviceList.get(1).getId().intValue())))
                .andExpect(jsonPath("$.[0].deviceName", is(mockDeviceList.get(0).getDeviceName())))
                .andExpect(jsonPath("$.[1].deviceName", is(mockDeviceList.get(1).getDeviceName())))
                .andExpect(jsonPath("$.[0].uniqueId", is(mockDeviceList.get(0).getUniqueId())))
                .andExpect(jsonPath("$.[1].uniqueId", is(mockDeviceList.get(1).getUniqueId())))
                .andDo(print());
    }

    @Test
    public void verifySuccessGetDevice() throws Exception{

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);

        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get(DeviceResource.REST_GET_DEVICE, mockDeviceList.get(0).getUniqueId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(mockDeviceList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.deviceName", is(mockDeviceList.get(0).getDeviceName())))
                .andExpect(jsonPath("$.uniqueId", is(mockDeviceList.get(0).getUniqueId())))
                .andDo(print());
    }
    @Test
    public void verifyNoDeviceWhenGetDevice() throws Exception{

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);

        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get(DeviceResource.REST_GET_DEVICE, mockDeviceList.get(1).getUniqueId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print());
    }
    @Test
    public void verifySuccessAllocateDevice() throws Exception{

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_ALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(deviceAllocationDTO))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void verifySuccessAllocateDeviceAlreadyAllocatedSameUser() throws Exception{
        // Mockdata
        Device device = mockDeviceList.remove(0);
        mockDeviceUserAssoc.setUser(mockUser1);
        mockDeviceUserAssoc.setDevice(device);
        mockDeviceUserAssoc.setAllocatedAt(new Date());
        mockDeviceUserAssoc.setAllocationInterval(5000L);
        allocatedToUsersAssocSet.add(mockDeviceUserAssoc);
        device.setAllocatedToUsersAssocSet(allocatedToUsersAssocSet);
        mockDeviceList.add(0, device);

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_ALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(deviceAllocationDTO))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void verifyAllocateDeviceAlreadyAllocated() throws Exception{
        // Mockdata
        Device device = mockDeviceList.remove(0);
        mockDeviceUserAssoc.setUser(mockUser2);
        mockDeviceUserAssoc.setDevice(device);
        mockDeviceUserAssoc.setAllocatedAt(new Date());
        mockDeviceUserAssoc.setAllocationInterval(5000L);
        allocatedToUsersAssocSet.add(mockDeviceUserAssoc);
        device.setAllocatedToUsersAssocSet(allocatedToUsersAssocSet);
        mockDeviceList.add(0, device);

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_ALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(deviceAllocationDTO))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    public void verifyAllocateDeviceNotFound() throws Exception{
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(null);

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_ALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(new ObjectMapper().writeValueAsString(deviceAllocationDTO))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void verifySuccessDeallocateDevice() throws Exception{
        // Mockdata
        Device device = mockDeviceList.remove(0);
        mockDeviceUserAssoc.setUser(mockUser1);
        mockDeviceUserAssoc.setDevice(device);
        mockDeviceUserAssoc.setAllocatedAt(new Date());
        mockDeviceUserAssoc.setAllocationInterval(5000L);
        allocatedToUsersAssocSet.add(mockDeviceUserAssoc);
        device.setAllocatedToUsersAssocSet(allocatedToUsersAssocSet);
        mockDeviceList.add(0, device);

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_DEALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void verifySuccessDeallocateDeviceAlreadyAllocated() throws Exception{
        // Mockdata
        Device device = mockDeviceList.remove(0);
        mockDeviceUserAssoc.setUser(mockUser2);
        mockDeviceUserAssoc.setDevice(device);
        mockDeviceUserAssoc.setAllocatedAt(new Date());
        mockDeviceUserAssoc.setAllocationInterval(5000L);
        allocatedToUsersAssocSet.add(mockDeviceUserAssoc);
        device.setAllocatedToUsersAssocSet(allocatedToUsersAssocSet);
        mockDeviceList.add(0, device);

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_DEALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void verifySuccessDeallocateDeviceNotFound() throws Exception{
        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser1);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(mockUser1));
        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(null);

        DeviceAllocationDTO deviceAllocationDTO = new DeviceAllocationDTO();
        deviceAllocationDTO.setAllocationInterval(5000L);

        mockMvc.perform(MockMvcRequestBuilders
                .put(DeviceResource.REST_DEALLOCATE_DEVICE, mockDeviceList.get(0).getUniqueId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
