CREATE database IF NOT EXISTS sparklers
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

DROP TABLE `user`;
CREATE TABLE `user` (
  `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `phone`       VARCHAR(16) NOT NULL DEFAULT '',
  `secret`      VARCHAR(64) NOT NULL DEFAULT '',
  `name`        VARCHAR(32)          DEFAULT '',
  `avatar`      VARCHAR(255)         DEFAULT '',
  `locked`      TINYINT(1)           DEFAULT 0,
  `authorities` VARCHAR(64)          DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_1` (`phone`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE `user3rd`;
CREATE TABLE `user3rd` (
  `type`      VARCHAR(64)   NOT NULL,
  `open_id`   VARCHAR(16)   NOT NULL,
  `nickname`  VARCHAR(32)   DEFAULT '',
  `avatar`    VARCHAR(255)  DEFAULT '',
  `user_id`   BIGINT(20)    DEFAULT 0,
  UNIQUE KEY `uk_1` (`type`,`open_id`),
  INDEX `idx_1`(`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;