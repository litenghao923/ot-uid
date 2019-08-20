package com.moma.otasset.service;

import java.math.BigDecimal;
import java.util.List;

public interface AssetService {

    List<String> getAllUserData();

    //充值
    String rechargeByUid(Long uid, BigDecimal amount);

    //提现
    String withdrawByUid(Long uid, BigDecimal amount);

    Integer addUser(Long uid, String nickName);
}
