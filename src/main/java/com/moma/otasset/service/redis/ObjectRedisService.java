package com.moma.otasset.service.redis;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ObjectRedisService {

	//------------string

	void set(String key, Object obj);

	void remove(String key);

	//批量删除
	Long removeSomeKeys(List<String> keys);

	//清库
    Long removeAll();

	Object get(String key);

	void set(String key, Object obj, int time);

	void set(String key, Object obj, int time, TimeUnit timeUnit);

	//---------------hash
	void setHashModel(String key, String HK, Object HV);

	void setHashModel(String key, String HK);

	void setHashMap(String key, Map<String, Object> map);

    void setHashMap(String key, Map<String, Object> map, Integer num, TimeUnit timeUnit);

	void deleteHashModel(String key, String price);

	Object getHashModel(String key, String hk);

	Object getHashModel(String key);

	Object getEntries(String key);

	//-----------set

	/**
	* @author YiYi
	* @Description 对有序的set操作
	* @Date  2019/5/9 17:20
	* @Param [key, value, score]
	* @return void
	**/
	void setSortSet(String key, String value, Double score);

	/**
	* @author YiYi
	* @Description 获取排序的set
	* @Date  2019/5/9 17:20
	* @Param [sort, key]
	* @return java.lang.Object
	**/
	Set<Object> getAllSortSet(String sort, String key);

	/**
	* @author YiYi
	* @Description 获取分页的排序的set
	* @Date  2019/5/9 17:20
	* @Param [sort, key, pageSize, pageNum]
	* @return java.lang.Object
	**/
	Set<Object> getAllSortSetByPage(String sort, String key, Integer pageSize, Integer pageNum);

	//获取某个值的某个排序
	Double getSortSetScor(String key, Object value);

	//获取某个排序范围的数据以及序号
	Set<Object> getZSetDataByScore(String sort, String key, double min, double max);

	//删除某些值
	Long removeSortSetByValue(String key, Object... values);


	//删除一定排序范围的数据,返回删除个数
	Long removeSortSetByScore(String key, Long min, Long max);

	//删除一定排序范围的数据,返回删除个数
	Long removeSortSetByIndex(String key, Long start, Long end);

	//合并两个set,注意相同值的score会相加,返回合并后的个数
	Long unionAndStore(String key, String otherKey, String destkey);

	//合并多个set,注意相同值的score会相加,返回合并后的个数
	//destkey,合并后的key
	Long unionAndStore(String key, List<String> otherKey, String destkey);

	//-----------------通用
	//设置过期时间
	Boolean expire(String key, long timeout, TimeUnit unit);

}


