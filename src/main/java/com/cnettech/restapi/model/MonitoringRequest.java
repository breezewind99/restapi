package com.cnettech.restapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class MonitoringRequest {

    @Schema(description = "SystemCode", example = "SystemCode")
    public String SystemCode;

    @Schema(description = "SystemName", example = "SystemName")
    public String SystemName;

    @Schema(description = "CPU", example = "CPU")
    public int CPU;

    @Schema(description = "MEM", example = "MEM")
    public int MEM;

    @Schema(description = "HDD", example = "HDD")
    public String HDD;

    @Schema(description = "PROCESS", example = "PROCESS")
    public String PROCESS;
}