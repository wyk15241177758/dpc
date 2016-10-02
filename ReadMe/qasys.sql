/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : qasys

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2016-10-02 16:45:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `jobinf`
-- ----------------------------
DROP TABLE IF EXISTS `jobinf`;
CREATE TABLE `jobinf` (
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
-- Table structure for `joblog`
-- ----------------------------
DROP TABLE IF EXISTS `joblog`;
CREATE TABLE `joblog` (
  `JOBID` bigint(20) NOT NULL,
  `STATUS` int(11) NOT NULL,
  `EXETIME` bigint(20) DEFAULT NULL,
  `START` datetime DEFAULT NULL,
  `END` datetime DEFAULT NULL,
  `INDEXSIZE` int(11) DEFAULT NULL,
  PRIMARY KEY (`JOBID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of joblog
-- ----------------------------
