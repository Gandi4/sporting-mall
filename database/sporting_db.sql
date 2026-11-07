/*
 Navicat Premium Dump SQL

 Source Server         : wuzi_db
 Source Server Type    : MySQL
 Source Server Version : 80400 (8.4.0)
 Source Host           : localhost:3306
 Source Schema         : sporting_db

 Target Server Type    : MySQL
 Target Server Version : 80400 (8.4.0)
 File Encoding         : 65001

 Date: 31/05/2026 15:40:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人手机号',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint NOT NULL DEFAULT 0 COMMENT '是否默认地址：1是，0否',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_address_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 1, '张三', '13800010001', '北京市', '北京市', '朝阳区', '建国门外大街1号', 1, '2025-12-17 17:03:44');
INSERT INTO `address` VALUES (2, 2, 'lisi', '13800010002', 'A', 'A', 'A', 'A', 1, '2026-04-01 14:12:47');
INSERT INTO `address` VALUES (4, 3, 'kokomi', '1', '1', '11111', '11111', '11111', 1, '2026-04-06 16:34:39');
INSERT INTO `address` VALUES (5, 4, '１１１', '１１１', '１', '１', '１', '１', 1, '2026-04-29 16:50:58');
INSERT INTO `address` VALUES (6, 3, '222', '222', '22', '22', '22', '2', 0, '2026-05-06 20:01:48');
INSERT INTO `address` VALUES (7, 5, '1', '1', '1', '1', '1', '1', 1, '2026-05-12 15:28:34');
INSERT INTO `address` VALUES (8, 3, '333', '3', '3', '3', '3', '3', 0, '2026-05-13 08:33:04');

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `publish_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '1发布，0草稿',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '【阶段一】系统基石：分页与服务端架构打通', '本项目初期已全面打通数据链路：\n1. 分页规范：基于 MyBatis-Plus 实现了 Service 层的深度封装，解决了传统分页参数丢失导致的接口报错问题。\n2. 接口解耦：完成了从后端 ServiceImpl 逻辑到 Controller 接入的标准化流程，确保了前台商品展示的流畅性。\n3. 调试体系：建立了完善的参数校验机制，为后续复杂业务开发奠定了坚实地基。', '2025-12-17 10:00:00', 1);
INSERT INTO `announcement` VALUES (2, '【阶段二】架构升级：实体类规范化与底层建模', '为了提升系统可维护性，我们完成了底层模型的重构：\n1. 映射标准：严格遵循 MyBatis-Plus 规范，对 @TableName、@TableId 等核心注解进行了严谨配置，实现了数据库表与 Java Entity 的 1:1 精准映射。\n2. 命名工程：统一了数据库下划线与 Java 驼峰命名转换，确保了管理员、操作日志等核心业务数据的读写一致性，极大提升了代码的工业级健壮度。', '2026-02-15 14:00:00', 1);
INSERT INTO `announcement` VALUES (3, '【阶段三】交互创新：搜索排序与全链路购物闭环', '商城核心交易系统正式上线：\n1. 智能搜索：支持商品名模糊匹配、分类筛选，并新增价格动态排序（升序/降序）功能。\n2. 购物流程：完成了从“购物车-订单预览-多地址管理-支付确认”的全链路开发，解决了跨域 Cookie 凭证共享与 Token 管理的难题。\n3. 视觉规范：全站 UI 对齐 1200px 工业标准，引入“SPORTING 橙”主题色，统一了前后台交互体验。', '2026-03-25 09:30:00', 1);
INSERT INTO `announcement` VALUES (4, '【阶段四】重磅更新：15阶证道体系与AI智能助理', '本系统引入了两大创新模块：\n1. 修炼境界系统：独创 15 阶会员成长体系（灵胎至天庭）。通过每日登录（7天循环奖励）、消费（1灵石=1000修为）及评论获取经验，天宫境以上用户尊享金色专属勋章。\n2. AI 客服集成：对接 DeepSeek V3 大语言模型，针对运动装备提供 24h 智能咨询，通过后端 RestTemplate 封装实现了高效的非阻塞异步对话体验。', '2026-04-01 12:00:00', 1);
INSERT INTO `announcement` VALUES (5, '【阶段五】系统加固：数据治理与视觉稳定性修复', '针对内测阶段的问题进行了全面修复：\n1. 数据治理：重命名 order 核心表并解决 MySQL 保留字冲突；修复了 Long/Integer 类型转换导致的 500 报错，确保境界数据在全站实时同步。\n2. 资源优化：全量映射 Unsplash 高清 CDN 资源，通过 el-image 实现图损自动保护逻辑，解决了国外图片连接中断导致的页面白屏问题。\n3. 售后闭环：上线自主退换货模块，支持用户在线申请、管理员后台一键审批，完成了电商闭环的最后一块拼图。', '2026-04-07 15:00:00', 1);

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '点击跳转链接',
  `sort_order` int NULL DEFAULT 0,
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '1显示，0隐藏',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '首页轮播图' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '极致健身', 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=1600', NULL, 1, 1, '2026-04-02 10:07:42');
INSERT INTO `banner` VALUES (2, '跑鞋', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=1600', NULL, 2, 1, '2026-04-02 10:07:42');
INSERT INTO `banner` VALUES (3, '三星 Watch4C', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=1600', NULL, 4, 1, '2026-04-02 10:07:42');
INSERT INTO `banner` VALUES (4, '瑜伽生活', 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1600', NULL, 5, 1, '2026-04-02 10:07:42');
INSERT INTO `banner` VALUES (5, '篮球时刻', 'https://images.unsplash.com/photo-1546519638-68e109498ffc?q=80&w=1600', NULL, 6, 1, '2026-04-29 19:25:49');
INSERT INTO `banner` VALUES (6, '骑行无阻', 'https://images.unsplash.com/photo-1485965120184-e220f721d03e?q=80&w=1600', NULL, 7, 1, '2026-04-29 19:25:49');
INSERT INTO `banner` VALUES (7, '测试1', 'http://localhost:80/uploads/1778127011528_Konachan (1).png', NULL, 1, 1, '2026-05-07 12:10:17');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_cart_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cart_chk_1` CHECK (`quantity` > 0)
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, 1, 1, 2, '2025-12-17 17:03:44', '2025-12-17 17:03:44');
INSERT INTO `cart` VALUES (10, 2, 4, 1, '2026-04-29 16:49:46', '2026-04-29 16:49:46');
INSERT INTO `cart` VALUES (11, 2, 14, 1, '2026-04-29 16:49:52', '2026-04-29 16:49:52');
INSERT INTO `cart` VALUES (12, 2, 6, 1, '2026-04-29 16:51:04', '2026-04-29 16:51:04');
INSERT INTO `cart` VALUES (13, 2, 15, 2, '2026-04-29 16:51:09', '2026-04-29 16:51:09');
INSERT INTO `cart` VALUES (16, 4, 6, 1, '2026-04-29 20:19:10', '2026-04-29 20:19:10');
INSERT INTO `cart` VALUES (17, 4, 10, 1, '2026-04-29 20:19:16', '2026-04-29 20:19:16');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父分类ID',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '电子产品', NULL, 1, 1);
INSERT INTO `category` VALUES (2, '球类用品', NULL, 1, 1);
INSERT INTO `category` VALUES (3, '球拍类', NULL, 2, 1);
INSERT INTO `category` VALUES (4, '鞋类', NULL, 3, 1);
INSERT INTO `category` VALUES (5, '瑜伽健身', NULL, 4, 1);
INSERT INTO `category` VALUES (6, '力量训练', NULL, 5, 1);
INSERT INTO `category` VALUES (7, '游泳用品', NULL, 6, 1);
INSERT INTO `category` VALUES (8, '运动配件', NULL, 7, 1);
INSERT INTO `category` VALUES (9, '跳绳有氧', NULL, 8, 1);
INSERT INTO `category` VALUES (10, '护具系列', NULL, 9, 1);
INSERT INTO `category` VALUES (11, '智能穿戴', NULL, 10, 1);
INSERT INTO `category` VALUES (12, '其他分类', NULL, 11, 1);
INSERT INTO `category` VALUES (13, '测试分类', NULL, 0, 1);

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (3, 3, 4, '2026-04-06 16:18:38');
INSERT INTO `favorite` VALUES (5, 4, 2, '2026-04-29 17:00:29');
INSERT INTO `favorite` VALUES (7, 3, 15, '2026-05-07 18:20:20');
INSERT INTO `favorite` VALUES (8, 3, 3, '2026-05-13 08:32:22');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `manager` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '管理员密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_manager`(`manager` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES (1, 'admin', '123456');
INSERT INTO `manager` VALUES (2, 'admin2', '123456');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL,
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `address_snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址快照(String存储)',
  `status` enum('pending_payment','paid','shipped','delivered','completed','canceled','refunded') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending_payment' COMMENT '订单状态',
  `payment_method` enum('wechat','alipay','bank_card','balance') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `paid_at` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_order_user_status`(`user_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_order_created`(`created_at` ASC) USING BTREE,
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, '202312150001', 1, 9999.00, 0.00, 9999.00, '{\"city\": \"北京市\", \"phone\": \"13800010001\", \"district\": \"朝阳区\", \"province\": \"北京市\", \"receiver_name\": \"张三\", \"detail_address\": \"建国门外大街1号\"}', 'completed', 'alipay', '2023-12-15 14:30:00', '2025-12-17 17:03:44', NULL);
INSERT INTO `order` VALUES (2, 'SN1775098413691', 2, 11298.00, 0.00, 11298.00, 'lisi 13800010002 AAAA', 'paid', NULL, NULL, '2026-04-02 10:53:34', NULL);
INSERT INTO `order` VALUES (5, 'SN1775536924926', 3, 858.00, 0.00, 858.00, 'kokomi 1 1111111111111111', 'paid', NULL, NULL, '2026-04-07 12:42:05', NULL);
INSERT INTO `order` VALUES (6, 'SN1775623063840', 3, 10898.00, 0.00, 10898.00, 'kokomi 1 1111111111111111', 'paid', NULL, NULL, '2026-04-08 12:37:44', NULL);
INSERT INTO `order` VALUES (7, 'SN1777445647189', 2, 2299.00, 0.00, 2299.00, 'lisi 13800010002 AAAA', 'paid', NULL, NULL, '2026-04-29 14:54:07', NULL);
INSERT INTO `order` VALUES (8, 'SN1777461683459', 4, 2499.00, 0.00, 2499.00, '１１１ １１１ １１１１', 'paid', NULL, NULL, '2026-04-29 19:21:23', NULL);
INSERT INTO `order` VALUES (9, 'SN1778126923464', 3, 1899.00, 0.00, 1899.00, 'kokomi 1 1111111111111111', 'paid', NULL, NULL, '2026-05-07 12:08:43', NULL);
INSERT INTO `order` VALUES (10, 'SN1778136937042', 3, 699.00, 0.00, 699.00, '222 222 2222222', 'paid', NULL, NULL, '2026-05-07 14:55:37', NULL);
INSERT INTO `order` VALUES (11, 'SN1778136970982', 3, 19.00, 0.00, 19.00, '222 222 2222222', 'paid', NULL, NULL, '2026-05-07 14:56:11', NULL);
INSERT INTO `order` VALUES (12, 'SN1778149293605', 3, 318.00, 0.00, 318.00, '222 222 2222222', 'paid', NULL, NULL, '2026-05-07 18:21:34', NULL);
INSERT INTO `order` VALUES (13, 'SN1778570918621', 5, 199.00, 0.00, 199.00, '1 1 1111', 'paid', NULL, NULL, '2026-05-12 15:28:39', NULL);
INSERT INTO `order` VALUES (14, 'SN1778632352298', 3, 299.00, 0.00, 299.00, 'kokomi 1 1111111111111111', 'paid', NULL, NULL, '2026-05-13 08:32:32', NULL);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称快照',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NOT NULL COMMENT '下单时单价',
  `quantity` int NOT NULL,
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 1, 'iPhone 15 Pro Max', 'https://example.com/iphone15.jpg', 9999.00, 1, 9999.00);
INSERT INTO `order_item` VALUES (2, 2, 1, 'iPhone 15 Pro Max', 'https://img14.360buyimg.com/n1/jfs/t1/159495/10/40960/45579/65000570F47926955/751759530495f57a.jpg', 9999.00, 1, 9999.00);
INSERT INTO `order_item` VALUES (3, 2, 18, '运动手表', 'https://img14.360buyimg.com/n1/jfs/t1/200676/38/5829/42315/615a9999E68e2f8a8/504289898089f2e3.jpg', 1299.00, 1, 1299.00);
INSERT INTO `order_item` VALUES (6, 5, 6, 'Lululemon 瑜伽垫', 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=600', 499.00, 1, 499.00);
INSERT INTO `order_item` VALUES (7, 5, 15, '红双喜 乒乓球拍', 'https://images.unsplash.com/photo-1534158914592-062992fbe900?w=600', 359.00, 1, 359.00);
INSERT INTO `order_item` VALUES (8, 6, 1, 'iPhone 15 Pro Max', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600', 9999.00, 1, 9999.00);
INSERT INTO `order_item` VALUES (9, 6, 2, 'Nike Pegasus 40', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600', 899.00, 1, 899.00);
INSERT INTO `order_item` VALUES (10, 7, 13, 'Sony 无线降噪耳机', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', 2299.00, 1, 2299.00);
INSERT INTO `order_item` VALUES (11, 8, 9, 'Garmin 运动手表', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600', 2499.00, 1, 2499.00);
INSERT INTO `order_item` VALUES (12, 9, 10, '始祖鸟 户外运动包', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600', 1899.00, 1, 1899.00);
INSERT INTO `order_item` VALUES (13, 10, 5, 'Yonex 碳纤维羽毛球拍', 'https://images.unsplash.com/photo-1626225963770-856bb07765f0?w=600', 699.00, 1, 699.00);
INSERT INTO `order_item` VALUES (14, 11, 25, '运动吸汗发带', 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=600', 19.00, 1, 19.00);
INSERT INTO `order_item` VALUES (15, 12, 11, '专业负重跳绳', 'https://images.unsplash.com/photo-1434682772747-f16d3ea162c3?w=600', 69.00, 1, 69.00);
INSERT INTO `order_item` VALUES (16, 12, 20, '小米手环 8', 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=600', 249.00, 1, 249.00);
INSERT INTO `order_item` VALUES (17, 13, 4, 'Adidas 欧冠足球', 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=600', 199.00, 1, 199.00);
INSERT INTO `order_item` VALUES (18, 14, 3, 'Wilson 官方比赛篮球', 'https://images.unsplash.com/photo-1518063319789-7217e6706b04?w=600', 299.00, 1, 299.00);

-- ----------------------------
-- Table structure for order_track
-- ----------------------------
DROP TABLE IF EXISTS `order_track`;
CREATE TABLE `order_track`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `logistics_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流公司',
  `tracking_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运单号',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '当前状态描述',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  CONSTRAINT `order_track_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '物流追踪表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_track
-- ----------------------------
INSERT INTO `order_track` VALUES (1, 1, '顺丰速运', 'SF1234567890', '已签收', '2025-12-17 17:03:44');
INSERT INTO `order_track` VALUES (2, 2, NULL, NULL, '订单已下单，等待发货', '2026-04-02 10:53:34');
INSERT INTO `order_track` VALUES (5, 5, NULL, NULL, '订单已下单，等待发货', '2026-04-07 12:42:05');
INSERT INTO `order_track` VALUES (6, 6, NULL, NULL, '订单已下单，等待发货', '2026-04-08 12:37:44');
INSERT INTO `order_track` VALUES (7, 7, NULL, NULL, '订单已下单，等待发货', '2026-04-29 14:54:07');
INSERT INTO `order_track` VALUES (8, 8, NULL, NULL, '订单已下单，等待发货', '2026-04-29 19:21:23');
INSERT INTO `order_track` VALUES (9, 9, NULL, NULL, '订单已下单，等待发货', '2026-05-07 12:08:43');
INSERT INTO `order_track` VALUES (10, 10, NULL, NULL, '订单已下单，等待发货', '2026-05-07 14:55:37');
INSERT INTO `order_track` VALUES (11, 11, NULL, NULL, '订单已下单，等待发货', '2026-05-07 14:56:11');
INSERT INTO `order_track` VALUES (12, 12, NULL, NULL, '订单已下单，等待发货', '2026-05-07 18:21:34');
INSERT INTO `order_track` VALUES (13, 13, NULL, NULL, '订单已下单，等待发货', '2026-05-12 15:28:39');
INSERT INTO `order_track` VALUES (14, 14, NULL, NULL, '订单已下单，等待发货', '2026-05-13 08:32:32');

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `payment_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付平台流水号',
  `payment_method` enum('wechat','alipay','bank_card') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(10, 2) NOT NULL,
  `status` enum('pending','success','failed','refunded') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `paid_at` datetime NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_id`(`order_id` ASC) USING BTREE,
  UNIQUE INDEX `payment_no`(`payment_no` ASC) USING BTREE,
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES (1, 1, 'AL202312151430001', 'alipay', 9999.00, 'success', '2023-12-15 14:30:05', '2025-12-17 17:03:44');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品描述',
  `price` decimal(10, 2) NOT NULL COMMENT '售价',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `sales_count` int NOT NULL DEFAULT 0 COMMENT '销量',
  `main_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主图URL',
  `status` enum('on_sale','off_sale') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'on_sale' COMMENT '状态：上架/下架',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_product_status`(`status` ASC) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, 'iPhone 15 Pro Max', '钛金属设计，A17 Pro 芯片', 9999.00, 10999.00, 100, 50, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (2, 4, 'Nike Pegasus 40', '最新款全能跑步鞋，极致缓震', 899.00, 1099.00, 150, 20, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (3, 2, 'Wilson 官方比赛篮球', 'NBA同款手感，吸湿耐磨', 299.00, 399.00, 80, 15, 'https://images.unsplash.com/photo-1518063319789-7217e6706b04?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (4, 2, 'Adidas 欧冠足球', '5号标准足球，精准轨迹', 199.00, 249.00, 60, 12, 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (5, 3, 'Yonex 碳纤维羽毛球拍', '全碳素超轻设计，进攻型', 699.00, 899.00, 40, 5, 'https://images.unsplash.com/photo-1626225963770-856bb07765f0?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (6, 5, 'Lululemon 瑜伽垫', 'TPE材质，防滑加厚 5mm', 499.00, 599.00, 100, 30, 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (7, 6, '可调节哑铃组', '单只 24kg，快速重量切换', 1299.00, 1599.00, 25, 3, 'https://images.unsplash.com/photo-1638531838225-397722428249?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (8, 7, 'Speedo 高清泳镜', '防雾透光，专业竞速系列', 129.00, 189.00, 200, 45, 'https://images.unsplash.com/photo-1530549387631-f535c769b90c?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (9, 11, 'Garmin 运动手表', 'GPS精准追踪，长续航心率监测', 2499.00, 2899.00, 30, 8, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (10, 8, '始祖鸟 户外运动包', '防水耐磨，30L 大容量', 1899.00, 2299.00, 15, 2, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (11, 9, '专业负重跳绳', '钢丝内芯，智能计数功能', 69.00, 99.00, 500, 100, 'https://images.unsplash.com/photo-1434682772747-f16d3ea162c3?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (12, 10, 'MC 护膝一对', '加压保护，防止关节受损', 159.00, 199.00, 120, 22, 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (13, 1, 'Sony 无线降噪耳机', 'XM5旗舰音质，静谧享受', 2299.00, 2699.00, 45, 10, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (14, 4, 'Asics 稳定支撑跑鞋', 'GEL技术，缓解脚部疲劳', 799.00, 999.00, 50, 14, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (15, 3, '红双喜 乒乓球拍', '狂飙3胶皮，专业选手首选', 359.00, 459.00, 30, 4, 'https://images.unsplash.com/photo-1534158914592-062992fbe900?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (16, 6, '铸铁壶铃 16kg', '磨砂手感，核心力量训练', 189.00, 249.00, 40, 6, 'https://images.unsplash.com/photo-1583454155184-870a1f63aebc?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (17, 8, '运动保温水壶', '1L 大容量，24小时保温', 89.00, 129.00, 300, 55, 'https://images.unsplash.com/photo-1523362628744-0c14a37ef25a?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (18, 12, '多功能泡沫轴', '深层肌肉放松，瑜伽辅助', 49.00, 79.00, 150, 40, 'https://images.unsplash.com/photo-1599447421416-3414500d18a5?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (19, 5, '健身拉力绳套装', '5种阻力级别，随时随地健身', 79.00, 119.00, 400, 80, 'https://images.unsplash.com/photo-1591948972322-5bb23187e111?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (20, 11, '小米手环 8', '全面屏设计，百种运动模式', 249.00, 299.00, 600, 230, 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (21, 1, 'iPad Air 5', 'M1 芯片，视网膜显示屏', 4399.00, 4799.00, 20, 4, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (22, 4, 'Vans 经典滑板鞋', '街头潮流必备，耐磨橡胶底', 459.00, 539.00, 80, 18, 'https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (23, 12, '筋膜枪 Pro', '强劲扭矩，深层击溃酸痛', 401.00, 599.00, 50, 11, 'https://images.unsplash.com/photo-1620188467120-5042ed1eb5da?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (24, 7, '充气式潜水浮标', '跟屁虫，户外游泳安全必备', 59.00, 79.00, 100, 15, 'https://images.unsplash.com/photo-1544625530-39401704a293?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');
INSERT INTO `product` VALUES (25, 10, '运动吸汗发带', '防止汗液流进眼睛，纯棉舒适', 16.00, 29.00, 1000, 320, 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?w=600', 'on_sale', '2024-04-02 10:00:00', '2024-04-02 10:00:00');

-- ----------------------------
-- Table structure for return_request
-- ----------------------------
DROP TABLE IF EXISTS `return_request`;
CREATE TABLE `return_request`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_item_id` bigint NOT NULL COMMENT '订单项ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退货原因',
  `type` tinyint NOT NULL COMMENT '类型：0退款 1换货 2退货',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0待审核 1通过 2拒绝 3完成',
  `apply_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processed_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '售后申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of return_request
-- ----------------------------
INSERT INTO `return_request` VALUES (1, 3, 2, 2003451, 13, 'Sony WH-1000XM5 无线降噪耳机', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', '音质问题，降噪效果不理想', 0, 0, '2025-12-20 14:32:18', NULL);
INSERT INTO `return_request` VALUES (3, 3, 4, 2007890, 7456, 'Uniqlo 男士轻薄羽绒服', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '颜色与图片严重不符', 2, 2, '2025-12-10 18:07:55', '2025-12-12 10:15:40');
INSERT INTO `return_request` VALUES (4, 3, 5, 2009123, 1, 'Apple iPhone 16 Pro 256GB', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600', '未收到商品', 0, 3, '2025-11-28 22:11:03', '2025-12-05 16:45:12');
INSERT INTO `return_request` VALUES (6, 3, 7, 2011567, 1567, 'L\'Oréal 巴黎欧莱雅护肤套装', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '过敏反应，不适合肤质', 0, 1, '2025-12-08 13:56:19', '2025-12-09 15:30:00');
INSERT INTO `return_request` VALUES (7, 3, 8, 2012789, 4, 'Adidas 经典三叶草卫衣', 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=600', '尺码偏大，想换小一码', 1, 0, '2025-11-20 20:44:31', '2025-12-01 09:10:25');
INSERT INTO `return_request` VALUES (9, 3, 10, 2015123, 4123, 'Beats Studio Pro 头戴式耳机', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '与其他描述不符，功能缺失', 0, 2, '2025-12-05 11:22:15', '2025-12-07 14:55:20');
INSERT INTO `return_request` VALUES (10, 3, 1, 2016345, 5345, 'Dyson Supersonic 吹风机', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '七天无理由退货', 2, 0, '2025-12-18 08:19:06', '2025-12-21 17:07:41');
INSERT INTO `return_request` VALUES (12, 3, 3, 2018789, 7789, 'Kindle Paperwhite 电子书阅读器', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '屏幕有坏点', 2, 3, '2025-11-15 19:55:42', '2025-11-25 12:35:50');
INSERT INTO `return_request` VALUES (14, 3, 5, 2020123, 9123, 'Switch OLED 游戏机', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '想退款购买其他型号', 0, 0, '2025-12-21 11:42:13', NULL);
INSERT INTO `return_request` VALUES (15, 3, 6, 2021345, 2345, 'AirPods Pro 2 降噪耳机', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '连接不稳定，频繁掉线', 2, 2, '2025-12-01 16:28:37', '2025-12-03 13:55:10');
INSERT INTO `return_request` VALUES (16, 3, 5, 6, 6, 'Lululemon 瑜伽垫', 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=600', '1', 0, 1, '2026-04-07 16:28:02', NULL);
INSERT INTO `return_request` VALUES (17, 2, 7, 10, 13, 'Sony 无线降噪耳机', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', '1111', 2, 1, '2026-05-06 16:46:39', NULL);
INSERT INTO `return_request` VALUES (18, 2, 2, 3, 9, '运动手表', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600', '323333', 0, 1, '2026-05-06 16:50:39', NULL);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_item_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `rating` tinyint NOT NULL COMMENT '评分1-5',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '评价内容',
  `images` json NULL COMMENT '评价图片URL数组',
  `is_anonymous` tinyint NOT NULL DEFAULT 0 COMMENT '是否匿名：1是，0否',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_item`(`order_item_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_chk_1` CHECK (`rating` between 1 and 5)
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1, 1, 1, 1, 5, '手机很好用，运行流畅！', '[\"https://example.com/review1.jpg\"]', 0, '2025-12-17 17:03:44');
INSERT INTO `review` VALUES (2, 6, 3, 6, 5, '好', NULL, 0, '2026-04-07 13:06:37');
INSERT INTO `review` VALUES (3, 7, 3, 15, 5, '很好的球拍让我乒乓球旋转', NULL, 0, '2026-04-07 13:07:01');
INSERT INTO `review` VALUES (4, 12, 3, 10, 5, '还可以', NULL, 0, '2026-05-07 14:55:17');
INSERT INTO `review` VALUES (5, 14, 3, 25, 5, '可以的', NULL, 0, '2026-05-07 14:57:46');
INSERT INTO `review` VALUES (6, 18, 3, 3, 5, '1111', NULL, 0, '2026-05-13 08:34:30');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（明文）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1正常，0禁用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_vip` tinyint NULL DEFAULT 0 COMMENT '0:普通, 1:VIP',
  `vip_expire_time` datetime NULL DEFAULT NULL COMMENT 'VIP过期时间',
  `experience` bigint NULL DEFAULT 0,
  `current_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '灵胎',
  `login_days` int NULL DEFAULT 0 COMMENT '连续登录天数',
  `last_login_date` date NULL DEFAULT NULL COMMENT '最后登录日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_user_status`(`status` ASC) USING BTREE,
  INDEX `idx_user_created`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhangsan', '123456', 'zhangsan@example.com', '13800010001', 'https://example.com/avatar1.jpg', 1, '2025-12-17 17:03:44', '2026-04-29 20:36:42', 0, NULL, 0, '灵胎', 0, NULL);
INSERT INTO `user` VALUES (2, 'lisi', '123456', NULL, '13800010002', NULL, 1, '2025-12-22 21:39:10', '2025-12-22 21:39:10', 1, '2026-05-05 07:54:09', 2310700, '凌霄', 0, NULL);
INSERT INTO `user` VALUES (3, 'kokomi', '123456', NULL, '13800010003', NULL, 1, '2026-04-07 11:57:14', '2026-04-29 20:36:54', 1, NULL, 15148500, '天庭', 0, NULL);
INSERT INTO `user` VALUES (4, 'wangwu', '123456', NULL, NULL, NULL, 1, '2026-04-29 16:49:35', '2026-04-29 20:39:14', 1, NULL, 2499000, '凌霄', 0, NULL);
INSERT INTO `user` VALUES (5, 'kokomi2', '123456', 'demo5@example.com', NULL, NULL, 1, '2026-05-12 15:28:11', '2026-05-12 15:28:11', 0, NULL, 199450, '神桥', 0, NULL);

-- ----------------------------
-- Table structure for vip_config
-- ----------------------------
DROP TABLE IF EXISTS `vip_config`;
CREATE TABLE `vip_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `min_exp` bigint NOT NULL,
  `is_svip` tinyint NULL DEFAULT 0 COMMENT '0:神藏(会员), 1:天宫(高级会员)',
  `daily_gain` int NULL DEFAULT 0 COMMENT '每日被动修为增益',
  `discount_rate` decimal(3, 2) NULL DEFAULT 1.00,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_config
-- ----------------------------
INSERT INTO `vip_config` VALUES (1, '灵胎', 0, 0, 0, 1.00);
INSERT INTO `vip_config` VALUES (2, '五曜', 100, 0, 15, 0.98);
INSERT INTO `vip_config` VALUES (3, '六合', 1000, 0, 20, 0.96);
INSERT INTO `vip_config` VALUES (4, '七星', 5000, 0, 30, 0.94);
INSERT INTO `vip_config` VALUES (5, '天人', 20000, 0, 45, 0.92);
INSERT INTO `vip_config` VALUES (6, '生死', 50000, 0, 60, 0.90);
INSERT INTO `vip_config` VALUES (7, '神桥', 100000, 0, 80, 0.88);
INSERT INTO `vip_config` VALUES (8, '尊神', 200000, 1, 120, 0.85);
INSERT INTO `vip_config` VALUES (9, '真神', 400000, 1, 180, 0.80);
INSERT INTO `vip_config` VALUES (10, '瑶池', 700000, 1, 260, 0.75);
INSERT INTO `vip_config` VALUES (11, '斩神台', 1100000, 1, 350, 0.70);
INSERT INTO `vip_config` VALUES (12, '玉京', 1600000, 1, 450, 0.65);
INSERT INTO `vip_config` VALUES (13, '凌霄', 2200000, 1, 600, 0.60);
INSERT INTO `vip_config` VALUES (14, '帝座', 3000000, 1, 800, 0.55);
INSERT INTO `vip_config` VALUES (15, '天庭', 5000000, 1, 1200, 0.50);

-- ----------------------------
-- Table structure for vip_order
-- ----------------------------
DROP TABLE IF EXISTS `vip_order`;
CREATE TABLE `vip_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `amount` decimal(10, 2) NOT NULL,
  `months` int NOT NULL COMMENT '购买月数',
  `status` tinyint NULL DEFAULT 1 COMMENT '订单状态 1:已完成 0:已取消',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `pay_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_order
-- ----------------------------
INSERT INTO `vip_order` VALUES (1, 2, 'VIP1775343130168', 19.00, 1, 1, '2026-04-05 06:52:10', NULL);

SET FOREIGN_KEY_CHECKS = 1;
