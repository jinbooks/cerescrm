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
 

 

package com.jinbooks.entity.dto;

import java.util.ArrayList;
import java.util.List;


public class QueryGroupMembersDto {
	
	List<String> members;

	public QueryGroupMembersDto() {
		members = new ArrayList<>();
	}

	public QueryGroupMembersDto(List<String> members) {
		this.members = members;
	}
	
	public void add(String memberId) {
		this.members.add(memberId);
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
	
}
