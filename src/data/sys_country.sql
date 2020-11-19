/*
Navicat MySQL Data Transfer

Source Server         : 121.89.188.211
Source Server Version : 80015
Source Host           : 121.89.188.211:3306
Source Database       : quote_new

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2020-10-21 14:55:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_country
-- ----------------------------
DROP TABLE IF EXISTS `sys_country`;
CREATE TABLE `sys_country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL COMMENT '国家名称',
  `code` varchar(50) DEFAULT NULL COMMENT '国家编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8 COMMENT='国家信息表';

-- ----------------------------
-- Records of sys_country
-- ----------------------------
INSERT INTO `sys_country` VALUES ('1', '中国', 'CHINA');
INSERT INTO `sys_country` VALUES ('2', '阿尔及利亚', 'ALGERIA');
INSERT INTO `sys_country` VALUES ('3', '安道尔', 'ANDORRA');
INSERT INTO `sys_country` VALUES ('4', '安圭拉', 'ANGUILLA');
INSERT INTO `sys_country` VALUES ('5', '安提瓜和巴布达', 'ANTIGUA AND BARBUDA');
INSERT INTO `sys_country` VALUES ('6', '亚美尼亚', 'ARMENIA');
INSERT INTO `sys_country` VALUES ('7', '澳大利亚', 'AUSTRALIA');
INSERT INTO `sys_country` VALUES ('8', '阿塞拜疆', 'AZERBAIJAN');
INSERT INTO `sys_country` VALUES ('9', '孟加拉国', 'BANGLADESH');
INSERT INTO `sys_country` VALUES ('10', '白俄罗斯', 'BELARUS');
INSERT INTO `sys_country` VALUES ('11', '伯利兹', 'BELIZE');
INSERT INTO `sys_country` VALUES ('12', '百慕大', 'BERMUDA');
INSERT INTO `sys_country` VALUES ('13', '玻利维亚', 'BOLIVIA');
INSERT INTO `sys_country` VALUES ('14', '博茨瓦纳', 'BOTSWANA');
INSERT INTO `sys_country` VALUES ('15', '巴西', 'BRAZIL');
INSERT INTO `sys_country` VALUES ('16', '英属维尔京群岛', 'BRITISH VIRGIN ISLANDS');
INSERT INTO `sys_country` VALUES ('17', '保加利亚', 'BULGARIA');
INSERT INTO `sys_country` VALUES ('18', '缅甸', 'BURMA');
INSERT INTO `sys_country` VALUES ('19', '柬埔寨', 'CAMBODIA');
INSERT INTO `sys_country` VALUES ('20', '加拿大', 'CANADA');
INSERT INTO `sys_country` VALUES ('21', '开曼群岛', 'CAYMAN ISLANDS');
INSERT INTO `sys_country` VALUES ('22', '乍得', 'CHAD');
INSERT INTO `sys_country` VALUES ('24', '科科斯（基林）群岛', 'COCOS (KEELING) ISLANDS');
INSERT INTO `sys_country` VALUES ('25', '科摩罗', 'COMOROS');
INSERT INTO `sys_country` VALUES ('26', '刚果（布）', 'CONGO, REPUBLIC OF THE');
INSERT INTO `sys_country` VALUES ('27', '哥斯达黎加', 'COSTA RICA');
INSERT INTO `sys_country` VALUES ('28', '克罗地亚', 'CROATIA');
INSERT INTO `sys_country` VALUES ('29', '塞浦路斯', 'CYPRUS');
INSERT INTO `sys_country` VALUES ('30', '丹麦', 'DENMARK');
INSERT INTO `sys_country` VALUES ('31', '多米尼克', 'DOMINICA');
INSERT INTO `sys_country` VALUES ('32', '厄瓜多尔', 'ECUADOR');
INSERT INTO `sys_country` VALUES ('33', '萨尔瓦多', 'EL SALVADOR');
INSERT INTO `sys_country` VALUES ('34', '厄立特里亚', 'ERITREA');
INSERT INTO `sys_country` VALUES ('35', '埃塞俄比亚', 'ETHIOPIA');
INSERT INTO `sys_country` VALUES ('36', '福克兰群岛（马尔维纳斯）', 'FALKLAND ISLANDS (ISLAS MALVINAS)');
INSERT INTO `sys_country` VALUES ('37', '斐济', 'FIJI');
INSERT INTO `sys_country` VALUES ('38', '法国', 'FRANCE');
INSERT INTO `sys_country` VALUES ('39', '法属波利尼西亚', 'FRENCH POLYNESIA');
INSERT INTO `sys_country` VALUES ('40', '加蓬', 'GABON');
INSERT INTO `sys_country` VALUES ('41', '德国', 'GERMANY');
INSERT INTO `sys_country` VALUES ('42', '直布罗陀', 'GIBRALTAR');
INSERT INTO `sys_country` VALUES ('43', '格陵兰', 'GREENLAND');
INSERT INTO `sys_country` VALUES ('44', '瓜德罗普', 'GUADELOUPE');
INSERT INTO `sys_country` VALUES ('45', '危地马拉', 'GUATEMALA');
INSERT INTO `sys_country` VALUES ('46', '几内亚', 'GUINEA');
INSERT INTO `sys_country` VALUES ('47', '圭亚那', 'GUYANA');
INSERT INTO `sys_country` VALUES ('48', '洪都拉斯', 'HONDURAS');
INSERT INTO `sys_country` VALUES ('49', '匈牙利', 'HUNGARY');
INSERT INTO `sys_country` VALUES ('50', '印度', 'INDIA');
INSERT INTO `sys_country` VALUES ('51', '伊朗', 'IRAN');
INSERT INTO `sys_country` VALUES ('52', '爱尔兰', 'IRELAND');
INSERT INTO `sys_country` VALUES ('53', '意大利', 'ITALY');
INSERT INTO `sys_country` VALUES ('54', '日本', 'JAPAN');
INSERT INTO `sys_country` VALUES ('55', '约旦', 'JORDAN');
INSERT INTO `sys_country` VALUES ('56', '肯尼亚', 'KENYA');
INSERT INTO `sys_country` VALUES ('57', '朝鲜', 'KOREA, NORTH');
INSERT INTO `sys_country` VALUES ('58', '科威特', 'KUWAIT');
INSERT INTO `sys_country` VALUES ('59', '老挝', 'LAOS');
INSERT INTO `sys_country` VALUES ('60', '黎巴嫩', 'LEBANON');
INSERT INTO `sys_country` VALUES ('61', '利比里亚', 'LIBERIA');
INSERT INTO `sys_country` VALUES ('62', '列支敦士登', 'LIECHTENSTEIN');
INSERT INTO `sys_country` VALUES ('63', '卢森堡', 'LUXEMBOURG');
INSERT INTO `sys_country` VALUES ('64', '前南马其顿', 'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF');
INSERT INTO `sys_country` VALUES ('65', '马拉维', 'MALAWI');
INSERT INTO `sys_country` VALUES ('66', '马尔代夫', 'MALDIVES');
INSERT INTO `sys_country` VALUES ('67', '马耳他', 'MALTA');
INSERT INTO `sys_country` VALUES ('68', '马绍尔群岛', 'MARSHALL ISLANDS');
INSERT INTO `sys_country` VALUES ('69', '毛里塔尼亚', 'MAURITANIA');
INSERT INTO `sys_country` VALUES ('70', '马约特', 'MAYOTTE');
INSERT INTO `sys_country` VALUES ('71', '密克罗尼西亚', 'MICRONESIA, FEDERATED STATES OF');
INSERT INTO `sys_country` VALUES ('72', '摩纳哥', 'MONACO');
INSERT INTO `sys_country` VALUES ('73', '蒙特塞拉特', 'MONTSERRAT');
INSERT INTO `sys_country` VALUES ('74', '莫桑比克', 'MOZAMBIQUE');
INSERT INTO `sys_country` VALUES ('75', '瑙鲁', 'NAURU');
INSERT INTO `sys_country` VALUES ('76', '荷兰', 'NETHERLANDS');
INSERT INTO `sys_country` VALUES ('77', '新喀里多尼亚', 'NEW CALEDONIA');
INSERT INTO `sys_country` VALUES ('78', '尼加拉瓜', 'NICARAGUA');
INSERT INTO `sys_country` VALUES ('79', '尼日利亚', 'NIGERIA');
INSERT INTO `sys_country` VALUES ('80', '诺福克岛', 'NORFOLK ISLAND');
INSERT INTO `sys_country` VALUES ('81', '挪威', 'NORWAY');
INSERT INTO `sys_country` VALUES ('82', '巴基斯坦', 'PAKISTAN');
INSERT INTO `sys_country` VALUES ('83', '巴拿马', 'PANAMA');
INSERT INTO `sys_country` VALUES ('84', '巴拉圭', 'PARAGUAY');
INSERT INTO `sys_country` VALUES ('85', '菲律宾', 'PHILIPPINES');
INSERT INTO `sys_country` VALUES ('86', '波兰', 'POLAND');
INSERT INTO `sys_country` VALUES ('87', '卡塔尔', 'QATAR');
INSERT INTO `sys_country` VALUES ('88', '罗马尼亚', 'ROMANIA');
INSERT INTO `sys_country` VALUES ('89', '卢旺达', 'RWANDA');
INSERT INTO `sys_country` VALUES ('90', '圣基茨和尼维斯', 'SAINT KITTS AND NEVIS');
INSERT INTO `sys_country` VALUES ('91', '圣皮埃尔和密克隆', 'SAINT PIERRE AND MIQUELON');
INSERT INTO `sys_country` VALUES ('92', '萨摩亚', 'SAMOA');
INSERT INTO `sys_country` VALUES ('93', '圣多美和普林西比', 'SAO TOME AND PRINCIPE');
INSERT INTO `sys_country` VALUES ('94', '塞内加尔', 'SENEGAL');
INSERT INTO `sys_country` VALUES ('95', '塞舌尔', 'SEYCHELLES');
INSERT INTO `sys_country` VALUES ('96', '新加坡', 'SINGAPORE');
INSERT INTO `sys_country` VALUES ('97', '斯洛文尼亚', 'SLOVENIA');
INSERT INTO `sys_country` VALUES ('98', '索马里', 'SOMALIA');
INSERT INTO `sys_country` VALUES ('99', '斯里兰卡', 'SRI LANKA');
INSERT INTO `sys_country` VALUES ('100', '苏里南', 'SURINAME');
INSERT INTO `sys_country` VALUES ('101', '斯威士兰', 'SWAZILAND');
INSERT INTO `sys_country` VALUES ('102', '瑞士', 'SWITZERLAND');
INSERT INTO `sys_country` VALUES ('103', '中国台湾', 'TAIWAN');
INSERT INTO `sys_country` VALUES ('104', '坦桑尼亚', 'TANZANIA');
INSERT INTO `sys_country` VALUES ('105', '巴哈马', 'THE BAHAMAS');
INSERT INTO `sys_country` VALUES ('106', '多哥', 'TOGO');
INSERT INTO `sys_country` VALUES ('107', '汤加', 'TONGA');
INSERT INTO `sys_country` VALUES ('108', '突尼斯', 'TUNISIA');
INSERT INTO `sys_country` VALUES ('109', '土库曼斯坦', 'TURKMENISTAN');
INSERT INTO `sys_country` VALUES ('110', '图瓦卢', 'TUVALU');
INSERT INTO `sys_country` VALUES ('111', '乌克兰', 'UKRAINE');
INSERT INTO `sys_country` VALUES ('112', '英国', 'UNITED KINGDOM');
INSERT INTO `sys_country` VALUES ('113', '乌拉圭', 'URUGUAY');
INSERT INTO `sys_country` VALUES ('114', '瓦努阿图', 'VANUATU');
INSERT INTO `sys_country` VALUES ('115', '越南', 'VIETNAM');
INSERT INTO `sys_country` VALUES ('116', '瓦利斯和富图纳', 'WALLIS AND FUTUNA');
INSERT INTO `sys_country` VALUES ('117', '也门', 'YEMEN');
INSERT INTO `sys_country` VALUES ('118', '赞比亚', 'ZAMBIA');
INSERT INTO `sys_country` VALUES ('119', '阿尔巴尼亚', 'ALBANIA');
INSERT INTO `sys_country` VALUES ('120', '美属萨摩亚', 'AMERICAN SAMOA');
INSERT INTO `sys_country` VALUES ('121', '安哥拉', 'ANGOLA');
INSERT INTO `sys_country` VALUES ('122', '南极洲', 'ANTARCTICA');
INSERT INTO `sys_country` VALUES ('123', '阿根廷', 'ARGENTINA');
INSERT INTO `sys_country` VALUES ('124', '阿鲁巴', 'ARUBA');
INSERT INTO `sys_country` VALUES ('125', '奥地利', 'AUSTRIA');
INSERT INTO `sys_country` VALUES ('126', '巴林', 'BAHRAIN');
INSERT INTO `sys_country` VALUES ('127', '巴巴多斯', 'BARBADOS');
INSERT INTO `sys_country` VALUES ('128', '比利时', 'BELGIUM');
INSERT INTO `sys_country` VALUES ('129', '贝宁', 'BENIN');
INSERT INTO `sys_country` VALUES ('130', '不丹', 'BHUTAN');
INSERT INTO `sys_country` VALUES ('131', '波黑', 'BOSNIA AND HERZEGOVINA');
INSERT INTO `sys_country` VALUES ('132', '文莱', 'BRUNEI DARUSSALAM');
INSERT INTO `sys_country` VALUES ('133', '布基纳法索', 'BURKINA FASO');
INSERT INTO `sys_country` VALUES ('134', '布隆迪', 'BURUNDI');
INSERT INTO `sys_country` VALUES ('135', '喀麦隆', 'CAMEROON');
INSERT INTO `sys_country` VALUES ('136', '佛得角', 'CAPE VERDE');
INSERT INTO `sys_country` VALUES ('137', '中非', 'CENTRAL AFRICAN REPUBLIC');
INSERT INTO `sys_country` VALUES ('138', '智利', 'CHILE');
INSERT INTO `sys_country` VALUES ('139', '圣诞岛', 'CHRISTMAS ISLAND');
INSERT INTO `sys_country` VALUES ('140', '哥伦比亚', 'COLOMBIA');
INSERT INTO `sys_country` VALUES ('141', '刚果（金）', 'CONGO, DEMOCRATIC REPUBLIC OF THE');
INSERT INTO `sys_country` VALUES ('142', '库克群岛', 'COOK ISLANDS');
INSERT INTO `sys_country` VALUES ('143', '科特迪瓦', 'COTE D\'IVOIRE');
INSERT INTO `sys_country` VALUES ('144', '古巴', 'CUBA');
INSERT INTO `sys_country` VALUES ('145', '捷克', 'CZECH REPUBLIC');
INSERT INTO `sys_country` VALUES ('146', '吉布提', 'DJIBOUTI');
INSERT INTO `sys_country` VALUES ('147', '多米尼加', 'DOMINICAN REPUBLIC');
INSERT INTO `sys_country` VALUES ('148', '埃及', 'EGYPT');
INSERT INTO `sys_country` VALUES ('149', '赤道几内亚', 'EQUATORIAL GUINEA');
INSERT INTO `sys_country` VALUES ('150', '爱沙尼亚', 'ESTONIA');
INSERT INTO `sys_country` VALUES ('151', '法罗群岛', 'FAROE ISLANDS');
INSERT INTO `sys_country` VALUES ('152', '芬兰', 'FINLAND');
INSERT INTO `sys_country` VALUES ('153', '法属圭亚那', 'FRENCH GUIANA');
INSERT INTO `sys_country` VALUES ('154', '格鲁吉亚', 'GEORGIA');
INSERT INTO `sys_country` VALUES ('155', '加纳', 'GHANA');
INSERT INTO `sys_country` VALUES ('156', '希腊', 'GREECE');
INSERT INTO `sys_country` VALUES ('157', '格林纳达', 'GRENADA');
INSERT INTO `sys_country` VALUES ('158', '关岛', 'GUAM');
INSERT INTO `sys_country` VALUES ('159', '根西岛', 'GUERNSEY');
INSERT INTO `sys_country` VALUES ('160', '几内亚比绍', 'GUINEA-BISSAU');
INSERT INTO `sys_country` VALUES ('161', '海地', 'HAITI');
INSERT INTO `sys_country` VALUES ('162', '梵蒂冈', 'HOLY SEE (VATICAN CITY)');
INSERT INTO `sys_country` VALUES ('163', '中国香港', 'HONG KONG (SAR)');
INSERT INTO `sys_country` VALUES ('164', '冰岛', 'ICELAND');
INSERT INTO `sys_country` VALUES ('165', '印度尼西亚', 'INDONESIA');
INSERT INTO `sys_country` VALUES ('166', '伊拉克', 'IRAQ');
INSERT INTO `sys_country` VALUES ('167', '以色列', 'ISRAEL');
INSERT INTO `sys_country` VALUES ('168', '牙买加', 'JAMAICA');
INSERT INTO `sys_country` VALUES ('169', '哈萨克斯坦', 'KAZAKHSTAN');
INSERT INTO `sys_country` VALUES ('170', '基里巴斯', 'KIRIBATI');
INSERT INTO `sys_country` VALUES ('171', '韩国', 'KOREA, SOUTH');
INSERT INTO `sys_country` VALUES ('172', '吉尔吉斯斯坦', 'KYRGYZSTAN');
INSERT INTO `sys_country` VALUES ('173', '拉脱维亚', 'LATVIA');
INSERT INTO `sys_country` VALUES ('174', '莱索托', 'LESOTHO');
INSERT INTO `sys_country` VALUES ('175', '利比亚', 'LIBYA');
INSERT INTO `sys_country` VALUES ('176', '立陶宛', 'LITHUANIA');
INSERT INTO `sys_country` VALUES ('177', '中国澳门', 'MACAO');
INSERT INTO `sys_country` VALUES ('178', '马达加斯加', 'MADAGASCAR');
INSERT INTO `sys_country` VALUES ('179', '马来西亚', 'MALAYSIA');
INSERT INTO `sys_country` VALUES ('180', '马里', 'MALI');
INSERT INTO `sys_country` VALUES ('181', '马提尼克', 'MARTINIQUE');
INSERT INTO `sys_country` VALUES ('182', '毛里求斯', 'MAURITIUS');
INSERT INTO `sys_country` VALUES ('183', '墨西哥', 'MEXICO');
INSERT INTO `sys_country` VALUES ('184', '摩尔多瓦', 'MOLDOVA');
INSERT INTO `sys_country` VALUES ('185', '蒙古', 'MONGOLIA');
INSERT INTO `sys_country` VALUES ('186', '摩洛哥', 'MOROCCO');
INSERT INTO `sys_country` VALUES ('187', '纳米尼亚', 'NAMIBIA');
INSERT INTO `sys_country` VALUES ('188', '尼泊尔', 'NEPAL');
INSERT INTO `sys_country` VALUES ('189', '荷属安的列斯', 'NETHERLANDS ANTILLES');
INSERT INTO `sys_country` VALUES ('190', '新西兰', 'NEW ZEALAND');
INSERT INTO `sys_country` VALUES ('191', '尼日尔', 'NIGER');
INSERT INTO `sys_country` VALUES ('192', '纽埃', 'NIUE');
INSERT INTO `sys_country` VALUES ('193', '北马里亚纳', 'NORTHERN MARIANA ISLANDS');
INSERT INTO `sys_country` VALUES ('194', '阿曼', 'OMAN');
INSERT INTO `sys_country` VALUES ('195', '帕劳', 'PALAU');
INSERT INTO `sys_country` VALUES ('196', '巴布亚新几内亚', 'PAPUA NEW GUINEA');
INSERT INTO `sys_country` VALUES ('197', '秘鲁', 'PERU');
INSERT INTO `sys_country` VALUES ('198', '葡萄牙', 'PORTUGAL');
INSERT INTO `sys_country` VALUES ('199', '波多黎各', 'PUERTO RICO');
INSERT INTO `sys_country` VALUES ('200', '留尼汪', 'REUNION');
INSERT INTO `sys_country` VALUES ('201', '俄罗斯', 'RUSSIA');
INSERT INTO `sys_country` VALUES ('202', '圣赫勒拿', 'SAINT HELENA');
INSERT INTO `sys_country` VALUES ('203', '圣卢西亚', 'SAINT LUCIA');
INSERT INTO `sys_country` VALUES ('204', '圣文森特和格林纳丁斯', 'SAINT VINCENT AND THE GRENADINES');
INSERT INTO `sys_country` VALUES ('205', '圣马力诺', 'SAN MARINO');
INSERT INTO `sys_country` VALUES ('206', '沙特阿拉伯', 'SAUDI ARABIA');
INSERT INTO `sys_country` VALUES ('207', '塞尔维亚和黑山', 'SERBIA AND MONTENEGRO');
INSERT INTO `sys_country` VALUES ('208', '塞拉利', 'SIERRA LEONE');
INSERT INTO `sys_country` VALUES ('209', '斯洛伐克', 'SLOVAKIA');
INSERT INTO `sys_country` VALUES ('210', '所罗门群岛', 'SOLOMON ISLANDS');
INSERT INTO `sys_country` VALUES ('211', '南非', 'SOUTH AFRICA');
INSERT INTO `sys_country` VALUES ('212', '西班牙', 'SPAIN');
INSERT INTO `sys_country` VALUES ('213', '苏丹', 'SUDAN');
INSERT INTO `sys_country` VALUES ('214', '斯瓦尔巴岛和扬马延岛', 'SVALBARD');
INSERT INTO `sys_country` VALUES ('215', '瑞典', 'SWEDEN');
INSERT INTO `sys_country` VALUES ('216', '叙利亚', 'SYRIA');
INSERT INTO `sys_country` VALUES ('217', '塔吉克斯坦', 'TAJIKISTAN');
INSERT INTO `sys_country` VALUES ('218', '泰国', 'THAILAND');
INSERT INTO `sys_country` VALUES ('219', '冈比亚', 'THE GAMBIA');
INSERT INTO `sys_country` VALUES ('220', '托克劳', 'TOKELAU');
INSERT INTO `sys_country` VALUES ('221', '特立尼达和多巴哥', 'TRINIDAD AND TOBAGO');
INSERT INTO `sys_country` VALUES ('222', '土耳其', 'TURKEY');
INSERT INTO `sys_country` VALUES ('223', '特克斯和凯科斯群岛', 'TURKS AND CAICOS ISLANDS');
INSERT INTO `sys_country` VALUES ('224', '乌干达', 'UGANDA');
INSERT INTO `sys_country` VALUES ('225', '阿拉伯联合酋长国', 'UNITED ARAB EMIRATES');
INSERT INTO `sys_country` VALUES ('226', '美国', 'UNITED STATES');
INSERT INTO `sys_country` VALUES ('227', '乌兹别克斯坦', 'UZBEKISTAN');
INSERT INTO `sys_country` VALUES ('228', '委内瑞拉', 'VENEZUELA');
INSERT INTO `sys_country` VALUES ('229', '美属维尔京群岛', 'VIRGIN ISLANDS');
INSERT INTO `sys_country` VALUES ('230', '南斯拉夫', 'YUGOSLAVIA');
INSERT INTO `sys_country` VALUES ('231', '津巴布韦', 'ZIMBABWE');
INSERT INTO `sys_country` VALUES ('332', '阿富汗', 'AFGHANISTAN');
