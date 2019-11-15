/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50528
 Source Host           : localhost:3306
 Source Schema         : crawler

 Target Server Type    : MySQL
 Target Server Version : 50528
 File Encoding         : 65001

 Date: 15/11/2019 16:43:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jd_item
-- ----------------------------

CREATE TABLE `jd_item`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `spu` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品集合id',
  `sku` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品最小品类单元id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品标题',
  `price` bigint(10) NULL DEFAULT NULL COMMENT '商品价格',
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品详情地址',
  `created` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updated` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
