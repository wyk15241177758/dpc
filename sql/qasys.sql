/*
Navicat MySQL Data Transfer

Source Server         : 127
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2016-11-03 11:41:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `jobinf`
-- ----------------------------
DROP TABLE IF EXISTS `jobinf`;
CREATE TABLE `jobinf` (
  `JOBID` bigint(20) NOT NULL AUTO_INCREMENT,
  `JOBNAME` varchar(64) NOT NULL,
  `JOBGROUP` varchar(64) NOT NULL,
  `JOBSTATUS` int(11) NOT NULL,
  `CRONEXPRESSION` varchar(225) NOT NULL,
  `BEANCLASS` varchar(125) NOT NULL,
  `DESCRIPTION` longtext,
  `TRIGGERNAME` varchar(64) NOT NULL,
  `TRIGGERGROUPNAME` varchar(64) NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobinf
-- ----------------------------
INSERT INTO `jobinf` VALUES ('1', '数据推送', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', '', 'TRIGGER_NAME数据推送', 'TRIGGERGROUP_NAME', '2016-11-02 22:08:10', '2016-11-03 00:33:30');

-- ----------------------------
-- Table structure for `joblog`
-- ----------------------------
DROP TABLE IF EXISTS `joblog`;
CREATE TABLE `joblog` (
  `JOBID` bigint(20) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `INDEXSIZE` int(11) DEFAULT NULL,
  `EXETIME` bigint(20) DEFAULT NULL,
  `START` datetime DEFAULT NULL,
  `END` datetime DEFAULT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of joblog
-- ----------------------------
INSERT INTO `joblog` VALUES ('1', '0', '0', '0', '2016-11-03 11:00:00', null);

-- ----------------------------
-- Table structure for `keyword`
-- ----------------------------
DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PID` int(11) DEFAULT NULL,
  `WORDVALUE` varchar(255) DEFAULT NULL,
  `EXTEND` varchar(255) DEFAULT NULL,
  `FLOOR` int(11) DEFAULT NULL,
  `IDX` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKFA40A9C990831BD2` (`PID`),
  CONSTRAINT `FKFA40A9C990831BD2` FOREIGN KEY (`PID`) REFERENCES `keyword` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------
