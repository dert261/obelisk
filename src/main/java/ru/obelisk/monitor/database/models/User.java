package ru.obelisk.monitor.database.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 CREATE TABLE `users` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`login` VARCHAR(50) NULL DEFAULT NULL,
	`pass` VARCHAR(50) NULL DEFAULT NULL,
	`status` INT(11) NULL DEFAULT '0',
	`role` INT(11) NULL DEFAULT '0',
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`email` VARCHAR(50) NULL DEFAULT NULL,
	`last_login` DATETIME NULL DEFAULT NULL,
	`signin_date` DATETIME NULL DEFAULT NULL,
	`ip_address` VARCHAR(16) NULL DEFAULT NULL,
	`local_user` TINYINT(1) NULL DEFAULT '0',
	`fname` VARCHAR(100) NULL DEFAULT NULL,
	`mname` VARCHAR(100) NULL DEFAULT NULL,
	`lname` VARCHAR(100) NULL DEFAULT NULL,
	`ad_guid` VARCHAR(100) NULL DEFAULT NULL,
	`mobile` VARCHAR(100) NULL DEFAULT NULL,
	`company` VARCHAR(500) NULL DEFAULT NULL,
	`department` VARCHAR(500) NULL DEFAULT NULL,
	`title` VARCHAR(500) NULL DEFAULT NULL,
	`ad_location` VARCHAR(500) NULL DEFAULT NULL,
	`street_address` VARCHAR(500) NULL DEFAULT NULL,
	`block_date` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=14757
;
 */

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	  
	@NotNull
	@Size(min = 3, max = 80)
	private String email;
	  
	@NotNull
	@Size(min = 2, max = 80)
	private String name;
}
