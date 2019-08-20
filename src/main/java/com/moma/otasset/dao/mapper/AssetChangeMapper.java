package com.moma.otasset.dao.mapper;

import com.moma.otasset.dao.domain.AssetChange;
import com.moma.otasset.dao.domain.AssetChangeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AssetChangeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    long countByExample(AssetChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int deleteByExample(AssetChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int insert(AssetChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int insertSelective(AssetChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    List<AssetChange> selectByExample(AssetChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    AssetChange selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AssetChange record, @Param("example") AssetChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AssetChange record, @Param("example") AssetChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AssetChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table asset_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AssetChange record);
}