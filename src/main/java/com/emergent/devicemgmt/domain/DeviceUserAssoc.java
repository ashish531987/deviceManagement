package com.emergent.devicemgmt.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devices_users_assoc")
public class DeviceUserAssoc {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    private User user;

    @ManyToOne(cascade=CascadeType.ALL)
    private Device device;

    @Temporal(TemporalType.TIMESTAMP)
    private Date allocatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deallocatedAt;

    private long allocationInterval;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getAllocatedAt() {
        return allocatedAt;
    }

    public void setAllocatedAt(Date allocatedAt) {
        this.allocatedAt = allocatedAt;
    }

    public Date getDeallocatedAt() {
        return deallocatedAt;
    }

    public void setDeallocatedAt(Date deallocatedAt) {
        this.deallocatedAt = deallocatedAt;
    }

    public long getAllocationInterval() {
        return allocationInterval;
    }

    public void setAllocationInterval(long allocationInterval) {
        this.allocationInterval = allocationInterval;
    }
}
