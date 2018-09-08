package com.emergent.devicemgmt;

import com.emergent.devicemgmt.api.DeviceResource;
import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.service.DeviceService;
import com.emergent.devicemgmt.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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

    List<Device> mockDeviceList = new ArrayList<>();
    User mockUser = new User();


    @Before
    public void setup() {
        mockUser.setEmail("ashish@anoosmar.com");
        mockUser.setPassword("pass@123");

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

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser);

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

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser);

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

        Mockito.when(userService.findByEmail(Mockito.anyString())).thenReturn(mockUser);

        Mockito.when(deviceService.getDeviceForUniqueId(mockDeviceList.get(0).getUniqueId())).thenReturn(java.util.Optional.ofNullable(mockDeviceList.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get(DeviceResource.REST_GET_DEVICE, mockDeviceList.get(1).getUniqueId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print());
    }
}
