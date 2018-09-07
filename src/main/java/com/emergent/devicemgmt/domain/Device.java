package com.emergent.devicemgmt.domain;

import javax.persistence.*;

@Entity
@Table(name="devices")
public class Device {
    @Id
    @GeneratedValue
    private Long id;

    private String deviceName;

    @Column(unique = true, nullable = false)
    private String uniqueId; // Set at the time of registration.

    public Device() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
