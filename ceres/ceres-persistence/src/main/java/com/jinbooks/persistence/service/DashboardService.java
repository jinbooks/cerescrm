package com.jinbooks.persistence.service;

import com.jinbooks.entity.dashboard.DashBoardResultVo;
import com.jinbooks.entity.idm.UserInfo;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/06/29 13:25:43
 * {@code @version} 1.0
 */

public interface DashboardService {

    DashBoardResultVo dashboard(Integer year, UserInfo currentUser);
}
