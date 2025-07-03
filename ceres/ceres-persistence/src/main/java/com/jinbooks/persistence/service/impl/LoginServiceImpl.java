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
 





package com.jinbooks.persistence.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinbooks.constants.ConstsStatus;
import com.jinbooks.entity.ChangePassword;
import com.jinbooks.entity.access.SessionList;
import com.jinbooks.entity.config.ConfigLoginPolicy;
import com.jinbooks.entity.history.HistoryLogin;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.Resources;
import com.jinbooks.persistence.mapper.LoginMapper;
import com.jinbooks.persistence.service.AuthzResourceService;
import com.jinbooks.persistence.service.AuthzService;
import com.jinbooks.persistence.service.ConfigLoginPolicyService;
import com.jinbooks.persistence.service.FileStorageService;
import com.jinbooks.persistence.service.HistoryLoginService;
import com.jinbooks.persistence.service.LoginService;
import com.jinbooks.persistence.service.SessionListService;
import com.jinbooks.persistence.service.UserInfoService;
import com.jinbooks.util.DateUtils;
import com.jinbooks.web.WebConstants;
import com.jinbooks.web.WebContext;

@Repository
public class LoginServiceImpl  extends ServiceImpl<LoginMapper,UserInfo> implements LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	LoginMapper loginMapper;

	@Autowired
    UserInfoService userInfoService;

	@Autowired
    ConfigLoginPolicyService configLoginPolicyService;

	@Autowired
    HistoryLoginService historyLoginService;

	@Autowired
    SessionListService sessionListService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AuthzService authzService;

    @Autowired
    AuthzResourceService authzResourceService;

	public LoginMapper getMapper() {
		return loginMapper;
	}

    @Override
	public void updateLastLogin(UserInfo userInfo) {
		this.getMapper().updateLastLogin(userInfo);
	}

    @Override
	public UserInfo findById(String userId) {
		return this.getMapper().findById(userId);
	}

    @Override
	public UserInfo findByUsername(String loginName) {
		return this.getMapper().findByUsername(loginName);
	}

    @Override
	public List<GrantedAuthority> grantAuthority(UserInfo userInfo) {
		return authzService.grantAuthority(userInfo);
	}

     @Override
	 public Set<Resources> getResourcesBySubject(UserInfo user){
		 return authzResourceService.getResourcesBySubject(user);
	 }


    /**
     * dynamic ConfigLoginPolicy Valid for user login.
     * @param userInfo
     * @return boolean
     */
    @Override
    public boolean applyLoginPolicy(UserInfo userInfo) {

    	ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();

        DateTime currentdateTime = new DateTime();
         /*
          * check login attempts fail times
          */
         if (userInfo.getLoginFailedCount() >= configLoginPolicy.getLoginAttempts() && userInfo.getLoginFailedTime() != null) {
             logger.debug("login Attempts is {}" , userInfo.getLoginFailedCount());
             DateTime loginFailedTime = new DateTime(userInfo.getLoginFailedTime());
             //duration
             logger.trace("Login Failed Time {}" , loginFailedTime.toString(DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS));

             Duration duration = new Duration(loginFailedTime, currentdateTime);
             int intDuration = Integer.parseInt(duration.getStandardMinutes() + "");
             logger.debug("Login Failed duration {} , " +
                           "Login policy Duration {} , "+
                           "validate result {}" ,
                           intDuration,
                           configLoginPolicy.getLockInterval(),
                           (intDuration > configLoginPolicy.getLockInterval())
                     );
             //auto unlock attempts when intDuration >= set Duration
             if(intDuration >= configLoginPolicy.getLockInterval()) {
                 logger.debug("resetAttempts ...");
                 updateUnlockUser(userInfo);
             }else {
            	 updateLockUser(userInfo);
                 throw new BadCredentialsException(
                         WebContext.getI18nValue("login.error.attempts",
                                 new Object[]{userInfo.getLoginFailedCount(),configLoginPolicy.getLockInterval()})
                         );
             }
         }

         //locked
         if(userInfo.getIsLocked()==ConstsStatus.LOCK) {
             throw new BadCredentialsException(
                                 userInfo.getUsername()+ " "+
                                 WebContext.getI18nValue("login.error.locked")
                                 );
         }
         // inactive
         if(userInfo.getStatus()!=ConstsStatus.ACTIVE) {
             throw new BadCredentialsException(
                                 userInfo.getUsername()+
                                 WebContext.getI18nValue("login.error.inactive")
                                 );
         }

         return true;
     }

    /**
     * lockUser
     *
     * @param userInfo
     */
    public void updateLockUser(UserInfo userInfo) {
        try {
            if (userInfo != null && StringUtils.isNotEmpty(userInfo.getId())&&userInfo.getIsLocked() == ConstsStatus.ACTIVE) {
            	userInfo.setIsLocked(ConstsStatus.LOCK);
            	userInfo.setUnLockTime(new Date());
            	getMapper().updateLockUser(userInfo);
     	   }
        } catch (Exception e) {
            logger.error("lockUser Exception",e);
        }
    }


    /**
     * unlockUser
     *
     * @param userInfo
     */
    public void updateUnlockUser(UserInfo userInfo) {
        try {
            if (userInfo != null && StringUtils.isNotEmpty(userInfo.getId())) {
                userInfo.setIsLocked(ConstsStatus.ACTIVE);
                userInfo.setUnLockTime(new Date());
                getMapper().updateLockUser(userInfo);
            }
        } catch (Exception e) {
            logger.error("unlockUser Exception",e);
        }
    }

    /**
     * if login password is error ,BadPasswordCount++ and set bad date
     *
     * @param userId
     */
    public void updateLoginFailedCount(String userId) {
    	try {
        	Date currentDate = new Date();
        	UserInfo user = new UserInfo();
        	user.setId(userId);
        	user.setLoginFailedTime(currentDate);
        	getMapper().updateLoginFailedCount(user);
        } catch (Exception e) {
            logger.error("setBadPasswordCount Exception",e);
        }
    }

    public void updateBadPasswordCount(UserInfo userInfo) {
        if (userInfo != null && StringUtils.isNotEmpty(userInfo.getId())) {
            userInfo.setBadPasswordCount(userInfo.getBadPasswordCount() + 1);
            try {
            	Date currentDate = new Date();
            	userInfo.setLoginFailedTime(currentDate);
            	userInfo.setBadPasswordTime(currentDate);
            	getMapper().updateBadPasswordCount(userInfo);
            } catch (Exception e) {
                logger.error("setBadPasswordCount Exception",e);
            }
            ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();
            if(userInfo.getBadPasswordCount() >= configLoginPolicy.getLoginAttempts()) {
         	   logger.debug("Bad Password Count {} , Max Attempts {}",
         			   userInfo.getBadPasswordCount() + 1,configLoginPolicy.getLoginAttempts());
         	   this.updateLockUser(userInfo);
            }
        }
    }

    public void updateLoginFailedCountReset(UserInfo userInfo) {
        if (userInfo != null && StringUtils.isNotEmpty(userInfo.getId()) && userInfo.getBadPasswordCount()>0) {
        	Date currentDate = new Date();
        	userInfo.setLoginFailedTime(currentDate);
        	userInfo.setBadPasswordTime(currentDate);
        	getMapper().updateLoginFailedCountRest(userInfo);
        }
    }

    public void coverPassword(UserInfo userInfo, String password) {
    	 //write password to database Realm
    	ChangePassword changePassword = new ChangePassword(userInfo);
        changePassword.setPassword(password);
        userInfoService.changePassword(changePassword, false);
    }



    public ConfigLoginPolicy getConfigLoginPolicy() {
    	return configLoginPolicyService.getConfigLoginPolicy();
    }

    public void insertHistory(HistoryLogin historyLogin) {
    	historyLogin.setOperateTime(new Date());
    	this.historyLoginService.save(historyLogin);
    	//insert online session
    	if(WebConstants.LOGIN_RESULT.SUCCESS.equals(historyLogin.getMessage())) {
	       SessionList onlineSession = new SessionList();
	       BeanUtils.copyProperties(historyLogin, onlineSession);
	       sessionListService.insertOnline(onlineSession);
       }
    }
}
