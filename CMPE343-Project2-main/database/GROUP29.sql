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
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,'Ahmet',NULL,'Yılmaz','Ahmo',5415550011,5415550012,'ahmet.yilmaz@example.com',NULL,'1996-02-15','2025-11-25 15:40:12','2025-11-25 15:40:12'),(2,'Ayşe',NULL,'Kara','Ays',5453208877,NULL,'ayse.kara@example.com',NULL,'1998-11-22','2025-11-25 15:40:12','2025-11-25 15:40:12'),(3,'Mehmet',NULL,'Demir','Memoli',5324401299,NULL,'mehmet.demir@example.com',NULL,'1995-05-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(4,'Fatma',NULL,'Şahin','Fatiko',5532008899,55320018899,'fatma.sahin@example.com',NULL,'1997-03-28','2025-11-25 15:40:12','2025-11-25 15:40:12'),(5,'Can',NULL,'Arslan','Caneyy',5051123344,NULL,'can.arslan@example.com','https://linkedin.com/in/can-ars','2000-07-17','2025-11-25 15:40:12','2025-11-25 15:40:12'),(6,'Zeynep',NULL,'Çelik','Zeyzey',5319804412,NULL,'zeynep.celik@example.com',NULL,'1999-01-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(7,'Emre',NULL,'Aydın','Emoliko',5427712210,NULL,'emre.aydin@example.com',NULL,'1996-04-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(8,'Elif',NULL,'Koç','Elf',5346671288,NULL,'elif.koc@example.com',NULL,'1998-12-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(9,'Murat',NULL,'Polat','Murdi',5017782233,NULL,'murat.polat@example.com',NULL,'1994-09-21','2025-11-25 15:40:12','2025-11-25 15:40:12'),(10,'Hilal',NULL,'Çınar','Hilalişko',5539876540,NULL,'hilal.cinar@example.com',NULL,'1999-10-13','2025-11-25 15:40:12','2025-11-25 15:40:12'),(11,'Burak',NULL,'Ateş','Burko',5309871234,NULL,'burak.ates@example.com',NULL,'1997-06-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(12,'Seda',NULL,'Öz','SedaÖz',5312219088,NULL,'seda.oz@example.com',NULL,'1998-08-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(13,'Hakan',NULL,'Yalçın','HakYal',5503456677,NULL,'hakan.yalcin@example.com',NULL,'1995-03-15','2025-11-25 15:40:12','2025-11-25 15:40:12'),(14,'Melisa',NULL,'Sır','Bal',5056677788,NULL,'melisa.sır@example.com',NULL,'2005-05-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(15,'Okan',NULL,'Gür','Oki',5467788991,NULL,'okan.gur@example.com',NULL,'1997-12-29','2025-11-25 15:40:12','2025-11-25 15:40:12'),(16,'Naz',NULL,'Durmaz','Nazo',5301129876,NULL,'naz.durmaz@example.com',NULL,'1998-01-30','2025-11-25 15:40:12','2025-11-25 15:40:12'),(17,'Onur',NULL,'Yurt','Oniko',5528897766,NULL,'onur.yurt@example.com',NULL,'2007-11-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(18,'İrem',NULL,'Aksoy','İroş',5314567890,NULL,'irem.aksoy@example.com',NULL,'1999-07-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(19,'Serkan',NULL,'Uzun','Serko',5389982211,NULL,'serkan.uzun@example.com',NULL,'1994-10-19','2025-11-25 15:40:12','2025-11-25 15:40:12'),(20,'Ceren',NULL,'Dağ','Cero',5059987722,NULL,'ceren.dag@example.com',NULL,'1997-02-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(21,'Gökhan',NULL,'Avcı','Goko',5312298765,NULL,'gokhan.avci@example.com',NULL,'1996-12-20','2025-11-25 15:40:12','2025-11-25 15:40:12'),(22,'Büşra',NULL,'Bilgin','Büco',5523107788,NULL,'busra.bilgin@example.com',NULL,'1998-08-18','2025-11-25 15:40:12','2025-11-25 15:40:12'),(23,'Aykut',NULL,'Elmas','Vine',5309982211,NULL,'aykut.elmas@example.com',NULL,'1991-02-15','2025-11-25 15:40:12','2025-11-25 15:40:12'),(24,'Ece',NULL,'Sarı','Eci',5058896622,NULL,'ece.sari@example.com',NULL,'1995-03-03','2025-11-25 15:40:12','2025-11-25 15:40:12'),(25,'Kaan',NULL,'Aslan','Kani',5423321987,NULL,'kaan.aslan@example.com',NULL,'1999-09-27','2025-11-25 15:40:12','2025-11-25 15:40:12'),(26,'Derya',NULL,'Uçar','Dero',5314412233,NULL,'derya.ucar@example.com',NULL,'1998-06-10','2025-11-25 15:40:12','2025-11-25 15:40:12'),(27,'Umut',NULL,'Şen','Umo',5547766122,NULL,'umut.sen@example.com',NULL,'1997-10-02','2025-11-25 15:40:12','2025-11-25 15:40:12'),(28,'Selin',NULL,'Bozkurt','Selo',5012289944,NULL,'selin.bozkurt@example.com',NULL,'1996-11-05','2025-11-25 15:40:12','2025-11-25 15:40:12'),(29,'Yasin',NULL,'Güçlü','Yaso',5461298833,NULL,'yasin.guclu@example.com',NULL,'1997-07-23','2025-11-25 15:40:12','2025-11-25 15:40:12'),(30,'Gül',NULL,'Yıldız','Gülo',5321195588,NULL,'gul.yildiz@example.com',NULL,'1998-04-01','2025-11-25 15:40:12','2025-11-25 15:40:12'),(31,'Ali','Rıza','Tan','Alico',5319876655,NULL,'aliriza.tan@example.com','https://linkedin.com/in/aliriza-tan','1994-03-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(32,'Mustafa','Kemal','Ergin','Musti',5528899911,NULL,'mustafakemal.ergin@example.com',NULL,'1995-09-09','2025-11-25 15:40:12','2025-11-25 15:40:12'),(33,'Deniz','Can','Öztürk','Deno',5307781231,NULL,'denizcan.ozturk@example.com','https://linkedin.com/in/denizcan-ozturk','1996-05-05','2025-11-25 15:40:12','2025-11-25 15:40:12'),(34,'Halil','İbrahim','Kurt','Haliko',5019912277,NULL,'halilibrahim.kurt@example.com',NULL,'1997-08-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(35,'Ezgi','Nur','Yaman','Ezgo',5468877621,NULL,'ezginur.yaman@example.com','https://linkedin.com/in/ezginur-yaman','1998-11-14','2025-11-25 15:40:12','2025-11-25 15:40:12'),(36,'Meryem','Su','Sağlam','Merso',5311102233,NULL,'meryemsu.saglam@example.com',NULL,'1999-02-16','2025-11-25 15:40:12','2025-11-25 15:40:12'),(37,'Hasan','Hüseyin','Baran','Haso',5537719022,NULL,'hasanhuseyin.baran@example.com',NULL,'1997-06-03','2025-11-25 15:40:12','2025-11-25 15:40:12'),(38,'Aslı','Enver','Erkan','Aslo',5543209876,NULL,'aslıenver.erkan@example.com','https://linkedin.com/in/aslienver-erkan','2000-02-29','2025-11-25 15:40:12','2025-11-25 15:40:12'),(39,'Betül','Mina','Şimşek','Betu',5051122774,NULL,'betulmina.simsek@example.com',NULL,'1998-03-19','2025-11-25 15:40:12','2025-11-25 15:40:12'),(40,'Yasemin','Ela','Kır','Yaso',5429981100,NULL,'yaseminela.kir@example.com','https://linkedin.com/in/yaseminela-kir','1994-10-25','2025-11-25 15:40:12','2025-11-25 15:40:12'),(41,'Kerem','Ali','Budak','Kero',5319876600,5361152277,'keremali.budak@example.com',NULL,'1996-04-04','2025-11-25 15:40:12','2025-11-25 15:40:12'),(42,'Hatice','Nur','Duman','Hatico',5547721199,5529912277,'haticenur.duman@example.com','https://linkedin.com/in/haticenur-duman','1998-01-12','2025-11-25 15:40:12','2025-11-25 15:40:12'),(43,'Arda','Can','Toprak','Ardiko',5301197766,5302211199,'ardacan.toprak@example.com',NULL,'1997-09-14','2025-11-25 15:40:12','2025-11-25 15:40:12'),(44,'Pelin','Su','Yazıcı','Pelço',5017789900,5016671122,'pelinsu.yazici@example.com',NULL,'2003-09-27','2025-11-25 15:40:12','2025-11-25 15:40:12'),(45,'Ömer','Faruk','Kaya','Ömö',5469921100,5461189922,'omerfaruk.kaya@example.com',NULL,'1996-11-11','2025-11-25 15:40:12','2025-11-25 15:40:12'),(46,'Sinem','Naz','Aydın','Sino',5324498765,5322281199,'sinemnaz.aydin@example.com','https://linkedin.com/in/sinemnaz-aydin','1998-06-06','2025-11-25 15:40:12','2025-11-25 15:40:12'),(47,'Efe','Can','Güneş','Efeco',5537789900,5532211177,'efecan.gunes@example.com',NULL,'1997-03-17','2025-11-25 15:40:12','2025-11-25 15:40:12'),(48,'Tuğba','Melis','Karaca','Tugo',5059912211,5053311122,'tugbamelis.karaca@example.com',NULL,'1999-08-28','2025-11-25 15:40:12','2025-11-25 15:40:12'),(49,'Ayhan','Bora','Kural','Aybo',5312277811,5311192277,'ayhanbora.kural@example.com','https://linkedin.com/in/ayhanbora-kural','1995-02-08','2025-11-25 15:40:12','2025-11-25 15:40:12'),(50,'İlker','Cem','Konuk','İlko',5387789922,5382215566,'ilkercem.konuk@example.com',NULL,'1996-07-19','2025-11-25 15:40:12','2025-11-25 15:40:12');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'tt','tt','Test','Tester','TESTER','2025-11-25 15:18:04','2025-11-25 15:18:04',1),(2,'jd','jd','Junior','Developer','JUNIOR_DEV','2025-11-25 15:18:04','2025-11-25 15:18:04',1),(3,'sd','sd','Senior','Developer','SENIOR_DEV','2025-11-25 15:18:04','2025-11-25 15:18:04',1),(4,'man','man','Main','Manager','MANAGER','2025-11-25 15:18:04','2025-11-25 15:18:04',1);
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

-- Dump completed on 2025-11-25 18:48:10
