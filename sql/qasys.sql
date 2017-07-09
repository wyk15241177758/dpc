/*
Navicat MySQL Data Transfer

Source Server         : mariadb
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-03-23 10:09:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gwfield`
-- ----------------------------
DROP TABLE IF EXISTS `gwfield`;
CREATE TABLE `gwfield` (
  `fieldid` int(11) NOT NULL AUTO_INCREMENT,
  `jobid` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `tablekey` tinyint(10) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`fieldid`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gwfield
-- ----------------------------
INSERT INTO `gwfield` VALUES ('12', '11', 'ID', '1', '存储');
INSERT INTO `gwfield` VALUES ('13', '11', 'SEARCHCONTENT', '0', '存储');
INSERT INTO `gwfield` VALUES ('14', '11', 'SEARCHTIMES', '0', '存储');
INSERT INTO `gwfield` VALUES ('15', '11', 'CREATETIME', '0', '存储');
INSERT INTO `gwfield` VALUES ('16', '11', 'UPDATETIME', '0', '存储');
INSERT INTO `gwfield` VALUES ('17', '12', 'XQ_ID', '1', '存储');
INSERT INTO `gwfield` VALUES ('18', '12', 'XQ_TITLE', '0', '存储');
INSERT INTO `gwfield` VALUES ('19', '12', 'XQ_URL', '0', '存储');
INSERT INTO `gwfield` VALUES ('20', '12', 'LM_NAME', '0', '存储');
INSERT INTO `gwfield` VALUES ('21', '12', 'ZD_NAME', '0', '存储');
INSERT INTO `gwfield` VALUES ('22', '12', 'SJFL', '0', '存储');
INSERT INTO `gwfield` VALUES ('23', '12', 'LOAD_TIME', '0', '存储');
INSERT INTO `gwfield` VALUES ('24', '12', 'KEY_WORD', '0', '存储');
INSERT INTO `gwfield` VALUES ('25', '12', 'XQ_PUDATE', '0', '存储');
INSERT INTO `gwfield` VALUES ('26', '12', 'ZDLM_URL', '0', '存储');

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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobinf
-- ----------------------------
INSERT INTO `jobinf` VALUES ('11', '检索历史', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME检索历史', 'TRIGGERGROUP_NAME', '2017-01-10 22:09:59', null, '/indexpath_searchHis', '127.0.0.1', 'root', 'root', 'qasys', '3306', 'searchhis');
INSERT INTO `jobinf` VALUES ('12', '爬虫采集数据推送', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME测试使用', 'TRIGGERGROUP_NAME', '2017-01-16 10:42:20', '2017-03-09 11:11:19', '/indexpath', '127.0.0.1', 'root', 'root', 'jtcrawler', '3306', 'viewforsearch');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `parsehotword`
-- ----------------------------
DROP TABLE IF EXISTS `parsehotword`;
CREATE TABLE `parsehotword` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `HOTWORD` varchar(255) DEFAULT NULL,
  `NUM` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of parsehotword
-- ----------------------------

-- ----------------------------
-- Table structure for `rekeyword`
-- ----------------------------
DROP TABLE IF EXISTS `rekeyword`;
CREATE TABLE `rekeyword` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `kids` varchar(255) DEFAULT NULL,
  `kid` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `scene`
-- ----------------------------
DROP TABLE IF EXISTS `scene`;
CREATE TABLE `scene` (
  `SCENEID` int(11) NOT NULL AUTO_INCREMENT,
  `SCENENAME` varchar(64) NOT NULL,
  `CREATETIME` datetime NOT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  PRIMARY KEY (`SCENEID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `scenepage`
-- ----------------------------
DROP TABLE IF EXISTS `scenepage`;
CREATE TABLE `scenepage` (
  `scenePageId` int(11) NOT NULL AUTO_INCREMENT,
  `sceneWordId` int(11) NOT NULL,
  `pageTitle` varchar(300) NOT NULL,
  `pageLink` varchar(300) NOT NULL,
  `sjfl` varchar(500) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `HTML` longtext,
  PRIMARY KEY (`scenePageId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sceneword`
-- ----------------------------
DROP TABLE IF EXISTS `sceneword`;
CREATE TABLE `sceneword` (
  `sceneWordId` int(11) NOT NULL AUTO_INCREMENT,
  `sceneId` int(11) NOT NULL,
  `sceneName` varchar(64) NOT NULL,
  `enterWords` varchar(500) NOT NULL,
  `outWords` varchar(500) NOT NULL,
  `createTime` datetime NOT NULL,
  `updateTime` datetime DEFAULT NULL,
  `SJFL` longtext,
  PRIMARY KEY (`sceneWordId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `searchhis`
-- ----------------------------
DROP TABLE IF EXISTS `searchhis`;
CREATE TABLE `searchhis` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTENTMD5` varchar(50) NOT NULL,
  `SEARCHCONTENT` varchar(100) NOT NULL,
  `SEARCHTIMES` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;