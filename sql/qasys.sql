/*
Navicat MySQL Data Transfer

Source Server         : mariadb
Source Server Version : 50505
Source Host           : localhost:3307
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-01-10 16:06:20
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
INSERT INTO `jobinf` VALUES ('10', '数据推送', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME测试2', 'TRIGGERGROUP_NAME', '2016-11-07 22:34:29', '2017-01-02 16:26:37', '/indexpath', '127.0.0.1', 'root', 'root', 'jtcrawler', '3306', 'crawler_xq');

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
INSERT INTO `joblog` VALUES ('10', '1', '1607', '2632', '2017-01-04 09:51:12', '2017-01-04 09:51:15');

-- ----------------------------
-- Table structure for `job_inf`
-- ----------------------------
DROP TABLE IF EXISTS `job_inf`;
CREATE TABLE `job_inf` (
  `jobId` int(11) NOT NULL AUTO_INCREMENT,
  `jobName` varchar(64) NOT NULL,
  `jobGroup` varchar(64) NOT NULL,
  `jobStatus` int(11) NOT NULL,
  `cronExpression` varchar(225) NOT NULL,
  `beanClass` varchar(125) NOT NULL,
  `description` blob,
  `triggerName` varchar(64) NOT NULL,
  `triggerGroupName` varchar(64) NOT NULL,
  `createTime` datetime NOT NULL,
  `updateTime` datetime NOT NULL,
  PRIMARY KEY (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of job_inf
-- ----------------------------
INSERT INTO `job_inf` VALUES ('1', '智能问答数据抽取', 'EXTJWEB_JOBGROUP_NAME', '1', '0 0/1 * * * ?', 'com.jt.gateway.service.operation.IndexTask', 0x74657374, 'TRIGGER_NAME', 'TRIGGERGROUP_NAME', '0000-00-00 00:00:00', '0000-00-00 00:00:00');

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
-- Records of scene
-- ----------------------------
INSERT INTO `scene` VALUES ('1', 'test', '2016-12-05 22:37:32', null);
INSERT INTO `scene` VALUES ('2', 'test2', '2016-11-15 20:55:21', null);

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
  PRIMARY KEY (`sceneWordId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sceneword
-- ----------------------------
INSERT INTO `sceneword` VALUES ('1', '1', 'test', '入口词1;入口词21111', '出口词1;出口词2', '2016-12-06 23:34:18', '2016-12-10 19:43:18');
INSERT INTO `sceneword` VALUES ('2', '1', 'test', '11入口词', '出口词1;出口词2;出口词3', '2016-12-06 23:48:00', '2016-12-10 20:01:15');
INSERT INTO `sceneword` VALUES ('3', '1', 'test', '南阳', '市长', '2016-12-11 17:57:32', '2016-12-11 17:58:22');
INSERT INTO `sceneword` VALUES ('4', '1', 'test', '22入口词22入口词22入口词22入口词22入口词22入口词', '33出口词', '2016-12-11 22:05:36', '2016-12-11 22:06:14');
INSERT INTO `sceneword` VALUES ('5', '1', 'test', '333', 'fdsa', '2016-12-11 22:11:48', null);
INSERT INTO `sceneword` VALUES ('6', '1', 'test', '居住证', '居住证', '2017-01-04 09:54:47', null);
INSERT INTO `sceneword` VALUES ('7', '1', 'test', '结婚证', '结婚证', '2017-01-04 09:55:23', null);

-- ----------------------------
-- Table structure for `searchhis`
-- ----------------------------
DROP TABLE IF EXISTS `searchhis`;
CREATE TABLE `searchhis` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SEARCHCONTENT` varchar(255) NOT NULL,
  `SEARCHTIMES` int(11) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of searchhis
-- ----------------------------
