-- 初始化数据 admin/123456
INSERT INTO `users` (`username`, `password`, `nickname`, `introduction`, `avatar`) VALUES ('admin', 'c093d41ff0a36bd70c1daec90b724ced', '超级管理员', '我是超级管理员', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif');
INSERT INTO `user_role` (`userId`, `listIndex`, `roleName`) VALUES (1, 0, 'admin');
