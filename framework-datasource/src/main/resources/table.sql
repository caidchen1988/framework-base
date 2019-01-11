/*
Navicat MySQL Data Transfer

Source Server         : 本地数据库
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : test1

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2019-01-04 17:48:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `cou_no` varchar(10) DEFAULT NULL COMMENT '课程号',
  `cou_name` varchar(150) DEFAULT NULL COMMENT '课程名称',
  `teach_no` varchar(10) DEFAULT NULL COMMENT '教师编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` int(32) NOT NULL,
  `stu_no` varchar(10) DEFAULT NULL COMMENT '学号',
  `cou_no` varchar(10) DEFAULT NULL COMMENT '课程号',
  `score` decimal(4,1) DEFAULT NULL COMMENT '成绩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `stu_no` varchar(20) DEFAULT NULL COMMENT '学生编号',
  `stu_name` varchar(50) DEFAULT NULL COMMENT '学生姓名',
  `stu_sex` char(2) DEFAULT NULL,
  `stu_birthday` date DEFAULT NULL COMMENT '学生生日',
  `stu_class` char(2) DEFAULT NULL COMMENT '学生所在班级',
  PRIMARY KEY (`id`),
  KEY `index_1` (`stu_class`) USING BTREE,
  KEY `index_2` (`stu_birthday`) USING BTREE,
  KEY `index_3` (`stu_sex`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000077 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `teach_no` varchar(10) DEFAULT NULL COMMENT '教师编号',
  `teach_name` varchar(100) DEFAULT NULL COMMENT '教师姓名',
  `teach_sex` char(2) DEFAULT NULL COMMENT '教师性别',
  `teach_birthday` date DEFAULT NULL COMMENT '教师生日',
  `teach_depart` varchar(150) DEFAULT NULL COMMENT '教师所在部门',
  `teach_prof` varchar(100) DEFAULT NULL COMMENT '教师职称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12346 DEFAULT CHARSET=utf8;
