package com.moma.otasset.service;

import com.moma.otasset.dao.domain.AssetChange;
import com.moma.otasset.dao.domain.AssetUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AssetService {

    List<String> getAllUserData();

    //充值
    String rechargeByUid(Long uid, BigDecimal amount);

    //提现
    String withdrawByUid(Long uid, BigDecimal amount);

    Integer addUser(Long uid, String nickName);

    List<AssetChange> getAssetChangeByPage(Integer pageNum, Integer pageSize);

    Map<String, BigDecimal> getAccountOverview();

    //充值提现
    String biCoinToTeam(BigDecimal amount, Integer type);

    List<AssetUser> getAssetUserByPage(Integer pageNum, Integer pageSize);
}
