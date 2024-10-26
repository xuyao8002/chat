CREATE TABLE `leaf_alloc` (
  `biz_tag` varchar(128) NOT NULL DEFAULT '',
  `max_id` bigint NOT NULL DEFAULT '1',
  `step` int NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
insert into leaf_alloc(biz_tag, max_id, step, description) values('userId', 1, 10, '用户id');



CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `user_pwd` varchar(64) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE `user_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `friend_id` bigint NOT NULL COMMENT '好友id',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识，0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_friend_id_idx` (`user_id`,`friend_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关系表';

CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_id` bigint NOT NULL COMMENT '发送方id',
  `to_id` bigint NOT NULL COMMENT '接收方id',
  `msg` varchar(500) NOT NULL COMMENT '消息内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `type` tinyint(1) NOT NULL DEFAULT '2' COMMENT '消息类型',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识，0未删除，1已删除',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已读标识，0未读，1已读',
  PRIMARY KEY (`id`),
  KEY `from_id_to_id_idx` (`from_id`,`to_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息表';