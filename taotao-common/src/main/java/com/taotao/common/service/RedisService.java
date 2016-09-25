package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/*
 * 封装redisService然后在ItemCatService 中添加缓存逻辑，以便在apiService 中调用
 * */

@Service
public class RedisService {
    
    @Autowired(required=false)//若spring容器中有对象就注入没有就忽略
    private ShardedJedisPool shardedJedisPool;
        
    private<T> T execute(Function<T, ShardedJedis> fun){
        ShardedJedis shardedJedis = null;
        try{
            //从连接池中获取jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        }catch(Exception e){
                e.printStackTrace();
        }finally{
            if(null != shardedJedis){
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }
    /*
     * 执行set操作
     * */
    public String set(final String key,final String value){
        return this.execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis e) {
                return e.set(key, value);
            }
        });
    }
    /*
     * 执行get操作
     * */
    public String get(final String key){
        return this.execute(new Function<String,ShardedJedis>(){

            @Override
            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }
    //执行DEL操作
    public Long del(final String key){
        return this.execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis e) {
                return e.del(key);
            }
        });
    }
    //设置生存时间
    public Long expire(final String key,final Integer seconds){
        return this.execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis e) {
                return e.expire(key, seconds);
            }
            
        });
    }

    /**
     * 执行set操作并且设置生存时间，单位为秒
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                String str = e.set(key, value);
                e.expire(key, seconds);
                return str;
            }
        });
    }

}
