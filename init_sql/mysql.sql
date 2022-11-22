-- ----------------------------
-- Table structure for ideal_event_lock
-- ----------------------------
DROP TABLE IF EXISTS `ideal_event_lock`;
CREATE TABLE `ideal_event_lock`
(
  `lock_name` varchar(64) NOT NULL,
  PRIMARY KEY (`lock_name`)
) ENGINE = InnoDB COMMENT = '事件读取锁';

-- ----------------------------
-- Records of ideal_event_lock
-- ----------------------------
BEGIN;
INSERT INTO `ideal_event_lock`
VALUES ('transaction_publish');
COMMIT;

-- ----------------------------
-- Table structure for ideal_event_publish_temp
-- ----------------------------
DROP TABLE IF EXISTS `ideal_event_publish_temp`;
CREATE TABLE `ideal_event_publish_temp`
(
  `id`         bigint       NOT NULL AUTO_INCREMENT,
  `event_info` longtext     NOT NULL,
  `topic`      varchar(255) NOT NULL DEFAULT '',
  `exchange`   varchar(255) NOT NULL DEFAULT '',
  `timestamp`  bigint       NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT = '事件发布中转';

