create database if not exists sparklers
  default character set utf8mb4
  collate utf8mb4_unicode_ci;

DROP TABLE `user`;
CREATE TABLE `user` (
  `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `phone`       varchar(16) NOT NULL DEFAULT '',
  `secret`      varchar(64) NOT NULL DEFAULT '',
  `name`        varchar(32)          DEFAULT '',
  `avatar`      varchar(255)         DEFAULT '',
  `locked`      TINYINT(1)           DEFAULT 0,
  `authorities` VARCHAR(64)          default '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_1` (`phone`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

