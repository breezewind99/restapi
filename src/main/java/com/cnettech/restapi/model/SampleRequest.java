package com.cnettech.restapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 요청 데이타
 */
public class SampleRequest {

    @Schema(description = "SampleField1", example = "SampleField1")
    public String cratDt;

    @Schema(description = "SampleField2", example = "SampleField2")
    public int cratSeq;

}
