-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: cmpe343_proj2
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `middle_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_primary` decimal(11,0) NOT NULL,
  `phone_secondary` decimal(11,0) DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `linkedin_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birth_date` date NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,'Ahmet',NULL,'Yılmaz','Ahmo',5415550011,5415550012,'ahmet.yilmaz@example.com',NULL,'1996-02-15','2025-11-25 15:40:12','2025-11-25 15:40:12'),(2,'Ayşe',NULL,'Kara','Ays',5453208877,NULL,'ayse.kara@example.com',NULL,'1998-11-22','2025-11-25 15:40:12','2025-11-25 15:40:12'),(3,'Mehmet',NULL,'Demir','Memoli',5324401299,NULL,'mehmet.demir@example.com',NULL,'1995-05-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(4,'Fatma',NULL,'Şahin','Fatiko',5532008899,55320018899,'fatma.sahin@example.com',NULL,'1997-03-28','2025-11-25 15:40:12','2025-11-25 15:40:12'),(5,'Can',NULL,'Arslan','Caneyy',5051123344,NULL,'can.arslan@example.com','https://linkedin.com/in/can-ars','2000-07-17','2025-11-25 15:40:12','2025-11-25 15:40:12'),(6,'Zeynep',NULL,'Çelik','Zeyzey',5319804412,NULL,'zeynep.celik@example.com',NULL,'1999-01-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(7,'Emre',NULL,'Aydın','Emoliko',5427712210,NULL,'emre.aydin@example.com',NULL,'1996-04-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(8,'Sena',NULL,'Coşkun','Seno',5346671288,NULL,'sena.coskun@example.com',NULL,'2004-09-09','2025-11-25 15:40:12','2025-12-03 20:21:33'),(9,'Murat',NULL,'Polat','Murdi',5017782233,NULL,'murat.polat@example.com',NULL,'1994-09-21','2025-11-25 15:40:12','2025-11-25 15:40:12'),(10,'Hilal',NULL,'Çınar','Hilalişko',5539876540,NULL,'hilal.cinar@example.com',NULL,'1999-10-13','2025-11-25 15:40:12','2025-12-03 18:48:06'),(11,'Dıldar',NULL,'Baştuğ','Dıldır',5309871234,NULL,'dildar.bastug@example.com',NULL,'2004-03-01','2025-11-25 15:40:12','2025-12-03 20:25:11'),(12,'Seda',NULL,'Öz','SedaÖz',5312219088,NULL,'seda.oz@example.com',NULL,'1998-08-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(13,'Yiğit','Safa','Yıldırım','Dexi',5503456677,NULL,'ysafa.yildirim@example.com',NULL,'2004-09-21','2025-11-25 15:40:12','2025-12-03 20:23:04'),(14,'Melisa','Irmak','Sır','Meru',5056677788,NULL,'melisa.sır@example.com',NULL,'2005-05-25','2025-11-25 15:40:12','2025-12-03 20:23:55'),(15,'Okan',NULL,'Gür','Oki',5467788991,NULL,'okan.gur@example.com',NULL,'1997-12-29','2025-11-25 15:40:12','2025-11-25 15:40:12'),(16,'Doğukan',NULL,'Furat','Doğu',5301129876,NULL,'dogukan.furat@example.com',NULL,'2003-02-25','2025-11-25 15:40:12','2025-12-03 20:26:16'),(17,'Onur',NULL,'Yurt','Oniko',5528897766,NULL,'onur.yurt@example.com',NULL,'2007-11-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(18,'İrem',NULL,'Aksoy','İroş',5314567890,NULL,'irem.aksoy@example.com',NULL,'1999-07-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(19,'Serkan',NULL,'Uzun','Serko',5389982211,NULL,'serkan.uzun@example.com',NULL,'1994-10-19','2025-11-25 15:40:12','2025-11-25 15:40:12'),(20,'Ceren',NULL,'Dağ','Cero',5059987722,NULL,'ceren.dag@example.com',NULL,'1997-02-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(21,'Gökhan',NULL,'Avcı','Goko',5312298765,NULL,'gokhan.avci@example.com',NULL,'1996-12-20','2025-11-25 15:40:12','2025-11-25 15:40:12'),(22,'Büşra',NULL,'Bilgin','Büco',5523107788,NULL,'busra.bilgin@example.com',NULL,'1998-08-18','2025-11-25 15:40:12','2025-11-25 15:40:12'),(23,'Aykut',NULL,'Elmas','Vine',5309982211,NULL,'aykut.elmas@example.com',NULL,'1991-02-15','2025-11-25 15:40:12','2025-11-25 15:40:12'),(24,'Ece',NULL,'Sarı','Eci',5058896622,NULL,'ece.sari@example.com',NULL,'1995-03-03','2025-11-25 15:40:12','2025-11-25 15:40:12'),(25,'Kaan',NULL,'Aslan','Kani',5423321987,NULL,'kaan.aslan@example.com',NULL,'1999-09-27','2025-11-25 15:40:12','2025-11-25 15:40:12'),(26,'Derya',NULL,'Uçar','Dero',5314412233,NULL,'derya.ucar@example.com',NULL,'1998-06-10','2025-11-25 15:40:12','2025-11-25 15:40:12'),(27,'Umut',NULL,'Şen','Umo',5547766122,NULL,'umut.sen@example.com',NULL,'1997-10-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(28,'Selin',NULL,'Bozkurt','Selo',5012289944,NULL,'selin.bozkurt@example.com',NULL,'1996-11-05','2025-11-25 15:40:12','2025-12-04 10:10:55'),(29,'Yasin',NULL,'Güçlü','Yaso',5461298833,NULL,'yasin.guclu@example.com',NULL,'1997-07-23','2025-11-25 15:40:12','2025-11-25 15:40:12'),(30,'Gül',NULL,'Yıldız','Gülo',5321195588,NULL,'gul.yildiz@example.com',NULL,'1998-04-01','2025-11-25 15:40:12','2025-11-25 15:40:12'),(31,'Ali','Rıza','Tan','Alico',5319876655,NULL,'aliriza.tan@example.com','https://linkedin.com/in/aliriza-tan','1994-03-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(32,'Mustafa','Kemal','Ergin','Musti',5528899911,NULL,'mustafakemal.ergin@example.com',NULL,'1995-09-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(33,'Deniz','Can','Öztürk','Deno',5307781231,NULL,'denizcan.ozturk@example.com','https://linkedin.com/in/denizcan-ozturk','1996-05-05','2025-11-25 15:40:12','2025-11-25 15:40:12'),(34,'Halil','İbrahim','Kurt','Haliko',5019912277,NULL,'halilibrahim.kurt@example.com',NULL,'1997-08-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(35,'Ezgi','Nur','Yaman','Ezgo',5468877621,NULL,'ezginur.yaman@example.com','https://linkedin.com/in/ezginur-yaman','1998-11-14','2025-11-25 15:40:12','2025-11-25 15:40:12'),(36,'Meryem','Su','Sağlam','Merso',5311102233,NULL,'meryemsu.saglam@example.com',NULL,'1999-02-16','2025-11-25 15:40:12','2025-11-25 15:40:12'),(37,'Hasan','Hüseyin','Baran','Haso',5537719022,NULL,'hasanhuseyin.baran@example.com',NULL,'1997-06-03','2025-11-25 15:40:12','2025-11-25 15:40:12'),(38,'Aslı','Enver','Erkan','Aslo',5543209876,NULL,'aslıenver.erkan@example.com','https://linkedin.com/in/aslienver-erkan','2000-02-29','2025-11-25 15:40:12','2025-11-25 15:40:12'),(39,'Betül','Mina','Şimşek','Betu',5051122774,NULL,'betulmina.simsek@example.com',NULL,'1998-03-19','2025-11-25 15:40:12','2025-11-25 15:40:12'),(40,'Yasemin','Ela','Kır','Yaso',5429981100,NULL,'yaseminela.kir@example.com','https://linkedin.com/in/yaseminela-kir','1994-10-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(41,'Kerem','Ali','Budak','Kero',5319876600,5361152277,'keremali.budak@example.com',NULL,'1996-04-04','2025-11-25 15:40:12','2025-11-25 15:40:12'),(42,'Hatice','Nur','Duman','Hatico',5547721199,5529912277,'haticenur.duman@example.com','https://linkedin.com/in/haticenur-duman','1998-01-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(43,'Arda','Can','Toprak','Ardiko',5301197766,5302211199,'ardacan.toprak@example.com',NULL,'1997-09-14','2025-11-25 15:40:12','2025-11-25 15:40:12'),(44,'Pelin','Su','Yazıcı','Pelço',5017789900,5016671122,'pelinsu.yazici@example.com',NULL,'2003-09-27','2025-11-25 15:40:12','2025-11-25 15:40:12'),(45,'Ömer','Faruk','Kaya','Ömö',5469921100,5461189922,'omerfaruk.kaya@example.com',NULL,'1996-11-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(46,'Sinem','Naz','Aydın','Sino',5324498765,5322281199,'sinemnaz.aydin@example.com','https://linkedin.com/in/sinemnaz-aydin','1998-06-06','2025-11-25 15:40:12','2025-11-25 15:40:12'),(47,'İlktan',NULL,'Ar','Hocam',2125336532,5019051905,'ilktana@khas.edu.tr','https://www.linkedin.com/in/ilktan-ar-bba92347','1982-11-04','2025-12-03 18:43:25','2025-12-05 19:50:01'),(48,'Tuğba','Melis','Karaca','Tugo',5059912211,5053311122,'tugbamelis.karaca@example.com',NULL,'1999-08-28','2025-11-25 15:40:12','2025-11-25 15:40:12'),(49,'Selami','Bora','Kural','Aybo',5312277811,5311192277,'ayhanbora.kural@example.com','https://linkedin.com/in/ayhanbora-kural','1995-02-08','2025-11-25 15:40:12','2025-11-30 16:06:31'),(50,'Selim','Birkan','Yazıcıoğlu','Selkan',5405903434,5035876267,'selkan@example.com',NULL,'2005-07-30','2025-12-03 20:10:11','2025-12-03 20:10:11'),(51,'Veli','','Deli','Ali',5415550069,NULL,'veli@exam.com',NULL,'2005-01-01','2025-12-05 20:34:21','2025-12-05 20:34:21');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'tt','0e07cf830957701d43c183f1515f63e6b68027e528f43ef52b1527a520ddec82','test','Tester','TESTER','2025-11-25 15:18:04','2025-12-05 20:42:19',1),(2,'jd','ad3e69e9aa860657cc6476770fe253d08198746b9fcf9dc3186b47eb85c30335','Junior','Developer','JUNIOR_DEV','2025-11-25 15:18:04','2025-12-05 20:35:45',1),(3,'sd','03042cf8100db386818cee4ff0f2972431a62ed78edbd09ac08accfabbefd818','Senior','Developer','SENIOR_DEV','2025-11-25 15:18:04','2025-12-05 17:01:26',1),(4,'man','48b676e2b107da679512b793d5fd4cc4329f0c7c17a97cf6e0e3d1005b600b03','Main','Manager','MANAGER','2025-11-25 15:18:04','2025-12-03 19:58:14',1),(6,'a','ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb','aa','bb','TESTER','2025-12-05 20:35:55','2025-12-05 20:35:55',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-06  0:07:44
