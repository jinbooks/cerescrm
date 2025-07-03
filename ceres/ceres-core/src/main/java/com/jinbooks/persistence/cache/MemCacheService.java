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
 





package com.jinbooks.persistence.cache;

public interface MemCacheService {

	//use sessionId + name
	public  void 	put(String sessionId , String name, Object value);

    public  Object 	get(String sessionId , String name);

    public  Object 	remove(String sessionId , String name);

    //use key
    public  void 	put(String key, Object value);

    public  Object 	get(String key);

    public  Object	remove(String key);

}
