CREATE DATABASE `vdf-mobile-numbers` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

CREATE TABLE `vdf-mobile-numbers`.`mobile_subscriptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(16) NOT NULL COMMENT 'Pattern is ^[1-9][0-9]{1,14}$',
  `customer_id_owner` int(11) NOT NULL,
  `customer_id_user` int(11) NOT NULL,
  `service_type` varchar(20) NOT NULL,
  `service_start_date` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4