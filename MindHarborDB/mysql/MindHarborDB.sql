-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`utente` (
  `Username` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Cognome` VARCHAR(45) NOT NULL,
  `Categoria` ENUM('Paziente', 'Psicologo') NOT NULL,
  `Genere` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`psicologo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`psicologo` (
  `CostoOrario` INT NOT NULL,
  `Nome_Studio` VARCHAR(45) NOT NULL,
  `Psicologo_Username` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Psicologo_Username`),
  INDEX `fk_Psicologo_Utente1_idx` (`Psicologo_Username` ASC) VISIBLE,
  CONSTRAINT `fk_Psicologo_Utente1`
    FOREIGN KEY (`Psicologo_Username`)
    REFERENCES `mydb`.`utente` (`Username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`paziente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`paziente` (
  `Età` INT NOT NULL,
  `Diagnosi` VARCHAR(45) NULL DEFAULT NULL,
  `Paziente_Username` VARCHAR(45) NOT NULL,
  `Username_Psicologo` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Paziente_Username`),
  INDEX `fk_Paziente_Utente1_idx` (`Paziente_Username` ASC) VISIBLE,
  INDEX `Username_Psicologo` (`Username_Psicologo` ASC) VISIBLE,
  CONSTRAINT `fk_Paziente_Utente1`
    FOREIGN KEY (`Paziente_Username`)
    REFERENCES `mydb`.`utente` (`Username`),
  CONSTRAINT `Username_Psicologo`
    FOREIGN KEY (`Username_Psicologo`)
    REFERENCES `mydb`.`psicologo` (`Psicologo_Username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`appuntamento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`appuntamento` (
  `ID_Appuntamento` INT NOT NULL AUTO_INCREMENT,
  `Data` DATE NOT NULL,
  `Ora` VARCHAR(5) NOT NULL,
  `Username_Paziente` VARCHAR(45) NOT NULL,
  `Username_Psicologo` VARCHAR(45) NOT NULL,
  `statoAppuntamento` TINYINT(1) NULL DEFAULT '0' COMMENT 'Indica se l\'appuntamento è stato accettato dallo psicologo o no',
  `statoNotificaPsicologo` TINYINT(1) NULL DEFAULT '1' COMMENT 'indica allo psicologo la presenza di una nuova richiesta di appuntamento',
  `statoNotificaPaziente` TINYINT(1) NULL DEFAULT '0' COMMENT 'Indica al paziente l\'approvazione della sua richiesta di appuntamento',
  PRIMARY KEY (`ID_Appuntamento`),
  INDEX `Username_Paziente_idx` (`Username_Paziente` ASC) VISIBLE,
  INDEX `Username_Psicologofx` (`Username_Psicologo` ASC) VISIBLE,
  CONSTRAINT `Username_Paziente`
    FOREIGN KEY (`Username_Paziente`)
    REFERENCES `mydb`.`paziente` (`Paziente_Username`),
  CONSTRAINT `Username_Psicologofx`
    FOREIGN KEY (`Username_Psicologo`)
    REFERENCES `mydb`.`psicologo` (`Psicologo_Username`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`testpsicologico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`testpsicologico` (
  `DataOdierna` DATE NOT NULL,
  `Risultato` INT NULL DEFAULT '0',
  `Psicologo` VARCHAR(45) NOT NULL,
  `Paziente` VARCHAR(45) NOT NULL,
  `Test` VARCHAR(45) NOT NULL,
  `statoNotificaPaziente` TINYINT(1) NOT NULL DEFAULT '1' COMMENT 'Indica al paziente se ci sono nuovi test da svolgere',
  `Svolto` TINYINT(1) NULL DEFAULT '0' COMMENT 'indica se il test è stato svolto dal paziente oppure no',
  `statoNotificaPsicologo` TINYINT(1) NULL DEFAULT '0' COMMENT 'indica allo psicologo che ci sono nuovi test svolti dal paziente',
  PRIMARY KEY (`DataOdierna`, `Psicologo`, `Paziente`),
  INDEX `fk_Test Psicologico_Psicologo1_idx` (`Psicologo` ASC) VISIBLE,
  INDEX `fk_Test Psicologico_Paziente1_idx` (`Paziente` ASC) VISIBLE,
  INDEX `fk_Test Psicologico_Lista Test1_idx` (`Test` ASC) VISIBLE,
  CONSTRAINT `fk_Test Psicologico_Paziente1`
    FOREIGN KEY (`Paziente`)
    REFERENCES `mydb`.`paziente` (`Paziente_Username`),
  CONSTRAINT `fk_Test Psicologico_Psicologo1`
    FOREIGN KEY (`Psicologo`)
    REFERENCES `mydb`.`psicologo` (`Psicologo_Username`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`terapia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`terapia` (
  `Psicologo` VARCHAR(45) NOT NULL,
  `Paziente` VARCHAR(45) NOT NULL,
  `Terapia` VARCHAR(800) NOT NULL,
  `DataOdierna` VARCHAR(10) NULL DEFAULT NULL,
  `DataTest` DATE NOT NULL,
  `NotificaPaziente` INT NULL DEFAULT '1',
  PRIMARY KEY (`Psicologo`, `Paziente`, `DataTest`),
  INDEX `terapia_testpsicologico_Paziente_fk_2` (`Paziente` ASC) VISIBLE,
  INDEX `FK_Testpsicologico` (`DataTest` ASC, `Psicologo` ASC, `Paziente` ASC) VISIBLE,
  CONSTRAINT `FK_Testpsicologico`
    FOREIGN KEY (`DataTest` , `Psicologo` , `Paziente`)
    REFERENCES `mydb`.`testpsicologico` (`DataOdierna` , `Psicologo` , `Paziente`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;