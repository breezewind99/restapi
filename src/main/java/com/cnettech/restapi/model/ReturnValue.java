package com.cnettech.restapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReturnValue {
    @Schema(description = "오류코드" , example = "0")
    public int Error_Code;
    @Schema(description = "오류내용" , example = "오류내용")
    public String Error_Description;
}
