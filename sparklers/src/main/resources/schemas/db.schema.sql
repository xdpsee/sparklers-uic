
CREATE DATABASE IF NOT EXISTS `sparklers` DEFAULT CHARSET utf8mb4;

DROP TABLE `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `phone` varchar(16) NOT NULL DEFAULT '',
  `secret` varchar(64) NOT NULL DEFAULT '',
  `name` varchar(32) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_1` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

