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
 

package com.jinbooks.entity.idm.dto;

import com.jinbooks.entity.PageQuery;

import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/11/27 11:59
 */

@Data
public class OrgUserPageDto extends PageQuery {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5854742069639940711L;
	private String bookId;
}
