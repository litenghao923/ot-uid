package com.moma.otasset.dao.mapper.ext;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface AssetChangeMapperExt {

    @Select("select SUM(asset) FROM asset_user")
    BigDecimal sumAllAmount();

    @Select("SELECT bcoin_asset FROM asset_change ORDER BY c_time desc limit 1")
    BigDecimal sumBicoinAmount();

}
