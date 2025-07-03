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
 





package com.jinbooks.authn.realm;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.jinbooks.authn.SignedPrincipal;
import com.jinbooks.entity.client.ClientResolve;
import com.jinbooks.entity.history.HistoryLogin;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.service.LoginService;
import com.jinbooks.persistence.service.PasswordPolicyValidatorService;
import com.jinbooks.web.WebConstants;
import com.jinbooks.web.WebContext;

/**
 * AbstractAuthenticationRealm.认证域抽象类
 *
 * @author Crystal.Sea
 *
 */
public abstract class AbstractAuthenticationRealm {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationRealm.class);

    protected PasswordPolicyValidatorService passwordPolicyValidator;

    protected LoginService loginService;

    /**
     *
     */
    public AbstractAuthenticationRealm() {

    }

    public PasswordPolicyValidatorService getPasswordPolicyValidator() {
        return passwordPolicyValidator;
    }



    public UserInfo loadUserInfo(String username, String password) {
        return loginService.findByUsername(username);
    }

    public abstract boolean passwordMatches(UserInfo userInfo, String password);

    /**
     * grant Authority by userinfo
     *
     * @param userInfo
     * @return ArrayList<GrantedAuthority>
     */
    public List<GrantedAuthority> grantAuthority(UserInfo userInfo) {
        return loginService.grantAuthority(userInfo);
    }

    public void applyLoginPolicy(UserInfo userInfo) {
    	this.loginService.applyLoginPolicy(userInfo);
    }

    /**
     * login log write to log db
     *
     * @param userInfo
     * @param client
     * @param type
     * @param code
     * @param message
     */
    public boolean insertLoginHistory(UserInfo userInfo,ClientResolve client, String type, String provider, String code, String message) {
        HistoryLogin historyLogin = new HistoryLogin();
        historyLogin.setSessionId(WebContext.genId());
        Authentication  authentication  = (Authentication ) WebContext.getAttribute(WebConstants.AUTHENTICATION);
        if(authentication != null
        		&& authentication.getPrincipal() instanceof SignedPrincipal principal) {
              historyLogin.setSessionId(principal.getSessionId());
              historyLogin.setStyle(principal.getStyle());
        }

        logger.debug("user session id is {} . ",historyLogin.getSessionId());
        String requestIpAddress = WebContext.getRequestIpAddress();

        userInfo.setLastLoginTime(new Date());
        userInfo.setLastLoginIp(requestIpAddress);

        historyLogin.setIpAddr(userInfo.getLastLoginIp());
        historyLogin.setProvider(provider);
        historyLogin.setCode(code);
        historyLogin.setLoginType(type);
        historyLogin.setMessage(message);
        historyLogin.setUserId(userInfo.getId());
        historyLogin.setUsername(userInfo.getUsername());
        historyLogin.setDisplayName(userInfo.getDisplayName());
        historyLogin.setBookId(userInfo.getWorkspaceId());

        historyLogin.setBrowser(client.getBrowser());
        historyLogin.setPlatform(client.getPlatform());

        historyLogin.setCountry(client.getCountry());
        historyLogin.setProvince(client.getProvince());
        historyLogin.setCity(client.getCity());
        historyLogin.setLocation(client.getLocation());

        historyLogin.setOperateTime(new Date());
        //insert
        loginService.insertHistory(historyLogin);

        //update user last info
        loginService.updateLastLogin(userInfo);

        return true;
    }
}
