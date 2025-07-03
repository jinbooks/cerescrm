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
 

package com.jinbooks.persistence.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinbooks.entity.config.ConfigLoginPolicy;
import com.jinbooks.entity.history.HistoryLogin;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.entity.permissions.Resources;

public interface LoginService extends IService<UserInfo> {

	public void updateLastLogin(UserInfo userInfo);

	public UserInfo findById(String userId);

	public UserInfo findByUsername(String loginName);

	public List<GrantedAuthority> grantAuthority(UserInfo userInfo) ;

	public Set<Resources> getResourcesBySubject(UserInfo user);

	public boolean applyLoginPolicy(UserInfo userInfo);

	public void updateLockUser(UserInfo userInfo);

	public void updateUnlockUser(UserInfo userInfo);

	public void updateLoginFailedCount(String userId) ;

	public void updateBadPasswordCount(UserInfo userInfo);

	public void updateLoginFailedCountReset(UserInfo userInfo) ;

	public void coverPassword(UserInfo userInfo, String password);

	public ConfigLoginPolicy getConfigLoginPolicy();

	public void insertHistory(HistoryLogin historyLogin);
}
