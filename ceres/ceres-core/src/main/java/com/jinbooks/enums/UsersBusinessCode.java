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




package com.jinbooks.enums;

import com.jinbooks.web.WebContext;
import lombok.Getter;

@Getter
public enum UsersBusinessCode {

    USER_FORBIDDEN(500008, WebContext.getI18nValue("user_forbidden")),

    USERNAME_USED(500009, WebContext.getI18nValue("username_used")),

    MOBILE_USED(500010, WebContext.getI18nValue("mobile_used")),

    EMAIL_USED(500011, WebContext.getI18nValue("email_used")),

    USER_VERIFY_MOBILE_ABSENT(500005, WebContext.getI18nValue("user_verify_mobile_absent"));

    final String msg;
    final Integer code;

    UsersBusinessCode(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
