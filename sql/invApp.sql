
DROP USER IF EXISTS 'invAppUser'@'localhost' ;
DROP USER IF EXISTS 'invAppUser'@'%' ;

CREATE USER 'invAppUser'@'localhost' IDENTIFIED BY '1nvApp!23';
GRANT ALL PRIVILEGES ON *.* TO 'invAppUser'@'localhost' WITH GRANT OPTION;
SHOW GRANTS FOR 'invAppUser'@'localhost';


CREATE USER 'invAppUser'@'%' IDENTIFIED BY '1nvApp!23';
GRANT ALL PRIVILEGES ON *.* TO 'invAppUser'@'%' WITH GRANT OPTION;
SHOW GRANTS FOR 'invAppUser'@'%';


DROP DATABASE IF EXISTS invApp;

CREATE DATABASE invApp CHARACTER SET utf8 COLLATE utf8_general_ci;

USE invApp;

CREATE TABLE invAppUser (
	id			mediumInt AUTO_INCREMENT NOT NULL,
	userCode	VARCHAR(10) NOT NULL,
	userPass	VARCHAR(25),
	userGrp		ENUM('ADMIN','GRP1','GRP2') NOT NULL,
	readOnly	TINYINT(1) DEFAULT 0,
	
	PRIMARY KEY(id)
) ENGINE = InnoDb;

INSERT INTO invAppUser VALUES (0,'admin','admin123','ADMIN',0);
INSERT INTO invAppUser VALUES (0,'Brian','Brian912','GRP1',0);

	
CREATE TABLE itemGroup (
	id         	 mediumInt AUTO_INCREMENT NOT NULL,
	description  VARCHAR(60),
	
	PRIMARY KEY(id)
) ENGINE = InnoDb;

CREATE TABLE itemCategory (
	id        	mediumInt AUTO_INCREMENT NOT NULL,
	description VARCHAR(60),
	
	PRIMARY KEY(id)
) ENGINE = InnoDb;


CREATE TABLE item(
	itemCode      VARCHAR(20) NOT NULL,
	description   VARCHAR(60),
	barCode		  VARCHAR(13),
	comments      TEXT,
	categoryId    mediumInt,
	groupId       mediumInt,
	costPrice	  DECIMAL(6,2),
	sellPrice_1   DECIMAL(6,2),
	sellPrice_2   DECIMAL(6,2),
	pic           BLOB,
	qtyInStock	  INTEGER,
	
	PRIMARY KEY(itemCode),
	
	UNIQUE KEY(barCode),
	
	FOREIGN KEY(categoryId) REFERENCES itemCategory(id) ON DELETE RESTRICT,
	FOREIGN KEY(groupId) REFERENCES itemGroup(id) ON DELETE RESTRICT
	
) ENGINE = InnoDb;


CREATE TABLE itemTrans(
	transDate	DATE,
	transType	ENUM('CashSale','CashSaleRet','AdjIn','AdjOut') NOT NULL,
	reference   INTEGER,
	itemCode	VARCHAR(20) NOT NULL,
	categoryId  mediumInt,
	groupId     mediumInt,
	costPrice	DECIMAL(6,2),
	sellPrice   DECIMAL(6,2),
	transQty	mediumInt,
	comments    TEXT,
	
	PRIMARY KEY(transDate, transType, reference),

	FOREIGN KEY(categoryId) REFERENCES itemCategory(id) ON DELETE RESTRICT,
	FOREIGN KEY(groupId) REFERENCES itemGroup(id) ON DELETE RESTRICT

) ENGINE = InnoDb;

/*
  to set utf8
  to add log file and triggers to update the 2 systems
  log file might be table rowId and output to csv
*/

/*Views*/
  
