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
 

 

package com.jinbooks.authn.session;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSessions  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2525429081773580767L;

	LinkedBlockingQueue<String> sessionIdQueue = new LinkedBlockingQueue<>(); 
	
	LinkedBlockingQueue<String> mobileSessionIdQueue = new LinkedBlockingQueue<>();
	
	public LinkedBlockingQueue<String> getSessionIdQueue() {
		return sessionIdQueue;
	}

	public void setSessionIdQueue(LinkedBlockingQueue<String> sessionIdQueue) {
		this.sessionIdQueue = sessionIdQueue;
	}

	public LinkedBlockingQueue<String> getMobileSessionIdQueue() {
		return mobileSessionIdQueue;
	}

	public void setMobileSessionIdQueue(LinkedBlockingQueue<String> mobileSessionIdQueue) {
		this.mobileSessionIdQueue = mobileSessionIdQueue;
	}

}
