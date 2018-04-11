-- MySQL dump 10.13  Distrib 5.6.22, for osx10.8 (x86_64)
--
-- Host: localhost    Database: cms_v3
-- ------------------------------------------------------
-- Server version	5.6.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES UTF8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `cms_media_template`
--

LOCK TABLES `cms_media_template` WRITE;
/*!40000 ALTER TABLE `cms_media_template` DISABLE KEYS */;
INSERT INTO `cms_media_template` (`id`, `create_time`, `update_time`, `code`, `external_code`, `status`, `title`, `title_tag`, `type`, `a_bitrate`, `a_codec`, `v_2pass`, `v_bitrate`, `v_codec`, `v_format`, `v_framerate`, `v_gop`, `v_max_bitrate`, `v_min_bitrate`, `v_profile`, `v_profile_level`, `v_resolution`, `v_bitrate_mode`, `transcode_mode`, `category`, `definition`, `prefix_desc`, `suffix_desc`) VALUES (1,'2018-03-10 14:28:16',NULL,'2m_CBR','bestv-profile-2m-CBR-01',1,'2m-CBR-01','SD_2M',0,128,'MP2',0,2000,'H264','TS',25,NULL,NULL,NULL,NULL,NULL,'576i','CBR',1,NULL,NULL,NULL,NULL),(2,'2018-03-10 14:28:16',NULL,'2m_VBR','bestv-profile-2m-VBR-01',1,'2m-VBR-01','HD_2M',0,128,'MP2',0,2300,'H264','TS',25,NULL,NULL,NULL,NULL,NULL,'720P','VBR',1,NULL,NULL,NULL,NULL),(3,'2018-03-10 14:28:16',NULL,'4m_VBR','bestv-profile-4m-VBR-01',1,'4m-VBR-01','HD_4M',0,64,'AAC',0,4000,'H264','TS',25,NULL,NULL,NULL,NULL,NULL,'1080P','VBR',1,NULL,NULL,NULL,NULL),(4,'2018-03-10 14:28:16',NULL,'6m_VBR','bestv-profile-6m-VBR-01',1,'6m-VBR-01','HD_6M',0,128,'MP2',0,6000,'H264','TS',25,NULL,NULL,NULL,NULL,NULL,'1080P','VBR',1,NULL,NULL,NULL,NULL),(19,'2018-03-10 15:58:56',NULL,'8m_CBR','bestv-profile-8m-CBR-03',1,'8m_CBR-01','HD_8M',0,128,'MP2',0,8000,'H264','TS',25,NULL,NULL,NULL,NULL,NULL,'1080P','CBR',1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `cms_media_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-28 10:27:18
