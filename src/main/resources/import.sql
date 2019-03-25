-- 初始化数据 admin/123456
INSERT INTO `users` (`username`, `password`, `nickname`, `introduction`, `avatar`, `roleId`) VALUES ('admin', 'c093d41ff0a36bd70c1daec90b724ced', '超级管理员', '我是超级管理员', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',1 );
INSERT INTO `user_roles` (`id`, `code`, `name`) VALUES (1, 'admin', '超级管理员');

-- 系统参数
INSERT INTO `system_parameter` (`introduce`, `key`, `status`, `value`) VALUES ('阿里云-账号余额报警阈值（单位：元）', 'ALI_ACCOUNT_BALANCE', 'fixed', '3000.00');
INSERT INTO `system_parameter` (`introduce`, `key`, `status`, `value`) VALUES ('阿里云-ECS服务器到期日期报警阈值（单位：天）', 'ALI_ECS_EXPIRED_DAY', 'fixed', '30');
INSERT INTO `system_parameter` (`introduce`, `key`, `status`, `value`) VALUES ('GODADDY-域名到期日期报警阈值（单位：天）', 'GODADDY_DOMAIN_EXPIRED_DAY', 'fixed', '30');
INSERT INTO `system_parameter` (`introduce`, `key`, `status`, `value`) VALUES ('GODADDY-证书到期日期报警阈值（单位：天）', 'GODADDY_CERTIFICATE_EXPIRED_DAY', 'fixed', '30');
INSERT INTO `system_parameter` (`introduce`, `key`, `status`, `value`) VALUES ('监控-服务器时间差报警阈值（单位：秒）', 'MONITORTIME_TIMEOUT_SECOND', 'fixed', '5');


-- 资源类型 resource_type
INSERT INTO `resource_type` (`id`, `code`, `name`, `column`, `remark`) VALUES (1, 'fwq', '服务器表格', '["状态","型号","机柜","资产编号","SN","操作系统","作用","专线IP","外网IP","内网IP","管理卡IP","掩码","远程端口","内网端口","管理端口","外网/专线端口","CPU","内存","RQAID","硬盘","电源","U数","备注"]', NULL);
INSERT INTO `resource_type` (`id`, `code`, `name`, `column`, `remark`) VALUES (2, 'kcpj', '库存（配件）', '["类型","品牌","容量","数量","更新","备注"]', NULL);
INSERT INTO `resource_type` (`id`, `code`, `name`, `column`, `remark`) VALUES (3, 'kcfwq', '库存（服务器）', '["型号","厂家","资产编号","SN","CPU","内存","硬盘","电源","U数","备注"]', NULL);
INSERT INTO `resource_type` (`id`, `code`, `name`, `column`, `remark`) VALUES (4, 'kcbgjl', '库存变更记录', '["操作人","类型","操作"]', NULL);