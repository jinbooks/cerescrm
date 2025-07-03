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
 





package com.jinbooks.ip2location;

import org.lionsoul.ip2region.xdb.Searcher;
import com.jinbooks.ip2location.offline.Ip2regionV2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class Ip2RegionV2Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String ip ="101.227.131.220";
		ClassPathResource resource = new ClassPathResource("/ip2region/ip2region.xdb");
        byte[] dbBinStr = StreamUtils.copyToByteArray(resource.getInputStream());
        System.out.println(dbBinStr.length);
        //logger.debug("ip2region length {}",dbBinStr.length);
        Searcher searcher = Searcher.newWithBuffer(dbBinStr);
        Ip2regionV2 ipRegionV2OffLine = new Ip2regionV2(searcher);
        String region = ipRegionV2OffLine.region(ip).toString();
        System.out.println(region);

	}
}
