CREATE TABLE `expense_table` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `expense_type` varchar(90) NOT NULL,
  `date` varchar(45) NOT NULL,
  `expense_val` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `line_chart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `index` int NOT NULL,
  `amount` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `monthly_budget` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `budget_name` varchar(90) NOT NULL,
  `limit_amount` double NOT NULL,
  `expense_amount` double NOT NULL,
  `period` varchar(45) NOT NULL,
  `init_date` varchar(45) NOT NULL,
  `final_date` varchar(45) NOT NULL,
  `selected_category` varchar(90) NOT NULL,
  `expense_index` int NOT NULL,
  `category_color` varchar(45) NOT NULL,
  `notification1` tinyint NOT NULL,
  `notification2` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_dashboard` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `total_balance` double NOT NULL DEFAULT '0',
  `food` double NOT NULL DEFAULT '0',
  `shopping` double NOT NULL DEFAULT '0',
  `housing` double NOT NULL DEFAULT '0',
  `transportation` double NOT NULL DEFAULT '0',
  `vehicle` double NOT NULL DEFAULT '0',
  `entertainment` double NOT NULL DEFAULT '0',
  `investments` double NOT NULL DEFAULT '0',
  `incomes` double NOT NULL DEFAULT '0',
  `communication` double NOT NULL DEFAULT '0',
  `fin_expenses` double NOT NULL DEFAULT '0',
  `others` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_data` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `goals_count` int unsigned NOT NULL DEFAULT '0',
  `debts_count` int unsigned NOT NULL DEFAULT '0',
  `lents_count` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_debts` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `debts_name` varchar(45) NOT NULL,
  `debt_amount` double NOT NULL,
  `repaid_amount` double NOT NULL,
  `target_date` varchar(45) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_fd` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `bank_name` varchar(100) NOT NULL,
  `deposit_amount` double NOT NULL,
  `invested_amount` double NOT NULL,
  `maturity_value` double NOT NULL,
  `earned_interest` double NOT NULL,
  `init_date` varchar(45) NOT NULL,
  `final_date` varchar(45) NOT NULL,
  `notify` tinyint NOT NULL,
  `comp_freq` double NOT NULL,
  `maturity_unit` double NOT NULL,
  `maturity_duration` double NOT NULL,
  `interest` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_goals` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `goals_name` varchar(45) NOT NULL,
  `target_amount` double NOT NULL,
  `saved_amount` double NOT NULL,
  `target_date` varchar(45) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL DEFAULT 'no',
  `lastname` varchar(45) NOT NULL DEFAULT 'name',
  `username` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `gender` varchar(45) NOT NULL DEFAULT 'null',
  `dob` varchar(45) NOT NULL DEFAULT 'null',
  `profession` varchar(45) NOT NULL DEFAULT 'null',
  `address` varchar(90) NOT NULL DEFAULT 'null',
  `phone` varchar(45) NOT NULL DEFAULT 'null',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_lents` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `lents_name` varchar(45) NOT NULL,
  `lent_amount` double NOT NULL,
  `rec_amount` double NOT NULL,
  `target_date` varchar(45) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
