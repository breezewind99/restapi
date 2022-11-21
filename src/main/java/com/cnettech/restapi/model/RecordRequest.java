package com.cnettech.restapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 요청 데이타
 */
public class RecordRequest {

    @Schema(description = "내선번호", example = "내선번호")
    public String DN;

    @Schema(description = "유저ID", example = "유저ID")
    public String USERID;

    @Schema(description = "유저명", example = "유저명")
    public String USERNM;

    @Schema(description = "전화번호", example = "전화번호")
    public String PHONE;

    @Schema(description = "녹취키값", example = "녹취키값")
    public String KEY;

    @Schema(description = "인아웃", example = "인아웃")
    public String INOUT;

    @Schema(description = "필드1", example = "필드1")
    public String DATA1;

    @Schema(description = "필드2", example = "필드2")
    public String DATA2;

    @Schema(description = "필드3", example = "필드3")
    public String DATA3;

    @Schema(description = "필드4", example = "필드4")
    public String DATA4;

    @Schema(description = "필드5", example = "필드5")
    public String DATA5;

    @Schema(description = "필드6", example = "필드6")
    public String DATA6;

    @Schema(description = "필드7", example = "필드7")
    public String DATA7;

    @Schema(description = "필드8", example = "필드8")
    public String DATA8;

    @Schema(description = "필드9", example = "필드9")
    public String DATA9;
    
    @Schema(description = "필드10", example = "필드10")
    public String DATA10;
}
