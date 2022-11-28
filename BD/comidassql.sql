SET FOREIGN_KEY_CHECKS=0
; 
/* Drop Views */

DROP VIEW IF EXISTS `VIEW_A` CASCADE
;

/* Drop Tables */

DROP TABLE IF EXISTS `Comida_Item` CASCADE
;

DROP TABLE IF EXISTS `Ingredientes` CASCADE
;

DROP TABLE IF EXISTS `Menu` CASCADE
;

DROP TABLE IF EXISTS `Orden` CASCADE
;

DROP TABLE IF EXISTS `Pagos` CASCADE
;

DROP TABLE IF EXISTS `Restaurante` CASCADE
;

DROP TABLE IF EXISTS `TABLE_A` CASCADE
;

DROP TABLE IF EXISTS `TABLE_B` CASCADE
;

DROP TABLE IF EXISTS `TABLE_C` CASCADE
;

DROP TABLE IF EXISTS `Usuario` CASCADE
;

/* Create Tables */

CREATE TABLE `Comida_Item`
(
	`Comida_ID` INT NOT NULL,
	`Comida_Nombre` VARCHAR(50) NULL,
	`Cantidad_Stock` INT NULL,
	`Precio` INT NULL,
	`Orden_ID` INT NULL,
	`Menu_ID` INT NULL,
	CONSTRAINT `PK_Comida_Item` PRIMARY KEY (`Comida_ID` ASC)
)

;

CREATE TABLE `Ingredientes`
(
	`Ingrediente_ID` INT NOT NULL,
	`Ingrediente_Nombre` VARCHAR(50) NULL,
	`Stock` INT NULL,
	`Comida_ID` INT NULL,
	CONSTRAINT `PK_Ingredientes` PRIMARY KEY (`Ingrediente_ID` ASC)
)

;

CREATE TABLE `Menu`
(
	`Menu_ID` INT NOT NULL,
	`Personalizable` BOOL NULL,
	`Nombre_Menu` VARCHAR(50) NULL,
	`Restaurante_ID` INT NULL,
	CONSTRAINT `PK_Menu` PRIMARY KEY (`Menu_ID` ASC)
)

;

CREATE TABLE `Orden`
(
	`Orden_ID` INT NOT NULL,
	`Cantidad` INT NOT NULL,
	`Precio_Unidad` INT NOT NULL,
	`Sub_Total` INT NOT NULL,
	`Fecha_Orden` DATE NULL,
	`Usuario_ID` INT NULL,
	CONSTRAINT `PK_Orden` PRIMARY KEY (`Orden_ID` ASC)
)

;

CREATE TABLE `Pagos`
(
	`Pagos_ID` INT NOT NULL,
	`Pago_Fecha` DATE NULL,
	`Pago_Monto` INT NULL,
	`Orden_ID` INT NULL,
	CONSTRAINT `PK_Pagos` PRIMARY KEY (`Pagos_ID` ASC)
)

;

CREATE TABLE `Restaurante`
(
	`Restaurante_ID` INT NOT NULL,
	`Resyaurante_Nombre` VARCHAR(50) NULL,
	`Direccion` VARCHAR(50) NULL,
	`Descripcion` VARCHAR(50) NULL,
	CONSTRAINT `PK_Restaurante` PRIMARY KEY (`Restaurante_ID` ASC)
)

;

CREATE TABLE `TABLE_A`
(
	`ID` INT NOT NULL,
	`COLUMN_A` INT NULL,
	`COLUMN B` INT NULL,
	CONSTRAINT `PK_Table A` PRIMARY KEY (`ID` ASC)
)

;

CREATE TABLE `TABLE_B`
(
	`COLUMN_A` INT NULL,
	`COLUMN B` INT NULL
)

;

CREATE TABLE `TABLE_C`
(
	`COLUMN_A` INT NULL,
	`COLUMN B` INT NULL
)

;

CREATE TABLE `Usuario`
(
	`Usuario_ID` INT NOT NULL,
	`Usuario_Nombre` VARCHAR(50) NOT NULL,
    `Usuario_Direccion` VARCHAR(50) NOT NULL,
	CONSTRAINT `PK_Usuario` PRIMARY KEY (`Usuario_ID` ASC)
)

;

/* Create Primary Keys, Indexes, Uniques, Checks */

ALTER TABLE `Comida_Item` 
 ADD INDEX `IXFK_Comida_Item_Menu` (`Menu_ID` ASC)
;

ALTER TABLE `Comida_Item` 
 ADD INDEX `IXFK_Comida_Item_Orden` (`Orden_ID` ASC)
;

ALTER TABLE `Ingredientes` 
 ADD INDEX `IXFK_Ingredientes_Comida_Item` (`Comida_ID` ASC)
;

ALTER TABLE `Menu` 
 ADD INDEX `IXFK_Menu_Restaurante` (`Restaurante_ID` ASC)
;

ALTER TABLE `Orden` 
 ADD INDEX `IXFK_Orden_Usuario` (`Usuario_ID` ASC)
;

ALTER TABLE `Pagos` 
 ADD INDEX `IXFK_Pagos_Orden` (`Orden_ID` ASC)
;


/* Create Foreign Key Constraints */

ALTER TABLE `Comida_Item` 
 ADD CONSTRAINT `FK_Comida_Item_Menu`
	FOREIGN KEY (`Menu_ID`) REFERENCES `Menu` (`Menu_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `Comida_Item` 
 ADD CONSTRAINT `FK_Comida_Item_Orden`
	FOREIGN KEY (`Orden_ID`) REFERENCES `Orden` (`Orden_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `Ingredientes` 
 ADD CONSTRAINT `FK_Ingredientes_Comida_Item`
	FOREIGN KEY (`Comida_ID`) REFERENCES `Comida_Item` (`Comida_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `Menu` 
 ADD CONSTRAINT `FK_Menu_Restaurante`
	FOREIGN KEY (`Restaurante_ID`) REFERENCES `Restaurante` (`Restaurante_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `Orden` 
 ADD CONSTRAINT `FK_Orden_Usuario`
	FOREIGN KEY (`Usuario_ID`) REFERENCES `Usuario` (`Usuario_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `Pagos` 
 ADD CONSTRAINT `FK_Pagos_Orden`
	FOREIGN KEY (`Orden_ID`) REFERENCES `Orden` (`Orden_ID`) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE `TABLE_B` 
 ADD CONSTRAINT `FK_TABLE_B_TABLE_A`
	FOREIGN KEY (`COLUMN_A`) REFERENCES `TABLE_A` (`ID`) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE `TABLE_C` 
 ADD CONSTRAINT `FK_TABLE_C_TABLE_A`
	FOREIGN KEY (`COLUMN_A`) REFERENCES `TABLE_A` (`ID`) ON DELETE No Action ON UPDATE No Action
;

SET FOREIGN_KEY_CHECKS=1
; 


INSERT INTO Usuario VALUES (589, "Uwur", 223);

select * from Usuario;
