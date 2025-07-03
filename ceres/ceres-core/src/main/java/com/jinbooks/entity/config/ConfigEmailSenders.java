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
 





package com.jinbooks.entity.config;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.EqualsAndHashCode;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jinbooks.entity.BaseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("jbx_config_email_senders")
public class ConfigEmailSenders  extends BaseEntity implements Serializable {

	@Serial
    private static final long serialVersionUID = 5564960495591334956L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String account;

    private String credentials;

    private String smtpHost;

    private Integer port;

    private int sslSwitch;

    private String sender;

    private String encoding;

    private String protocol;

    private int status;

    private String bookId;

    private String description;

}
