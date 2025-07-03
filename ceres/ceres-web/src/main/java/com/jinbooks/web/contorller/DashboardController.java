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


package com.jinbooks.web.contorller;

import com.jinbooks.entity.dashboard.DashBoardResultVo;
import com.jinbooks.persistence.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jinbooks.authn.annotation.CurrentUser;
import com.jinbooks.entity.Message;
import com.jinbooks.entity.idm.UserInfo;

/**
 * 仪表盘数据接口
 */

@Slf4j
@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    /**
     * 仪表盘数据
     */
    @GetMapping("/dashboard")
    public Message<DashBoardResultVo> dashboard(@RequestParam("year") Integer year, @CurrentUser UserInfo currentUser) {
        return Message.ok(dashboardService.dashboard(year, currentUser));
    }
}
