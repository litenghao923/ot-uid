package com.moma.otasset.dao.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AssetChangeVo {

    private Long id;

    private Long huobiUid;

    private String nickName;

    private Integer operationType;

    private BigDecimal amount;

    private BigDecimal bcoinAssetChange;

    private BigDecimal bcoinAsset;

    private String cTime;

}
