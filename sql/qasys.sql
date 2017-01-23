/*
Navicat MySQL Data Transfer

Source Server         : mariadb
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-01-23 18:16:29
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

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
INSERT INTO `jobinf` VALUES ('12', '测试使用', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', null, 'TRIGGER_NAME测试使用', 'TRIGGERGROUP_NAME', '2017-01-16 10:42:20', '2017-01-18 13:45:33', '/indexpath', '127.0.0.1', 'root', 'root', 'jtcrawler', '3306', 'crawler_xq');

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
INSERT INTO `joblog` VALUES ('11', '1', '33', '1394', '2017-01-23 17:30:00', '2017-01-23 17:30:01');
INSERT INTO `joblog` VALUES ('12', '1', '2506', '5563', '2017-01-23 17:30:00', '2017-01-23 17:30:05');

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
-- Records of rekeyword
-- ----------------------------
INSERT INTO `rekeyword` VALUES ('1', '2', '1');
INSERT INTO `rekeyword` VALUES ('2', '2', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of scenepage
-- ----------------------------
INSERT INTO `scenepage` VALUES ('3', '3', '市长介绍', 'http://www.bing.com', '教育', '2017-01-19 17:31:16', '2017-01-19 17:31:16');

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
-- Records of sceneword
-- ----------------------------
INSERT INTO `sceneword` VALUES ('1', '1', 'test', '入口词1;入口词21111', '出口词1;出口词2', '2016-12-06 23:34:18', '2016-12-10 19:43:18', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('2', '1', 'test', '11入口词', '出口词1;出口词2;出口词3', '2016-12-06 23:48:00', '2016-12-10 20:01:15', '走进岳阳;政务知识');
INSERT INTO `sceneword` VALUES ('3', '1', 'test', '市长', '市长', '2016-12-11 17:57:32', '2017-01-19 17:31:16', '军事;教育');
INSERT INTO `sceneword` VALUES ('4', '1', 'test', '22入口词22入口词22入口词22入口词22入口词22入口词', '33出口词', '2016-12-11 22:05:36', '2016-12-11 22:06:14', '走进岳阳;政务知识');

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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of searchhis
-- ----------------------------
INSERT INTO `searchhis` VALUES ('1', 'ef8c4dbc99fb66ca363431efc2d9fc1d', '如何办理结婚证1222', '3', '2016-12-18 11:40:44', '2017-01-08 19:52:33');
INSERT INTO `searchhis` VALUES ('2', 'aea36414f66794fb620cd05cfb55c3b2', '如何办理结婚证311', '3', '2016-12-19 11:40:44', '2017-01-08 19:54:02');
INSERT INTO `searchhis` VALUES ('3', 'e54ecf5b779d28e5940082caeb7f4d9a', '如何办理结婚证41', '3', '2016-12-20 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('4', 'c3e6ce38bf389bd5971c19eb21a28bfd', '如何办理结婚证4', '3', '2016-12-21 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('5', '25d037196fb19a3984b9927f2dea73f8', '如何办理结婚证511', '3', '2016-12-22 11:40:44', '2017-01-16 12:38:57');
INSERT INTO `searchhis` VALUES ('6', 'a99bedb1f06fa1a3eaf08428eebd4e59', '如何办理结婚证6', '3', '2016-12-23 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('7', 'e4216291741feb1ababaef47d306441', '如何办理结婚证7', '3', '2016-12-24 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('8', '635fb078de13040b4f047a89b7ad7374', '如何办理结婚证8', '3', '2016-12-25 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('9', '7975299ca1debd5b291ee4abd155c21e', '如何办理结婚证9', '3', '2016-12-26 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('11', '9d3ea557c2558fc1f8a178b9f52a0ddc', '如何办理结婚证0', '3', '2016-12-27 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('12', '1b3543d0db33931e7211dabe42142060', '如何办理结婚证1', '3', '2016-12-28 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('13', 'a3577c6ff330b1a930dee2d923c96fb1', '如何办理结婚证10', '3', '2016-12-29 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('14', 'e26d405434e2499d8defb8eafe44fff1', '如何办理结婚证11', '3', '2016-12-30 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('15', 'f0f4837b9ff4b2272116cd04fc75798b', '如何办理结婚证12', '3', '2017-01-01 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('16', '9d7920eb45603466c6c749a13ed74204', '如何办理结婚证13', '3', '2017-01-02 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('17', '15f5a28611b8f875a99b653d6c4c837', '如何办理结婚证14', '3', '2017-01-03 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('18', '8ef57bb05fe406d1d27024ec2e0ec17c', '如何办理结婚证1511', '3', '2017-01-04 11:40:44', '2017-01-08 16:44:38');
INSERT INTO `searchhis` VALUES ('19', 'ee6c7eae076dc6df98e5ef1acad0d5f8', '如何办理结婚证16', '3', '2017-01-05 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('20', '557076c782743485f50940fb91f85c1d', '如何办理结婚证17', '3', '2017-01-06 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('21', '4209337d5db223046583cf56ea7a1667', '如何办理结婚证18', '3', '2017-01-07 11:40:44', '2017-01-08 16:24:01');
INSERT INTO `searchhis` VALUES ('22', '9ad306eb929704dfdf198bf348c999e3', '11', '123', '2017-01-08 19:54:19', '2017-01-23 14:17:50');
INSERT INTO `searchhis` VALUES ('23', '601c2f5d296e69e0d6dde15974cf0551', '222', '0', '2017-01-08 20:24:41', '2017-01-23 14:17:54');
INSERT INTO `searchhis` VALUES ('24', '8d3be777d321224eaf18c15ff2a846df', '区长信箱', '15', '2017-01-11 22:53:58', '2017-01-18 13:43:28');
INSERT INTO `searchhis` VALUES ('25', 'f3713339c5733af42efc0a49dfba63d4', '区长', '10', '2017-01-11 22:54:08', '2017-01-18 13:47:02');
INSERT INTO `searchhis` VALUES ('26', 'f60490df2e844498f3106f90b702d31d', '信箱', '5', '2017-01-11 22:54:15', '2017-01-23 14:17:57');
INSERT INTO `searchhis` VALUES ('27', 'fadcdccb8e50e9200d5480b27a8042d9', '拱墅', '4', '2017-01-18 13:47:01', '2017-01-18 13:47:01');
INSERT INTO `searchhis` VALUES ('28', '62a30fa44f930948996869af1d922fa', '人大常委', '1', '2017-01-18 13:47:02', '2017-01-18 13:47:02');
INSERT INTO `searchhis` VALUES ('29', '45b37f4a75366cc5cc83453b544113ca', '市长', '1', '2017-01-18 18:33:51', '2017-01-18 18:33:51');
INSERT INTO `searchhis` VALUES ('30', '8e775747e52a6a9d9bad05ffbcf30ce5', '拱墅2', '1', '2017-01-18 18:33:51', '2017-01-18 18:33:51');
INSERT INTO `searchhis` VALUES ('31', '321199804020f3c5d5b9308559746112', '市长2', '1', '2017-01-19 13:43:48', '2017-01-19 13:43:48');
INSERT INTO `searchhis` VALUES ('32', '3c28c29ccccfd1e1468f6ebed5c3d83b', '拱墅3', '1', '2017-01-19 13:43:48', '2017-01-19 13:43:48');
INSERT INTO `searchhis` VALUES ('33', '45b37f4a75366cc5cc83453b544113ca', '市长', '1', '2017-01-19 15:21:57', '2017-01-23 17:30:07');
INSERT INTO `searchhis` VALUES ('34', 'f7584a7622f8fe725582df8efbc7bb73', '拱墅222111121231', '121', '2017-01-19 15:21:57', '2017-01-23 17:25:23');
