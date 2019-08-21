package com.moma.otasset.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository("objectRedisService")
public class ObjectRedisServiceImpl implements ObjectRedisService {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private org.springframework.data.redis.core.StringRedisTemplate StringRedisTemplate;

	@Override
	public void set(String key, Object obj) {
		redisTemplate.opsForValue().set(key, obj);
	}

	@Override
	public void remove(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public Long removeSomeKeys(List<String> keys) {
		return redisTemplate.delete(keys);
	}

    @Override
    public Long removeAll() {
        Set<String> keys = redisTemplate.keys("*");
        return redisTemplate.delete(keys);
    }

    @Override
	public Object get(String key) {
		Object obj = redisTemplate.opsForValue().get(key);
		return obj;
	}

	@Override
	public void set(String key, Object obj, int minute) {
		set(key, obj, minute,TimeUnit.MINUTES);
	}

	@Override
	public void set(String key, Object obj, int time,TimeUnit timeUnit) {
		try {
			redisTemplate.opsForValue().set(key, obj, time, timeUnit);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@Override
	public void setHashModel(String key,String HK, Object HV){
		redisTemplate.opsForHash().put(key,HK,HV);
	}

	public void setHashModel(String key,String HK){
		setHashModel(key,HK,HK);
	}

	public void setHashMap(String key, Map<String,Object> map){
		redisTemplate.opsForHash().putAll(key,map);
	}

    @Override
    public void setHashMap(String key, Map<String, Object> map, Integer num, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, num, timeUnit);
    }

	public void deleteHashModel(String key,String price){
		redisTemplate.opsForHash().delete(key,price);
	}

	@Override
	public Object getHashModel(String key,String hk){
		Object obj = redisTemplate.opsForHash().get(key,hk);
		return obj;
	}

	@Override
	public Object getHashModel(String key) {
        return redisTemplate.opsForHash().entries(key);
	}

	public Object getEntries(String key){
		Object obj = redisTemplate.opsForHash().entries(key);
		return obj;
	}

	@Override
	public void setSortSet(String key, String value, Double score) {
		redisTemplate.opsForZSet().add(key,value,score);
	}

	@Override
	public Set<Object> getAllSortSet(String sort, String key) {
		if(sort==null){
			sort = "asc";
		}
		if(sort.equals("asc")){
			//正序
			return redisTemplate.opsForZSet().range(key, 0, -1);
		}else{
			//倒叙
			return redisTemplate.opsForZSet().reverseRange(key,0,-1);
		}
	}

	@Override
	public Double getSortSetScor(String key,Object value) {
		return redisTemplate.opsForZSet().score(key,value);
	}

	@Override
	public Set<Object> getZSetDataByScore(String sort, String key, double min, double max) {
		if(sort==null){
			sort = "asc";
		}
		if(sort.equals("asc")) {
			//正序
			return redisTemplate.opsForZSet().rangeByScore(key, min, max);
		}else{
			//逆序
			return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
		}
	}


	@Override
	public Set<Object> getAllSortSetByPage(String sort, String key, Integer pageSize, Integer pageNum) {
		if(sort==null){
			sort = "asc";
		}
        //Long size = redisTemplate.opsForZSet().size(key);
        Long start= (pageNum-1)*pageSize.longValue();
		Long end= start+pageSize-1;
		if(sort.equals("asc")){
			//正序
			return redisTemplate.opsForZSet().range(key, start, end);
		}else{
			//倒叙
			return redisTemplate.opsForZSet().reverseRange(key,start,end);
		}
	}

	@Override
	public Long removeSortSetByValue(String key,Object...values) {
		return redisTemplate.opsForZSet().remove(key,values);
	}

	@Override
	public Long removeSortSetByScore(String key, Long min, Long max) {
		return redisTemplate.opsForZSet().removeRangeByScore(key, min.doubleValue(), max.doubleValue());
	}

	@Override
	public Long removeSortSetByIndex(String key, Long start, Long end) {
		return redisTemplate.opsForZSet().removeRange(key,start,end);
	}

	@Override
	public Long unionAndStore(String key, String otherKey, String destkey) {
		return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destkey);
	}

    @Override
    public Long unionAndStore(String key, List<String> otherKey, String destkey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destkey);
    }


	@Override
	public Boolean expire(String key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key,timeout,unit);
	}


}
