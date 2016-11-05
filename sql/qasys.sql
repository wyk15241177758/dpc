/*
Navicat MySQL Data Transfer

Source Server         : 127
Source Server Version : 50715
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50715
File Encoding         : 65001

Date: 2016-11-05 09:28:23
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
  `iskey` tinyint(10) NOT NULL,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`fieldid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gwfield
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jobinf
-- ----------------------------
INSERT INTO `jobinf` VALUES ('1', '数据推送', 'JOBGROUP_NAME', '1', '0 0/30 * * * ?', 'com.jt.gateway.service.operation.IndexTask', '', 'TRIGGER_NAME数据推送', 'TRIGGERGROUP_NAME', '2016-11-02 14:28:13', '2016-11-02 14:28:13', '', '', '', '', '', '0', '');

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
INSERT INTO `joblog` VALUES ('1', '0', '0', '0', '2016-11-02 17:30:00', null);
