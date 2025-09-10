SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
use ceres_crm;

ALTER TABLE `jbx_lead` MODIFY COLUMN  `source_type` tinyint(4) DEFAULT NULL COMMENT '来源类型：1-网站，2-电话，3-推荐，4-展会，5-广告，6-邮件，7-其他';

ALTER TABLE `jbx_lead` ADD COLUMN `demand` text DEFAULT NULL COMMENT '线索需求';

ALTER TABLE `jbx_opportunity` ADD COLUMN  `people_name` varchar(100) NOT NULL COMMENT '姓名';

ALTER TABLE `jbx_opportunity` ADD COLUMN  `company` varchar(200) NOT NULL COMMENT '公司';

SET FOREIGN_KEY_CHECKS = 1;



-- 20250629
ALTER TABLE `ceres_crm`.`jbx_customer`
ADD COLUMN `content` text NULL AFTER `customer_from`,
ADD COLUMN `province` VARCHAR(200) NULL AFTER `content`,
ADD COLUMN `city` VARCHAR(200) NULL AFTER `province`;

-- 20250925
ALTER TABLE `jbx_people` ADD COLUMN `wechat` varchar(50) DEFAULT NULL COMMENT '微信号';

ALTER TABLE `jbx_customer` ADD COLUMN `wechat` varchar(50) DEFAULT NULL COMMENT '微信号';
