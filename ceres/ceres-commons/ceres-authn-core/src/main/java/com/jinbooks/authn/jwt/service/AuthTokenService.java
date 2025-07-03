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
 





package com.jinbooks.authn.jwt.service;

import java.text.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.jinbooks.authn.congress.CongressService;
import com.jinbooks.authn.jwt.AuthJwt;
import com.jinbooks.configuration.AuthJwkConfig;
import com.jinbooks.crypto.jwt.Hmac512Service;
import com.jinbooks.persistence.cache.MemCacheService;
import com.jinbooks.web.WebContext;
import com.nimbusds.jose.JOSEException;

/**
 * 认证令牌服务
 *
 * @author Crystal.Sea
 *
 */
public class AuthTokenService  extends AuthJwtService{
	private static final  Logger logger = LoggerFactory.getLogger(AuthTokenService.class);

	/**
	 * 前端JWT生成配置信息
	 */
	AuthJwkConfig authJwkConfig;
	/**
	 * congress服务
	 */
	CongressService congressService;
	/**
	 * 缓存服务
	 */
	MemCacheService memCacheService;
	/**
	 * 认证刷新token服务
	 */
	AuthRefreshTokenService refreshTokenService;

	public AuthTokenService(
				AuthJwkConfig authJwkConfig,
				CongressService congressService,
				MemCacheService memCacheService,
				AuthRefreshTokenService refreshTokenService) throws JOSEException {

		this.authJwkConfig = authJwkConfig;

		this.congressService = congressService;

		this.memCacheService = memCacheService;

		this.refreshTokenService = refreshTokenService;

		this.hmac512Service = new Hmac512Service(authJwkConfig.getSecret());

	}

	/**
	 * create AuthJwt use Authentication JWT/根据认证信息生成认证JWT
	 * @param authentication
	 * @return AuthJwt
	 */
	public AuthJwt genAuthJwt(Authentication authentication) {
		if(authentication != null) {
			String refreshToken = refreshTokenService.genRefreshToken(authentication);
			logger.trace("generate JWT Token");
			String accessToken = genJwt(authentication);
			return new AuthJwt(
						accessToken,
						authentication,
						authJwkConfig.getExpires(),
						refreshToken);
		}
		return null;
	}

	public String genJwt(Authentication authentication) {
		return genJwt(
					authentication,
					authJwkConfig.getIssuer(),
					authJwkConfig.getExpires());
	}


	/**
	 * JWT with subject/subject生成JWT
	 * @param subject subject
	 * @return
	 */
	public String genJwt(String subject) {
		return genJwt(subject,authJwkConfig.getIssuer(),authJwkConfig.getExpires());
	}

	/**
	 * Random JWT/随机JWT
	 * @return
	 */
	public String genRandomJwt() {
		return genRandomJwt(authJwkConfig.getExpires());
	}

	/**
	 * 创建congress
	 * @param authentication
	 * @return congress
	 */
	public String createCongress(Authentication  authentication) {
		String congress = WebContext.genId();
		String refreshToken = refreshTokenService.genRefreshToken(authentication);
		congressService.store(
				congress,
				new AuthJwt(
						genJwt(authentication),
						authentication,
						authJwkConfig.getExpires(),
						refreshToken)
			);
		return congress;
	}

	/**
	 * 消费congress
	 * @param congress
	 * @return AuthJwt
	 */
	public AuthJwt consumeCongress(String congress) {
		return congressService.consume(congress);
	}

	/**
	 * 验证码校验
	 * @param state
	 * @param captcha
	 * @return boolean
	 */
	public boolean validateCaptcha(String state,String captcha) {
    	try {
			String jwtId = resolveJWTID(state);
			if(StringUtils.isNotBlank(jwtId) &&StringUtils.isNotBlank(captcha)) {
				Object momentaryCaptcha = memCacheService.get("", jwtId);
		        logger.debug("captcha : {}, momentary Captcha : {}" ,captcha, momentaryCaptcha);
		        if (!StringUtils.isBlank(captcha) && captcha.equals(momentaryCaptcha.toString())) {
		        	memCacheService.remove("", jwtId);
		        	return true;
		        }
			}
		} catch (ParseException e) {
			 logger.debug("Exception ",e);
		}
    	 return false;
    }


}
