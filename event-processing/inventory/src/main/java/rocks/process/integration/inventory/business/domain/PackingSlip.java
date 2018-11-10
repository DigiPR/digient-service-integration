/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.inventory.business.domain;

public class PackingSlip {
    private String packingSlipId;

    public PackingSlip(String packingSlipId) {
        this.packingSlipId = packingSlipId;
    }

    public String getPackingSlipId() {
        return packingSlipId;
    }

    public void setPackingSlipId(String packingSlipId) {
        this.packingSlipId = packingSlipId;
    }
}
