
USE SPARKLERS;

CREATE TABLE `user` (
  `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `phone`       VARCHAR(16) NOT NULL DEFAULT '',
  `secret`      VARCHAR(64) NOT NULL DEFAULT '',
  `name`        VARCHAR(32)          DEFAULT '',
  `avatar`      VARCHAR(255)         DEFAULT '',
  `locked`      TINYINT(1)           DEFAULT 0,
  `authorities` VARCHAR(64)          DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_uk_1` (`phone`)
);

CREATE TABLE `user3rd` (
  `type`      VARCHAR(64)   NOT NULL,
  `open_id`   VARCHAR(16)   NOT NULL,
  `nickname`  VARCHAR(32)   DEFAULT '',
  `avatar`    VARCHAR(255)  DEFAULT '',
  `user_id`   BIGINT(20)    DEFAULT 0,
  UNIQUE KEY `user3rd_uk_1` (`type`,`open_id`),
  INDEX `idx_1`(`user_id`)
);