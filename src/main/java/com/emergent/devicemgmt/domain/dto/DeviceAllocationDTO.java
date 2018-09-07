package com.emergent.devicemgmt.domain.dto;

public class DeviceAllocationDTO extends AbstractEmergentDTO {
    @InRange(min=5000, max = 50000, message = "Allocation interval is invalid!")
    private Long allocationInterval;

    public Long getAllocationInterval() {
        return allocationInterval;
    }

    public void setAllocationInterval(Long allocationInterval) {
        this.allocationInterval = allocationInterval;
    }
}
