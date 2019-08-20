package com.moma.otasset.service.impl;

import com.github.pagehelper.PageHelper;
import com.moma.otasset.dao.domain.AssetChange;
import com.moma.otasset.dao.domain.AssetChangeExample;
import com.moma.otasset.dao.domain.AssetUser;
import com.moma.otasset.dao.domain.AssetUserExample;
import com.moma.otasset.dao.mapper.AssetChangeMapper;
import com.moma.otasset.dao.mapper.AssetUserMapper;
import com.moma.otasset.dao.mapper.ext.AssetChangeMapperExt;
import com.moma.otasset.service.AssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    @Resource
    AssetUserMapper assetUserMapper;

    @Resource
    AssetChangeMapper assetChangeMapper;

    @Resource
    AssetChangeMapperExt assetChangeMapperExt;

    @Override
    public List<String> getAllUserData() {
        AssetUserExample assetUserExample = new AssetUserExample();
        List<AssetUser> assetUsers = assetUserMapper.selectByExample(assetUserExample);
        if (assetUsers != null && assetUsers.size() > 0) {
            List<String> collect = assetUsers.stream().map(o -> o.getNickName() + " (UID" + o.getHuobiUid() + ")").collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    @Transactional
    public String rechargeByUid(Long uid, BigDecimal amount) {
        AssetUserExample assetUserExample = new AssetUserExample();
        assetUserExample.or().andHuobiUidEqualTo(uid);
        List<AssetUser> assetUsers = assetUserMapper.selectByExample(assetUserExample);
        BigDecimal bicoinAmount = assetChangeMapperExt.sumBicoinAmount();
        if (assetUsers != null && assetUsers.size() > 0) {
            BigDecimal asset = assetUsers.get(0).getAsset();
            if (asset == null) {
                asset = BigDecimal.ZERO;
            }
            AssetUser assetUser = new AssetUser();
            assetUser.setAsset(asset.add(amount));
            assetUser.setId(assetUsers.get(0).getId());
            if (asset.add(amount).compareTo(new BigDecimal(500000)) > 0) {
                return "资产充值金额超限!";
            }
            int i = assetUserMapper.updateByPrimaryKey(assetUser);
            if (i > 0) {
                String s = addAssetChange(assetUsers.get(0), amount, bicoinAmount, 1);
                return "充值" + s;
            } else {
                return "充值失败";
            }
        }
        return "用户不存在";
    }

    @Override
    public String withdrawByUid(Long uid, BigDecimal amount) {
        AssetUserExample assetUserExample = new AssetUserExample();
        assetUserExample.or().andHuobiUidEqualTo(uid);
        List<AssetUser> assetUsers = assetUserMapper.selectByExample(assetUserExample);
        BigDecimal bicoinAmount = assetChangeMapperExt.sumBicoinAmount();
        if (assetUsers != null && assetUsers.size() > 0) {
            BigDecimal asset = assetUsers.get(0).getAsset();
            if (asset == null) {
                return "资产为零，无法进行提现操作!";
            }
            AssetUser assetUser = new AssetUser();
            if (asset.subtract(amount).compareTo(BigDecimal.ZERO) == -1) {
                return "提现资金大于所有资产，提现失败!";
            }
            assetUser.setAsset(asset.subtract(amount));
            assetUser.setId(assetUsers.get(0).getId());
            assetUser.setuTime(new Date());
            int i = assetUserMapper.updateByPrimaryKey(assetUser);
            if (i > 0) {
                String s = addAssetChange(assetUsers.get(0), amount, bicoinAmount, 2);
                return "提现" + s;
            } else {
                return "提现失败";
            }
        }
        return "用户不存在";
    }

    @Override
    public Integer addUser(Long uid, String nickName) {
        AssetUser assetUser = new AssetUser();
        assetUser.setNickName(nickName);
        assetUser.setHuobiUid(uid);
        assetUser.setAsset(BigDecimal.ZERO);
        assetUser.setcTime(new Date());
        assetUser.setuTime(new Date());
        int insert = assetUserMapper.insert(assetUser);
        return insert;
    }

    @Override
    public List<AssetChange> getAssetChangeByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        AssetChangeExample assetChangeExample = new AssetChangeExample();
        assetChangeExample.setOrderByClause("c_time desc");
        List<AssetChange> assetChanges = assetChangeMapper.selectByExample(null);
        if (assetChanges != null && assetChanges.size() > 0) {
            return assetChanges;
        }
        return null;
    }

    @Override
    public Map<String, BigDecimal> getAccountOverview() {
        BigDecimal allAmount = assetChangeMapperExt.sumAllAmount();
        BigDecimal bicoinAmount = assetChangeMapperExt.sumBicoinAmount();
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("用户总资金", allAmount == null ? BigDecimal.ZERO : allAmount);
        map.put("币coin出资", bicoinAmount == null ? BigDecimal.ZERO : bicoinAmount);
        return map;
    }

    @Override
    public String biCoinToTeam(BigDecimal amount, Integer type) {
        AssetChange assetChange = new AssetChange();
        assetChange.setHuobiUid(88888888L);
        assetChange.setNickName("币coin资金转账");
        BigDecimal bicoinAmount = assetChangeMapperExt.sumBicoinAmount();
        if (type == 1) {
            assetChange.setAmount(amount);
            assetChange.setBcoinAsset(bicoinAmount.subtract(amount));
            assetChange.setAmount(amount.negate());
        } else {
            assetChange.setAmount(amount);
            assetChange.setBcoinAsset(bicoinAmount.add(amount));
            assetChange.setBcoinAssetChange(amount);
        }

        assetChange.setOperationType(type);
        assetChange.setcTime(new Date());
        int insert = assetChangeMapper.insert(assetChange);
        if (insert > 0) {
            return "转账成功";
        }
        return "转账失败";
    }

    @Override
    public List<AssetUser> getAssetUserByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AssetUser> assetUsers = assetUserMapper.selectByExample(null);
        return assetUsers;
    }

    @Override
    public Integer getMaxPage(Integer type, Integer pageSize) {
        //1uid表，2资金变动表
        if (type == 1) {
            Integer integer = assetChangeMapperExt.countAssetUser();
            if (integer != null) {
                return (integer - 1) / pageSize + 1;
            }
        } else {
            Integer integer = assetChangeMapperExt.countAssetChange();
            if (integer != null) {
                return (integer - 1) / pageSize + 1;
            }
        }
        return null;
    }

    private String addAssetChange(AssetUser assetUser, BigDecimal amount, BigDecimal asset, Integer type) {
        AssetChange assetChange = new AssetChange();
        assetChange.setHuobiUid(assetUser.getHuobiUid());
        assetChange.setNickName(assetUser.getNickName());
        if (type == 1) {
            assetChange.setAmount(amount);
            assetChange.setBcoinAsset(asset.add(amount));
            assetChange.setBcoinAssetChange(amount);
        } else {
            assetChange.setAmount(amount);
            assetChange.setBcoinAsset(asset.subtract(amount));
            assetChange.setAmount(amount.negate());
        }

        assetChange.setOperationType(type);
        assetChange.setcTime(new Date());
        int insert = assetChangeMapper.insert(assetChange);
        if (insert > 0) {
            return "成功";
        }
        return "失败";
    }
}
