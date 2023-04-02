INSERT INTO `city_entity` VALUES (1,'Gothenburg');
INSERT INTO `vehicle` VALUES (2,'Bus'),(7,'Car'),(3,'Diplomat'),(1,'Emergency'),(6,'Foreign'),(5,'Military'),(8,'Motorbike'),(4,'Motorcycle');
INSERT INTO `city_entity_vehicle_join_table` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6);
INSERT INTO `holiday_calendar` VALUES (1,'2013-12-30 00:00:00.000000',1);
INSERT INTO `holiday_month` VALUES (1,0,0,0,0,0,1,0,0,0,0,0,0,1);
INSERT INTO `CITY_SPECIFIC_RULES_ENTITY` VALUES (1,60,0,1,60,1);
INSERT INTO `tarrif` VALUES (1,13.00,'15:00:00','15:29:59',1),(2,13.00,'06:30:00','06:59:59',1),(3,13.00,'08:00:00','08:29:59',1),(4,13.00,'17:00:00','17:59:59',1),(5,8.00,'18:00:00','18:29:59',1),(6,0.00,'00:00:00','05:59:59',1),(7,8.00,'06:00:00','06:29:59',1),(8,8.00,'08:30:00','14:59:59',1),(9,18.00,'07:00:00','07:59:59',1),(10,0.00,'18:30:00','23:59:59',1),(11,18.00,'15:30:00','16:59:59',1);
INSERT INTO `working_calendar` VALUES (1,1,1,0,0,1,1,1,1);