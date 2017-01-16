/*
Navicat MySQL Data Transfer

Source Server         : 127
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2017-01-16 00:26:42
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gwfield
-- ----------------------------
INSERT INTO `gwfield` VALUES ('12', '11', 'ID', '1', '存储');
INSERT INTO `gwfield` VALUES ('13', '11', 'SEARCHCONTENT', '0', '存储');
INSERT INTO `gwfield` VALUES ('14', '11', 'SEARCHTIMES', '0', '存储');
INSERT INTO `gwfield` VALUES ('15', '11', 'CREATETIME', '0', '存储');
INSERT INTO `gwfield` VALUES ('16', '11', 'UPDATETIME', '0', '存储');

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobinf
-- ----------------------------
INSERT INTO `jobinf` VALUES ('11', '检索历史', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME检索历史', 'TRIGGERGROUP_NAME', '2017-01-10 22:09:59', null, '/indexpath_searchHis', '127.0.0.1', 'root', 'root', 'qasys', '3306', 'searchhis');

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
INSERT INTO `joblog` VALUES ('6', '0', '0', '0', '2016-11-07 21:30:00', null);
INSERT INTO `joblog` VALUES ('9', '0', '0', '0', '2016-11-07 22:30:00', null);
INSERT INTO `joblog` VALUES ('10', '1', '10937', '5860', '2017-01-09 23:00:00', '2017-01-09 23:00:06');
INSERT INTO `joblog` VALUES ('11', '1', '25', '520', '2017-01-16 00:00:00', '2017-01-16 00:00:00');

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
-- Records of keyword
-- ----------------------------
INSERT INTO `keyword` VALUES ('1', null, '测试', null, '0', '0', '2016-11-15 11:05:12', '2016-11-15 11:05:12');
INSERT INTO `keyword` VALUES ('2', null, '测试2', null, '0', '0', '2016-11-15 11:05:33', '2016-11-15 11:05:33');
INSERT INTO `keyword` VALUES ('3', '1', '郑晓彬', null, '1', '0', '2016-11-15 11:05:54', '2016-11-15 11:05:54');
INSERT INTO `keyword` VALUES ('4', '1', '郑晓彬01', null, '1', '0', '2016-11-15 11:06:07', '2016-11-15 11:06:07');

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
  PRIMARY KEY (`scenePageId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of scenepage
-- ----------------------------
INSERT INTO `scenepage` VALUES ('1', '5', '区长介绍', 'http://www.bing.com', '政务知识;新闻中心', '2017-01-15 23:02:54', '2017-01-15 23:02:59');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sceneword
-- ----------------------------
INSERT INTO `sceneword` VALUES ('1', '1', 'test', '入口词1;入口词21111', '出口词1;出口词2', '2016-12-06 23:34:18', '2016-12-10 19:43:18', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('2', '1', 'test', '11入口词', '出口词1;出口词2;出口词3', '2016-12-06 23:48:00', '2016-12-10 20:01:15', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('3', '1', 'test', '南阳', '市长', '2016-12-11 17:57:32', '2016-12-11 17:58:22', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('4', '1', 'test', '22入口词22入口词22入口词22入口词22入口词22入口词', '33出口词', '2016-12-11 22:05:36', '2016-12-11 22:06:14', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('5', '1', 'test', '333', 'fdsa', '2016-12-11 22:11:48', null, '走进岳阳;政务知识');

-- ----------------------------
-- Table structure for `searchhis`
-- ----------------------------
DROP TABLE IF EXISTS `searchhis`;
CREATE TABLE `searchhis` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SEARCHCONTENT` varchar(100) NOT NULL,
  `SEARCHTIMES` int(11) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of searchhis
-- ----------------------------
INSERT INTO `searchhis` VALUES ('1', '如何办理结婚证1222', '3', '2016-12-18 11:40:44', '2017-01-08 19:52:33');
INSERT INTO `searchhis` VALUES ('2', '如何办理结婚证311', '3', '2016-12-19 11:40:44', '2017-01-08 19:54:02');
INSERT INTO `searchhis` VALUES ('3', '如何办理结婚证4', '3', '2016-12-20 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('4', '如何办理结婚证4', '3', '2016-12-21 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('5', '如何办理结婚证5', '3', '2016-12-22 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('6', '如何办理结婚证6', '3', '2016-12-23 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('7', '如何办理结婚证7', '3', '2016-12-24 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('8', '如何办理结婚证8', '3', '2016-12-25 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('9', '如何办理结婚证9', '3', '2016-12-26 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('11', '如何办理结婚证1', '3', '2016-12-27 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('12', '如何办理结婚证1', '3', '2016-12-28 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('13', '如何办理结婚证10', '3', '2016-12-29 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('14', '如何办理结婚证11', '3', '2016-12-30 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('15', '如何办理结婚证12', '3', '2017-01-01 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('16', '如何办理结婚证13', '3', '2017-01-02 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('17', '如何办理结婚证14', '3', '2017-01-03 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('18', '如何办理结婚证1511', '3', '2017-01-04 11:40:44', '2017-01-08 16:44:38');
INSERT INTO `searchhis` VALUES ('19', '如何办理结婚证16', '3', '2017-01-05 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('20', '如何办理结婚证17', '3', '2017-01-06 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('21', '如何办理结婚证18', '3', '2017-01-07 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('22', '11', '123', '2017-01-08 19:54:19', null);
INSERT INTO `searchhis` VALUES ('23', '222', '0', '2017-01-08 20:24:41', null);
INSERT INTO `searchhis` VALUES ('24', '区长信箱', '14', '2017-01-11 22:53:58', '2017-01-11 23:31:51');
INSERT INTO `searchhis` VALUES ('25', '区长', '8', '2017-01-11 22:54:08', null);
INSERT INTO `searchhis` VALUES ('26', '信箱', '5', '2017-01-11 22:54:15', null);
