/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 





package com.jinbooks.persistence.redis.connection;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jinbooks.persistence.redis.IRedisConnection;
import com.jinbooks.persistence.redis.IRedisStatement;
import com.jinbooks.persistence.redis.RedisConfig;
import com.jinbooks.persistence.redis.RedisConfigConsts;
import com.jinbooks.persistence.redis.statement.RedisClusterStatement;

import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisClusterConnection implements IRedisConnection{
	private static final  Logger logger = LoggerFactory.getLogger(RedisClusterConnection.class);

	RedisConfig 	config;
    JedisCluster 	jedisCluster 	= null;
    Set<HostAndPort> jedisClusterNode = null;
    GenericObjectPoolConfig<Connection> poolConfig = null;

    public RedisClusterConnection(RedisConfig config) {
        this.config = config;
    }

    @Override
    public void init(){
    	if(poolConfig == null) {
	    	poolConfig = new GenericObjectPoolConfig<>();
			poolConfig.setMaxIdle(config.getMaxIdle());
			poolConfig.setMinIdle(config.getMinIdle());
			poolConfig.setMaxTotal(config.getMaxActive());
			poolConfig.setMaxWait(Duration.ofMillis(config.getMaxWait()));
    	}

    	if(jedisClusterNode == null) {
	    	jedisClusterNode = new HashSet<>();
			for(String node : config.getNodes()) {
				logger.debug("redis cluster nodes {} ." , node);
				String[] hostAndPort = node.split(":");
				jedisClusterNode.add(new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1].trim())));
			}
    	}
    }

    @Override
	public void open() {
		if(jedisCluster == null) {
			if(StringUtils.isBlank(config.getPassword())) {
				logger.debug("init redis cluster Connection no password");
				jedisCluster = new JedisCluster(
											jedisClusterNode,
											config.getTimeOut(),
											config.getTimeOut(),
											RedisConfigConsts.DEFAULT_MAXATTEMPTS,
											poolConfig);
			}else {
				logger.debug("init redis cluster Connection with password");
				jedisCluster = new JedisCluster(
											jedisClusterNode,
											config.getTimeOut(),
											config.getTimeOut(),
											RedisConfigConsts.DEFAULT_MAXATTEMPTS,
											config.getPassword(),
											poolConfig);
			}
		}

	}

    @Override
    public IRedisStatement createStatement() {
    	return new RedisClusterStatement(jedisCluster);
    }
}
