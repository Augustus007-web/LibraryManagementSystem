/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : library_db

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 19/03/2026 15:17:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for books
-- ----------------------------
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图书ID，主键',
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ISBN 书号',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '书名',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版社',
  `publication_date` date NULL DEFAULT NULL COMMENT '出版日期',
  `category_id` bigint NULL DEFAULT NULL COMMENT '类别ID，外键关联 categories 表',
  `quantity` int NOT NULL DEFAULT 0 COMMENT '馆藏数量',
  `available_quantity` int NOT NULL DEFAULT 0 COMMENT '可借阅数量',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `cover_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `borrow_count` int NOT NULL DEFAULT 0,
  `shelf_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `isbn`(`isbn`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图书信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of books
-- ----------------------------
INSERT INTO `books` VALUES (1, '978-7-111-59800-6', 'Java核心技术 卷I', 'Cay S. Horstmann', '机械工业出版社', '2024-05-03', 1, 10, 7, '经典的Java入门和进阶书籍', 'https://m.360buyimg.com/mobilecms/s750x750_jfs/t1/102900/26/2632/158701/5dd601a5E9ed34588/596e136d4a144cae.jpg!q80.dpg', '2025-05-13 19:12:56', '2025-06-05 19:56:54', 0, NULL);
INSERT INTO `books` VALUES (2, '978-7-5063-6543-7', '三体', '刘慈欣', '重庆出版社', NULL, 4, 12, 10, '中国科幻文学的里程碑之作', 'https://p1.ssl.qhimg.com/t0147ec1b07078c0cf8.jpg', '2025-05-13 19:12:56', '2025-06-05 19:57:49', 0, NULL);
INSERT INTO `books` VALUES (3, '978-7-02-013000-0', '活着', '余华', '人民文学出版社', NULL, 2, 8, 7, '一部感人至深的小说', 'https://ts1.tc.mm.bing.net/th/id/R-C.a32ee203d37f067a562aeb892ccdd700?rik=9rT1%2fHWWQStetQ&riu=http%3a%2f%2fepaper.file.routeryun.com%2frdrb%2f2020-09-04%2f5f51014bbe98d.jpg&ehk=ALvYh4n5Oqe0XauWt8a%2fXkRwcU8tk19683R9sKuhNmE%3d&risl=&pid=ImgRaw&r=0', '2025-05-13 19:12:56', '2025-06-02 18:48:51', 0, NULL);
INSERT INTO `books` VALUES (7, '978-7-02-012335-3', '士兵突击', '兰晓龙', '人民文学出版社', NULL, 2, 3, 0, '当代中国最真实的士兵形象，他叫许三多，一名二级士官....', 'https://img.alicdn.com/i4/2200607228172/O1CN01Jgf1KW2AEpMm7N4kk_!!2200607228172.jpg', '2025-05-25 20:04:48', '2025-06-05 19:56:43', 0, NULL);
INSERT INTO `books` VALUES (9, '978-7-101-05449-1', '万历十五年', '黄仁宇', '中华书局出版社', NULL, 3, 15, 13, '万历十五年，亦即公元1587年，在中国，在这平平淡淡的一年中，发生了诺干为历史学家所易忽视的事件。', 'https://img.alicdn.com/i4/2783805853/O1CN01OGWWv21t6iiXJOdkH_!!2783805853.jpg', '2025-05-25 22:21:31', '2026-03-09 13:27:04', 0, NULL);
INSERT INTO `books` VALUES (10, '978-7-5063-6543-8', '李自成', '姚雪垠', '人民文学出版社', NULL, 3, 10, 9, '明末时期，农民军起义领袖李自成的一生', 'https://img.alicdn.com/bao/uploaded/i3/2200758688827/O1CN01Oqo3kk2F4ooaQYGWa_!!0-item_pic.jpg', '2025-05-28 17:50:08', '2025-05-30 01:28:51', 0, NULL);
INSERT INTO `books` VALUES (11, '978-7-5404-8847-5', '显微镜下的大明', '马伯庸', '湖南大学出版社', NULL, 3, 2, 1, '本书讲述的是六个深藏于故纸堆中的明代基层政治事件。', 'https://img.alicdn.com/i2/1755000608/O1CN01fzNdNz1GMVQQL4VEA_!!1755000608.jpg', '2025-05-28 21:44:44', '2025-06-05 19:06:33', 0, NULL);
INSERT INTO `books` VALUES (12, '978-7-5726-0858-2', '长安的荔枝', '马伯庸', '湖南大学出版社', NULL, 2, 5, 4, '天宝十四年，长安城小吏李善德接到一个任务，从岭南运来新鲜的荔枝。这是个不可能完成的任务.....\"一骑红尘妃子笑，有人知是荔枝来\"', 'https://img14.360buyimg.com/pop/jfs/t1/106292/39/20840/97633/63d503b0F9414836f/84db4eabe1eddcdc.jpg', '2025-05-28 23:08:32', '2025-06-04 23:37:38', 0, NULL);
INSERT INTO `books` VALUES (14, '978-7-115-62903-6', '实用软件工程第三版', '吕云翔', '重庆邮电出版社', NULL, 13, 1, 0, '景德镇艺术职业大学指定官方教材', 'https://file.51zhy.cn/fs4/files/upload/df0/df032460b6fe90457b7b7670ecdc4f5d.jpg_cover.jpg', '2025-05-28 23:47:20', '2026-03-09 13:27:50', 0, NULL);
INSERT INTO `books` VALUES (15, '978-7-8022-5481-7', '我的团长我的团', '兰晓龙', '新星出版社', NULL, 2, 10, 9, '一群炮灰组成的团，一位“坑蒙拐骗”的团长，他们面对的是一场几乎必死无疑的战争。 这是迄今为止，唯一一部对得起“中国远征军”这五个字的中国远征军题材的小说。', 'https://img.alicdn.com/i2/2200607228172/O1CN01QmFGk82AEpN6cTOk9_!!2200607228172.jpg', '2025-05-30 01:42:16', '2025-06-02 18:48:32', 0, NULL);
INSERT INTO `books` VALUES (16, '978-7-5442-8002-0', '你想活出怎样的人生', '【日】吉野源三郎', '南海出版公司', NULL, 2, 1, 0, '在1930年代，吉野源三郎写出了《你想活出怎样的人生》。那时的日本，军国主义势力正在抬头，吉野源三郎或许已经感知到社会的异常与疯狂，预感到战争即将爆发。他无法阻挡战争趋势，但选择写下这本书，试图把“人”之所以为“人”的价值传递给年轻一代。', 'https://img.alicdn.com/bao/uploaded/i1/2041592426/O1CN01YcMqzp1Tn9iYNBWoU_!!0-item_pic.jpg', '2025-05-30 01:45:23', '2025-05-31 00:34:03', 0, NULL);
INSERT INTO `books` VALUES (17, '978-7-5594-7820-7', '沙丘', '【美】弗兰克·赫伯特', '读库文化', '1965-02-04', 4, 10, 9, '”一切为了”香料“！“', 'https://img.alicdn.com/i1/376252686/O1CN0188vcAA1ViEcCqVX7O_!!376252686.jpg', '2025-05-30 23:59:28', '2025-06-02 18:48:46', 0, NULL);
INSERT INTO `books` VALUES (18, ' 978-7-0200-7368-9', '生死线', '兰晓龙', '人民文学出版社', NULL, 2, 4, 3, '本书以四个职业、身份、教养和性格完全不同的青年男子的合作和友谊为主线，描写了他们经历灵魂炼狱般的残酷青春成长故事，讲述了一段情义无价的平民抗战的传奇。', 'https://tse1.mm.bing.net/th/id/OIP.xhJAvDgjMsLuryepbL4wMAAAAA?rs=1&pid=ImgDetMain&o=7&rm=3', '2025-05-31 00:06:26', '2026-01-12 21:10:44', 0, NULL);
INSERT INTO `books` VALUES (19, ' 978-7-02-012108-3', '好家伙', '兰晓龙', '人民文学出版社', '2009-07-10', 2, 3, 2, '好家伙是种子，好家伙是先驱，好家伙是燃烧自己照亮未来的人。好家伙为之牺牲的未来，就是我们的今天。', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.-CYNzAcnY5A1NVsltWWE4wAAAA?rs=1&pid=ImgDetMain', '2025-05-31 00:29:38', '2025-06-02 18:48:27', 0, NULL);
INSERT INTO `books` VALUES (20, '978-7-5354-4060-0 ', '金瓯缺', '徐兴业', '河南文艺出版社', '2009-02-05', 3, 4, 3, '豆瓣8.8分！第三届茅盾文学奖获奖作品！中国现当代历史小说代表之作。四十七年心血创作，徐兴业先生1938年开始酝酿，饱经历史沧桑，至1985年出齐四卷。北宋徽宗年间，汴京歌舞依旧，似乎天下太平，高枕无忧。朝廷与金国达成共同夹击辽国的“海上之盟”，逼辽纳土称臣，收复燕京指日可待。朝中老将种师道迫于皇命仓促出征，朝气蓬发的青年武将马扩与新婚不久的妻子亸娘告别，开赴北疆收复国土。然而东京梦华转眼逝去，靖康日暮血泪涟涟。大厦将颓，狂风暴雨下的众生在挣扎中痛苦生存……', 'https://img.alicdn.com/imgextra/i3/3354676697/O1CN01LzNxZh1zLHU3jYM02_!!3354676697.jpg', '2025-06-02 21:42:36', '2025-12-28 18:47:34', 0, NULL);
INSERT INTO `books` VALUES (21, '978-7-02-014242-2', '冬与狮', '兰晓龙', '人民文学出版社', NULL, 2, 10, 8, '《冬与狮》故事的主要背景是朝鲜战争第二次战役东线战场的长津湖一战。1950年秋冬之交，第七穿插连所在的第九兵团原本驻扎在华东地区，被紧急征召开赴朝鲜长津湖战场...', 'https://img.alicdn.com/i4/2129856610/O1CN01dlqncv1yhQhi2atfT_!!2129856610.jpg', '2025-06-04 22:45:33', '2026-03-09 13:23:45', 0, NULL);
INSERT INTO `books` VALUES (22, '978-7-5726-1091-2', '太白金星有点烦', '马伯庸', '湖南文艺出版社', '2023-06-15', 2, 10, 10, '《太白金星有点烦》讲述天庭和西天联合推出了“西天取经”的重大项目，太白金星李长庚受命策划九九八十一难，确保唐僧能安全走完流程，平稳取经成佛。老神仙本以为一切尽在掌控中，谁知天大的麻烦才刚刚开始...', 'https://img.alicdn.com/i4/725677994/O1CN01UmIUNG28vIzklLBj4_!!725677994.jpg', '2025-06-04 23:13:20', '2025-06-04 23:13:20', 0, NULL);
INSERT INTO `books` VALUES (23, '978-7-5726-1609-9', '食南之徒', '马伯庸', '湖南文艺出版社', '2024-04-11', 2, 10, 10, '《食南之徒》讲述贪吃的西汉使者唐蒙出使美食圣地南越国，意外卷入南越宫廷的政治斗争的故事。该小说延续了马伯庸“在历史缝隙中寻找可能性”的写作风格，即透过历史长河中的微小之物窥测背后官场之道与叵测人心，微小之物在书中化身为“什么都能吃的岭南”里的一味小小酱料。', 'https://tse3-mm.cn.bing.net/th/id/OIP-C.ZlAKpnZh46enwqIHVzjwRAHaHk?r=0&rs=1&pid=ImgDetMain', '2025-06-04 23:17:56', '2025-06-04 23:17:56', 0, NULL);
INSERT INTO `books` VALUES (24, '978-7-2290-0476-7', '超新星纪元', '刘慈欣', '重庆出版社', '2003-11-05', 4, 10, 10, '在这个世界中，孩子们对以往成人心目中的纯洁的儿童形象进行大胆的反叛与颠覆。这些世界的新主人们，虽然与成人一般进行战争，但是他们把战争当作游戏，在纯洁的南极大陆上，进行着最血腥的战斗与厮杀。坦克、航母、歼击机、核弹头……所有武器，都是他们的新玩具。', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.ykwUbhP2AILCAg8rpX4N3AHaKv?r=0&rs=1&pid=ImgDetMain', '2025-06-04 23:21:48', '2025-06-04 23:21:48', 0, NULL);
INSERT INTO `books` VALUES (25, '978-7-5364-8427-6', '球状闪电', '刘慈欣', '四川科学技术出版社', '2004-06-10', 4, 10, 10, '陈博士有着一段惨不忍睹的童年经历，他亲眼看到一个神秘的闪电球从窗外飘了进来，将他的父母烧成了灰烬。长大后，陈博士爱上了物理学，因为他想知道到底是什么力量造成了父母的惨剧。', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.YWznX3VWtqZqDMyTwjvb-AHaHa?r=0&rs=1&pid=ImgDetMain', '2025-06-04 23:24:52', '2025-06-04 23:24:52', 0, NULL);
INSERT INTO `books` VALUES (26, '978-7-0200-4249-4', '小王子', '【法】安东尼·德·圣·埃克苏佩里', '人民文学出版社', '2016-07-05', 2, 20, 19, '飞行员和小王子在沙漠中共同拥有过一段极为珍贵的友谊。当小王子离开地球时，飞行员非常悲伤。他一直非常怀念他们共度的时光。他为纪念小王子写了这部小说。', 'https://m.360buyimg.com/mobilecms/s750x750_jfs/t18076/90/1409606400/300194/6de2f14f/5ac9d4ecNb87fc47d.jpg!q80.dpg', '2025-06-04 23:31:37', '2025-06-30 23:55:35', 0, NULL);
INSERT INTO `books` VALUES (27, '978-7-5404-7831-5', '长安十二时辰', '马伯庸', '人民文学出版社', '2017-11-10', 2, 8, 8, '唐天宝三年，元月十四日，长安。大唐皇都的居民不知道，上元节辉煌灯火亮起之时，等待他们的，将是场吞噬一切的劫难。突厥、狼卫、绑架、暗杀、烈焰、焚城，毁灭长安城的齿轮已经开始转动。而拯救长安的全部希望，只有一个即将被斩首的独眼死囚和短短的十二个时辰……', 'https://img.alicdn.com/bao/uploaded/i3/2215388945642/O1CN01Y8ILhs1rY5T2Uf5hY_!!0-item_pic.jpg', '2025-06-04 23:35:43', '2026-01-12 21:03:51', 0, NULL);
INSERT INTO `books` VALUES (28, '978-7-5404-7831-6', '白夜行', '东野圭吾', '人民文学出版社', NULL, 2, 2, 2, '这一切的起因竟是少女的母亲由于家庭窘迫，为了钱硬逼着自己的女儿出卖肉体，幼年不幸的经历，让雪穗的心灵从此失去了阳光；而亮司基于各种复杂的情愫，一直暗中帮助雪穗报复迫害她的人，同时也帮她一步步铲除一切妨碍她成功的障碍', 'https://so1.360tres.com/t0113682573f70fd843.jpg', '2025-06-05 18:58:24', '2025-10-03 16:55:06', 0, NULL);
INSERT INTO `books` VALUES (29, '978-7-5404-7831-9', '许三观买血记', '余华', '人民文学出版社', '2025-07-30', 2, 1, 1, '许三观靠着卖血渡过了人生的一个个难关，战胜了命运强加给他的惊涛骇浪，而当他老了，知道自己的血再也没有人要时，精神却崩溃了。小说中平凡的小人物挣扎于浮沉的时代，用他们并不厚实的臂膀不断拍打出激荡的浪花，观望着模糊的前路，而其间的苦难与悲情却又清晰可见，平凡之家的悲剧喜剧在余华细腻的笔触下显得可笑而又可怜。', 'https://img.alicdn.com/i4/3968989056/TB2n4LGoZIrBKNjSZK9XXagoVXa_!!3968989056.jpg', '2025-06-05 19:59:01', '2025-08-11 09:37:50', 0, NULL);
INSERT INTO `books` VALUES (30, '9787115428028', 'Python编程：从入门到实践', '埃里克·马瑟斯（Eric Matthes）', '人民邮电出版社', '2016-01-03', 1, 1, 1, '本书是一本针对所有层次的Python 读者而作的Python 入门书。', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.jwoXNFV0qkCUa07qu1A08gHaHa?w=210&h=210&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3', '2026-02-03 02:32:41', '2026-03-09 13:22:05', 0, NULL);

-- ----------------------------
-- Table structure for borrow_records
-- ----------------------------
DROP TABLE IF EXISTS `borrow_records`;
CREATE TABLE `borrow_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID，主键',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联 users 表',
  `book_id` bigint NOT NULL COMMENT '图书ID，外键关联 books 表',
  `borrow_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅日期',
  `due_date` timestamp NULL DEFAULT NULL COMMENT '应还日期',
  `return_date` timestamp NULL DEFAULT NULL COMMENT '实际归还日期 (NULL 表示未归还)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'BORROWED' COMMENT '借阅状态 (BORROWED, RETURNED, OVERDUE)',
  `notes` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `book_id`(`book_id`) USING BTREE,
  CONSTRAINT `borrow_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `borrow_records_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '借阅记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_records
-- ----------------------------
INSERT INTO `borrow_records` VALUES (1, 5, 1, '2025-05-28 23:01:15', '2025-06-27 23:01:15', NULL, 'BORROWED', NULL, '2025-05-28 23:01:15', '2025-05-28 23:01:15');
INSERT INTO `borrow_records` VALUES (2, 1, 2, '2025-05-28 23:02:51', '2025-06-27 23:02:51', '2025-05-28 23:20:41', 'RETURNED', NULL, '2025-05-28 23:02:51', '2025-05-28 23:20:41');
INSERT INTO `borrow_records` VALUES (3, 1, 7, '2025-05-28 23:21:31', '2025-06-27 23:21:31', '2025-05-30 23:30:37', 'RETURNED', NULL, '2025-05-28 23:21:31', '2025-05-30 23:30:37');
INSERT INTO `borrow_records` VALUES (4, 5, 7, '2025-05-28 23:24:58', '2025-06-27 23:24:58', NULL, 'BORROWED', NULL, '2025-05-28 23:24:58', '2025-05-28 23:24:58');
INSERT INTO `borrow_records` VALUES (5, 5, 2, '2025-05-28 23:25:08', '2025-06-27 23:25:08', NULL, 'BORROWED', NULL, '2025-05-28 23:25:08', '2025-05-28 23:25:08');
INSERT INTO `borrow_records` VALUES (6, 5, 11, '2025-05-28 23:25:16', '2025-06-27 23:25:16', NULL, 'BORROWED', NULL, '2025-05-28 23:25:16', '2025-05-28 23:25:16');
INSERT INTO `borrow_records` VALUES (7, 8, 7, '2025-05-28 23:27:07', '2025-06-27 23:27:07', NULL, 'BORROWED', NULL, '2025-05-28 23:27:07', '2025-05-28 23:27:07');
INSERT INTO `borrow_records` VALUES (8, 5, 10, '2025-05-29 00:08:02', '2025-06-28 00:08:02', NULL, 'BORROWED', NULL, '2025-05-29 00:08:02', '2025-05-29 00:08:02');
INSERT INTO `borrow_records` VALUES (9, 1, 16, '2025-05-30 23:30:10', '2025-06-29 23:30:10', '2025-05-30 23:30:35', 'RETURNED', NULL, '2025-05-30 23:30:10', '2025-05-30 23:30:35');
INSERT INTO `borrow_records` VALUES (10, 1, 18, '2025-05-31 00:08:20', '2025-06-30 00:08:20', '2025-06-02 21:05:49', 'RETURNED', NULL, '2025-05-31 00:08:20', '2025-06-02 21:05:49');
INSERT INTO `borrow_records` VALUES (11, 1, 7, '2025-05-31 00:24:30', '2025-06-30 00:24:30', '2025-06-02 21:05:46', 'RETURNED', NULL, '2025-05-31 00:24:30', '2025-06-02 21:05:46');
INSERT INTO `borrow_records` VALUES (12, 5, 16, '2025-05-31 00:34:03', '2025-06-30 00:34:03', NULL, 'BORROWED', NULL, '2025-05-31 00:34:03', '2025-05-31 00:34:03');
INSERT INTO `borrow_records` VALUES (13, 5, 9, '2025-06-02 18:48:19', '2025-07-02 18:48:19', '2026-03-09 13:26:47', 'RETURNED', NULL, '2025-06-02 18:48:19', '2026-03-09 13:26:47');
INSERT INTO `borrow_records` VALUES (14, 5, 19, '2025-06-02 18:48:27', '2025-07-02 18:48:27', NULL, 'BORROWED', NULL, '2025-06-02 18:48:27', '2025-06-02 18:48:27');
INSERT INTO `borrow_records` VALUES (15, 5, 14, '2025-06-02 18:48:30', '2025-07-02 18:48:30', '2025-06-03 00:02:40', 'RETURNED', NULL, '2025-06-02 18:48:30', '2025-06-03 00:02:40');
INSERT INTO `borrow_records` VALUES (16, 5, 15, '2025-06-02 18:48:32', '2025-07-02 18:48:32', NULL, 'BORROWED', NULL, '2025-06-02 18:48:32', '2025-06-02 18:48:32');
INSERT INTO `borrow_records` VALUES (17, 5, 17, '2025-06-02 18:48:46', '2025-07-02 18:48:46', NULL, 'BORROWED', NULL, '2025-06-02 18:48:46', '2025-06-02 18:48:46');
INSERT INTO `borrow_records` VALUES (18, 5, 3, '2025-06-02 18:48:51', '2025-07-02 18:48:51', NULL, 'BORROWED', NULL, '2025-06-02 18:48:51', '2025-06-02 18:48:51');
INSERT INTO `borrow_records` VALUES (19, 5, 18, '2025-06-02 18:48:56', '2025-07-02 18:48:56', '2025-06-02 21:09:05', 'RETURNED', NULL, '2025-06-02 18:48:56', '2025-06-02 21:09:05');
INSERT INTO `borrow_records` VALUES (20, 5, 12, '2025-06-02 18:49:01', '2025-07-02 18:49:01', '2025-06-02 18:49:30', 'RETURNED', NULL, '2025-06-02 18:49:01', '2025-06-02 18:49:30');
INSERT INTO `borrow_records` VALUES (21, 5, 12, '2025-06-02 18:50:01', '2025-07-02 18:50:01', NULL, 'BORROWED', NULL, '2025-06-02 18:50:01', '2025-06-02 18:50:01');
INSERT INTO `borrow_records` VALUES (22, 5, 18, '2025-06-02 21:09:17', '2025-07-02 21:09:17', NULL, 'BORROWED', NULL, '2025-06-02 21:09:17', '2025-06-02 21:09:17');
INSERT INTO `borrow_records` VALUES (23, 1, 20, '2025-06-02 21:43:07', '2025-07-02 21:43:07', '2025-06-03 00:01:13', 'RETURNED', NULL, '2025-06-02 21:43:07', '2025-06-03 00:01:13');
INSERT INTO `borrow_records` VALUES (24, 5, 20, '2025-06-02 23:57:07', '2025-07-02 23:57:07', '2025-10-26 12:37:55', 'RETURNED', NULL, '2025-06-02 23:57:07', '2025-10-26 12:37:55');
INSERT INTO `borrow_records` VALUES (25, 1, 21, '2025-06-04 22:47:15', '2025-07-04 22:47:15', NULL, 'BORROWED', NULL, '2025-06-04 22:47:15', '2025-06-04 22:47:15');
INSERT INTO `borrow_records` VALUES (26, 1, 1, '2025-06-04 23:36:54', '2025-07-04 23:36:54', NULL, 'BORROWED', NULL, '2025-06-04 23:36:54', '2025-06-04 23:36:54');
INSERT INTO `borrow_records` VALUES (27, 5, 28, '2025-06-05 18:58:58', '2025-07-05 18:58:58', '2025-06-05 18:59:10', 'RETURNED', NULL, '2025-06-05 18:58:58', '2025-06-05 18:59:10');
INSERT INTO `borrow_records` VALUES (28, 14, 11, '2025-06-05 19:06:09', '2025-07-05 19:06:09', '2025-06-05 19:06:33', 'RETURNED', NULL, '2025-06-05 19:06:09', '2025-06-05 19:06:33');
INSERT INTO `borrow_records` VALUES (29, 5, 28, '2025-06-05 19:40:38', '2025-07-05 19:40:38', '2025-10-03 16:55:06', 'RETURNED', NULL, '2025-06-05 19:40:38', '2025-10-03 16:55:06');
INSERT INTO `borrow_records` VALUES (30, 16, 7, '2025-06-05 19:56:43', '2025-07-05 19:56:43', NULL, 'BORROWED', NULL, '2025-06-05 19:56:43', '2025-06-05 19:56:43');
INSERT INTO `borrow_records` VALUES (31, 16, 1, '2025-06-05 19:56:54', '2025-07-05 19:56:54', NULL, 'BORROWED', NULL, '2025-06-05 19:56:54', '2025-06-05 19:56:54');
INSERT INTO `borrow_records` VALUES (32, 16, 9, '2025-06-05 19:56:56', '2025-07-05 19:56:56', NULL, 'BORROWED', NULL, '2025-06-05 19:56:56', '2025-06-05 19:56:56');
INSERT INTO `borrow_records` VALUES (33, 16, 2, '2025-06-05 19:56:58', '2025-07-05 19:56:58', '2025-06-05 19:57:22', 'RETURNED', NULL, '2025-06-05 19:56:58', '2025-06-05 19:57:22');
INSERT INTO `borrow_records` VALUES (34, 16, 2, '2025-06-05 19:57:35', '2025-07-05 19:57:35', '2025-06-05 19:57:40', 'RETURNED', NULL, '2025-06-05 19:57:35', '2025-06-05 19:57:40');
INSERT INTO `borrow_records` VALUES (35, 16, 2, '2025-06-05 19:57:49', '2025-07-05 19:57:49', NULL, 'BORROWED', NULL, '2025-06-05 19:57:49', '2025-06-05 19:57:49');
INSERT INTO `borrow_records` VALUES (36, 5, 29, '2025-06-05 19:59:12', '2025-07-05 19:59:12', '2025-06-05 19:59:22', 'RETURNED', NULL, '2025-06-05 19:59:12', '2025-06-05 19:59:22');
INSERT INTO `borrow_records` VALUES (37, 5, 26, '2025-06-30 23:55:35', '2025-07-30 23:55:35', NULL, 'BORROWED', NULL, '2025-06-30 23:55:35', '2025-06-30 23:55:35');
INSERT INTO `borrow_records` VALUES (38, 5, 21, '2025-09-27 18:06:30', '2025-10-27 18:06:30', '2025-09-27 18:07:05', 'RETURNED', NULL, '2025-09-27 18:06:30', '2025-09-27 18:07:05');
INSERT INTO `borrow_records` VALUES (39, 5, 20, '2025-10-26 12:38:21', '2025-11-25 12:38:21', NULL, 'BORROWED', NULL, '2025-10-26 12:38:21', '2025-10-26 12:38:21');
INSERT INTO `borrow_records` VALUES (40, 16, 20, '2025-12-28 18:34:20', '2026-01-27 18:34:20', '2025-12-28 18:38:37', 'RETURNED', NULL, '2025-12-28 18:34:20', '2025-12-28 18:38:37');
INSERT INTO `borrow_records` VALUES (41, 16, 20, '2025-12-28 18:46:35', '2026-01-27 18:46:35', '2025-12-28 18:47:34', 'RETURNED', NULL, '2025-12-28 18:46:35', '2025-12-28 18:47:34');
INSERT INTO `borrow_records` VALUES (42, 5, 27, '2026-01-12 21:03:39', '2026-02-11 21:03:39', '2026-01-12 21:03:51', 'RETURNED', NULL, '2026-01-12 21:03:39', '2026-01-12 21:03:51');
INSERT INTO `borrow_records` VALUES (43, 5, 30, '2026-03-09 13:21:30', '2026-04-08 13:21:30', '2026-03-09 13:22:05', 'RETURNED', NULL, '2026-03-09 13:21:30', '2026-03-09 13:22:05');
INSERT INTO `borrow_records` VALUES (44, 5, 21, '2026-03-09 13:23:45', '2026-04-08 13:23:45', NULL, 'BORROWED', NULL, '2026-03-09 13:23:45', '2026-03-09 13:23:45');
INSERT INTO `borrow_records` VALUES (45, 5, 9, '2026-03-09 13:27:04', '2026-04-08 13:27:04', NULL, 'BORROWED', NULL, '2026-03-09 13:27:04', '2026-03-09 13:27:04');
INSERT INTO `borrow_records` VALUES (46, 5, 14, '2026-03-09 13:27:50', '2026-04-08 13:27:50', NULL, 'BORROWED', NULL, '2026-03-09 13:27:50', '2026-03-09 13:27:50');

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类别ID，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类别名称 (如: 科幻, 历史, 计算机)',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图书类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, '计算机科学', '关于计算机编程、算法、数据结构等的书籍', '2025-05-13 19:12:56', '2025-05-13 19:12:56');
INSERT INTO `categories` VALUES (2, '文学', '小说、诗歌、散文等文学作品', '2025-05-13 19:12:56', '2025-05-13 19:12:56');
INSERT INTO `categories` VALUES (3, '历史', '关于世界各国历史事件和人物的书籍', '2025-05-13 19:12:56', '2025-05-13 19:12:56');
INSERT INTO `categories` VALUES (4, '科幻', '科学幻想类小说和读物', '2025-05-13 19:12:56', '2025-05-13 19:12:56');
INSERT INTO `categories` VALUES (5, '经管', '经济和管理类相关书籍', '2025-05-13 19:12:56', '2025-05-25 21:02:11');
INSERT INTO `categories` VALUES (11, '政治', '思想政治类书籍', '2025-05-25 21:03:31', '2025-05-25 21:03:31');
INSERT INTO `categories` VALUES (13, '教科书', '本专业学习过程中涉及到的教课书籍', '2025-05-25 22:54:01', '2025-05-25 22:54:01');
INSERT INTO `categories` VALUES (14, '童话', '无', '2025-06-05 20:02:32', '2025-06-05 20:02:32');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名 (例如学号)',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码 (存储加密后的密码)',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话号码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER' COMMENT '角色 (USER, ADMIN)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '$2a$10$Y.iA9PzUSDFbsMRQG5VzUuXEV2fXqjV5DRMPALU7apuPMmvygDS.W', '管理员', 'admin@example.com', '13800138000', 'ADMIN', '2025-05-13 19:12:56', '2025-05-13 19:12:56');
INSERT INTO `users` VALUES (5, '2022105010237', '@ZW3520711821', '赵伟', '3520711821@qq.com', '18460328173', 'ADMIN', '2025-05-13 14:30:16', '2025-06-04 22:41:08');
INSERT INTO `users` VALUES (7, '2022105010234', 'admin123', '彭奕豪', '2071879147@qq.com', '123456789', 'USER', '2025-05-14 14:34:50', '2025-05-14 14:34:50');
INSERT INTO `users` VALUES (8, '2022105010236', 'admin123', '何兴永', '3256034140@qq.com', '123456789', 'USER', '2025-05-14 14:35:55', '2025-05-14 14:35:55');
INSERT INTO `users` VALUES (9, '2022105010238', 'admin123', '赖华平', '3301616344@qq.com', '123456789', 'USER', '2025-05-14 14:37:59', '2025-05-14 14:37:59');
INSERT INTO `users` VALUES (14, '2022105010229', 'admin123', '孔祥卓', '3151125171@qq.com', '18397881586', 'USER', '2025-06-05 19:05:06', '2025-06-05 19:10:16');
INSERT INTO `users` VALUES (15, '2022105010241', 'admin123', '赖建宇', '123456789@qq.com', '123456789', 'USER', '2025-06-05 19:25:17', '2025-06-05 19:25:17');
INSERT INTO `users` VALUES (16, '2022105010255', 'admin123', '小明', '12345677@qq.com', '123456788', 'USER', '2025-06-05 19:55:52', '2025-10-03 16:54:31');

SET FOREIGN_KEY_CHECKS = 1;
