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

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/23 17:18
 */
@Getter
public enum BookBusinessExceptionEnum {

    ILLEGAL_MOVE_ORG(500001, WebContext.getI18nValue("illegal.move.org")),

    DUPLICATE_SUBJECTSCODE_EXIST(500002, WebContext.getI18nValue("duplicate.subjectscode.exist")),

    SUB_SUBJECTS_EXISTS(500003, WebContext.getI18nValue("sub.subjects.exists")),

    SUB_SUBJECTS_ACTIVE(500004, WebContext.getI18nValue("sub.subjects.active")),

    PARENT_ORGS_FORBIDDEN(500005, WebContext.getI18nValue("parent.orgs.forbidden")),

    DUPLICATE_DEEP_LIMIT(500006, WebContext.getI18nValue("duplicate.deep.limit")),

    DUPLICATE_SUBJECTS_EXIST(500007, WebContext.getI18nValue("duplicate.subjects.exist")),

    DUPLICATE_SETNAME_EXIST(500008, WebContext.getI18nValue("duplicate.setname.exist")),

    ALREADY_STANDARD_USED(500009, WebContext.getI18nValue("already.standard.used")),

    ALREADY_SET_USED(500010, WebContext.getI18nValue("already.set.used")),

    DISABLE_BEFORE_DELETE(500011, WebContext.getI18nValue("disable.before.delete")),

    DELETE_HAS_VOUCHER(500012, WebContext.getI18nValue("delete.has.voucher"));

    final String msg;

    final Integer code;

    BookBusinessExceptionEnum(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
