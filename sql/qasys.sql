/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2016-11-08 10:07:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gwfield`
-- ----------------------------
DROP TABLE IF EXISTS `gwfield`;
CREATE TABLE `gwfield` (
  `fieldid` bigint(20) NOT NULL AUTO_INCREMENT,
  `jobid` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `tablekey` tinyint(10) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`fieldid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gwfield
-- ----------------------------
INSERT INTO `gwfield` VALUES ('5', '10', 'xq_id', '1', '存储');
INSERT INTO `gwfield` VALUES ('6', '10', 'xq_title', '0', '存储');
INSERT INTO `gwfield` VALUES ('7', '10', 'xq_url', '0', '存储');
INSERT INTO `gwfield` VALUES ('8', '10', 'lm_name', '0', '存储');
INSERT INTO `gwfield` VALUES ('9', '10', 'zd_name', '0', '存储');
INSERT INTO `gwfield` VALUES ('10', '10', 'sjfl', '0', '存储');
INSERT INTO `gwfield` VALUES ('11', '10', 'load_time', '0', '存储');

-- ----------------------------
-- Table structure for `jobinf`
-- ----------------------------
DROP TABLE IF EXISTS `jobinf`;
CREATE TABLE `jobinf` (
  `JOBID` bigint(20) NOT NULL AUTO_INCREMENT,
  `JOBNAME` varchar(64) NOT NULL,
  `JOBGROUP` varchar(64) NOT NULL,
  `JOBSTATUS` int(11) NOT NULL COMMENT '1未执行，2正在执行',
  `CRONEXPRESSION` varchar(225) NOT NULL,
  `BEANCLASS` varchar(125) NOT NULL,
  `DESCRIPTION` longtext,
  `TRIGGERNAME` varchar(64) NOT NULL,
  `TRIGGERGROUPNAME` varchar(64) NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `indexpath` varchar(200) NOT NULL,
  `sqlip` varchar(50) NOT NULL,
  `sqluser` varchar(50) NOT NULL,
  `sqlpw` varchar(50) NOT NULL,
  `sqldb` varchar(50) NOT NULL,
  `sqlport` int(11) NOT NULL,
  `sqltable` varchar(50) NOT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobinf
-- ----------------------------
INSERT INTO `jobinf` VALUES ('10', '数据推送', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME测试2', 'TRIGGERGROUP_NAME', '2016-11-07 22:34:29', '2016-11-08 00:02:10', 'D:/indexpath', '127.0.0.1', 'root', 'root', 'jtcrawler', '3306', 'crawler_xq');

-- ----------------------------
-- Table structure for `joblog`
-- ----------------------------
DROP TABLE IF EXISTS `joblog`;
CREATE TABLE `joblog` (
  `JOBID` bigint(20) NOT NULL,
  `STATUS` int(11) NOT NULL COMMENT '0成功，1失败',
  `INDEXSIZE` int(11) DEFAULT NULL,
  `EXETIME` bigint(20) DEFAULT NULL,
  `START` datetime DEFAULT NULL,
  `END` datetime DEFAULT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of joblog
-- ----------------------------

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
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  KEY `FKFA40A9C990831BD2` (`PID`),
  CONSTRAINT `FKFA40A9C990831BD2` FOREIGN KEY (`PID`) REFERENCES `keyword` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------
