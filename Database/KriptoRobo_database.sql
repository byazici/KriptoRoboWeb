CREATE TABLE `algo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(12) NOT NULL,
  `pair_symbol` varchar(12) NOT NULL,
  `create_date` datetime NOT NULL,
  `last_create` datetime NOT NULL,
  `expire_date` datetime NOT NULL,
  `max_amount` decimal(20,2) NOT NULL,
  `buy_rate` decimal(20,3) NOT NULL,
  `buy_price` decimal(20,2) NOT NULL,
  `sell_rate` decimal(20,3) NOT NULL,
  `sell_price` decimal(20,2) NOT NULL,
  `trig_rate` decimal(20,3) NOT NULL,
  `trig_price` decimal(20,2) NOT NULL,
  `avg_price` decimal(20,2) NOT NULL,
  `repeated` tinyint(4) NOT NULL,
  `ref_id` int(11) DEFAULT NULL,
  `duration` int(11) NOT NULL,
  `price_duration` int(11) NOT NULL,
  `buy_id` int(11) DEFAULT NULL,
  `sell_id` int(11) DEFAULT NULL,
  `profit` decimal(20,2) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `descr` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `balance_daily` (
  `tick_date` date NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `try_balance` decimal(22,8) NOT NULL,
  `try_amount` decimal(22,8) NOT NULL,
  `btc_balance` decimal(22,8) NOT NULL,
  `btc_amount` decimal(22,8) NOT NULL,
  `eth_balance` decimal(22,8) NOT NULL,
  `eth_amount` decimal(22,8) NOT NULL,
  `overall_btc` decimal(22,8) NOT NULL,
  PRIMARY KEY (`tick_date`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE `feedback` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(50) NOT NULL DEFAULT '',
  `mail` varchar(128) NOT NULL DEFAULT '',
  `subject` varchar(128) NOT NULL DEFAULT '',
  `message` varchar(1024) NOT NULL DEFAULT '',
  `insert_date` datetime NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `pair_symbol` varchar(12) NOT NULL DEFAULT '',
  `buy_sell` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `rlz_date` datetime DEFAULT NULL,
  `cancel_date` datetime DEFAULT NULL,
  `price` decimal(20,2) NOT NULL,
  `amount` decimal(20,8) NOT NULL,
  `total` decimal(20,8) NOT NULL,
  `rlz_total` decimal(20,8) DEFAULT NULL,
  `trade_ref` varchar(20) NOT NULL,
  `market_order` tinyint(4) NOT NULL,
  `status` tinyint(11) NOT NULL,
  `algo_id` int(4) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `tick` (
  `tick_date` datetime NOT NULL,
  `pair` varchar(10) NOT NULL,
  `last` decimal(20,2) NOT NULL,
  PRIMARY KEY (`tick_date`,`pair`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `tick_daily` (
  `tick_date` date NOT NULL,
  `pair` varchar(10) NOT NULL,
  `open` decimal(20,2) NOT NULL,
  `high` decimal(20,2) NOT NULL,
  `low` decimal(20,2) NOT NULL,
  `close` decimal(20,2) NOT NULL,
  `average` decimal(20,2) NOT NULL,
  PRIMARY KEY (`tick_date`,`pair`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `tick_hourly` (
  `tick_date` datetime NOT NULL,
  `pair` varchar(10) NOT NULL,
  `open` decimal(20,2) NOT NULL,
  `high` decimal(20,2) NOT NULL,
  `low` decimal(20,2) NOT NULL,
  `close` decimal(20,2) NOT NULL,
  PRIMARY KEY (`tick_date`,`pair`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_mail` varchar(128) DEFAULT NULL,
  `user_pass` varchar(216) DEFAULT NULL,
  `first_name` varchar(30) DEFAULT '',
  `last_name` varchar(30) DEFAULT NULL,
  `tckn` varchar(11) DEFAULT NULL,
  `birth_year` int(11) DEFAULT NULL,
  `public_key` varchar(256) DEFAULT NULL,
  `private_key` varchar(256) DEFAULT NULL,
  `roles` int(11) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `wrong_login` int(11) DEFAULT NULL,
  `token` varchar(256) DEFAULT NULL,
  `max_algo` int(11) DEFAULT NULL,
  `max_amt` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_token` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `token` varchar(256) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

