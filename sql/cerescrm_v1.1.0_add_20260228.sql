ALTER TABLE `cerescrm`.`jbx_follow_up` 
ADD COLUMN `relation_name` VARCHAR(200) NULL COMMENT '业务名称' AFTER `relation_id`,
ADD COLUMN `customer_name` VARCHAR(200) NULL COMMENT '客户' AFTER `relation_name`,
CHANGE COLUMN `workspace_id` `workspace_id` VARCHAR(50) NOT NULL COMMENT '工作区ID' AFTER `id`;
