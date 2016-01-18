# Skimpy
Skimpy is a java and JSP based web application for planning meals, managing nutrition and comparing supermarket prices in the UK. It requires a MySQL database, and a web server. The Java JDK Skimpy was developed with and tested on was JRE System Library [jdk1.8.0_25]. The DBSM Skimpy was tested with was XAMPP 3.2.1 and the Webserver Apache Tomcat 8.0.
## Software Requirements
1. A version of Apache Tomcat 8.0 with the latest Skimpy version (1.0) is included with this release. Skimpy is pre-deployed however,Tomcat may need configuration to run in your environment.
2. XAMPP a MySQL Database Management System.
## Installation
Install XAMPP. XAMPP is a MySQL control panel. Skimpy was tested with XAMPP and other MySQL implementations may produce strange results.
XAMPP latest version: https://www.apachefriends.org/index.html 
Run The Installation and follow the instructions. Set up XAMPP as required for your particular environment.
## Compilation (optional)
In order to run Web Scrapers, or initialise the Database, the Skimpy Java files must be compiled. There are three batch files for compiling:
1. **compile_war.bat** creates a web archive which can be deployed using Apache Tomcat.
2. **compile_java_classes.bat** compiles the Skimpy Source code. This is used primarily for running the database and scraper utility, **run_scraper.bat**
## Deployment
1. Extract the directory **Apache-tomcat-8.0.21 (Skimpy)** This folder file contains a version of Tomcat as well as the latest version of Skimpy (1.0) pre-deployed.
2. To deploy manually, download Tomcat version 8.0 (core): http://tomcat.apache.org/download-80.cgi
##Configuring Tomcat
Tomcat may require configuration for your environment. 
1. Go to the /bin directory and run the file: configtest.bat. This script will show if there are any issues with your Tomcat installation. If you are not using the bundled version of Tomcat, you will need to run configtest.bat and configure Tomcat for your environment.
2. Run the script setenv.bat in your Tomcat directory. This will set the Tomcat Environment variables specifically JAVA_HOME and JRE_HOME to the location of your Java JDK and JRE folders on your hard drive. These may need to be changed depending on the path of your JDK and JRE
3. Run configtest.bat to make sure there are no further problems.
##Deploying Skimpy
(If you are using the bundled pre-deployed version of Skimpy, skip this step)
Copy the file "Skimpy.war" to your "Tomcat/webapps" directory. Skimpy will be deployed automatically when you run Tomcat.
##Running XAMPP
A mySQL database is required to run Skimpy. XAMPP is an Apache Database suite.
1. Run the XAMPP control panel. Start Apache and MySQL using the buttons provided. 
![XAMPP Screenshot](https://cloud.githubusercontent.com/assets/8971646/12394831/295b6548-bdf6-11e5-86ad-09e57c8f4cbc.png)
Optionally view your database by opening your web browser and going to the page: "http://localhost/phpmyadmin/index.php".
##Setting Up The Database
To run Skimpy, the database must be set up.
1. Run the file: “Executable/run_scraper.bat” You will be presented with the following options:
![Running Skimpy](https://cloud.githubusercontent.com/assets/8971646/12394952/bdae6da8-bdf6-11e5-8bbe-4412d5d04dfd.png)
Pressing "a" will initialise the database. This means there will be no products or users in any tables.
Pressing "b" will open a sub-menu for initializing an individual table, deleting its contents.
Pressing "c" will open a sub-menu for populating individual tables with test data.
Pressing "d" will open a sub-menu for running one of the web scrapers.
Pressing "q" will quit the menu.
2. Press "a" to initialise the database, or "c" for populating tables with test data.
##Running Skimpy
1. Start the Tomcat web server. Return to your Tomcat/bin directory and run "startup.bat" 
after the database is initialised. Skimpy will now function. To access the interface, open your web browser and open the page **"http://localhost:8080/Skimpy/"**
2. To shutdown the server, run the script "shutdown.bat"
3. To reboot the server, run the script "reboot.bat"
##Maintenance
Skimpy should be relatively maintenance free however, the scrapers need to be run daily to update the database. To run the scrapers, open the file run_scraper.bat and press d to select the option d:> Run scrapers. This will lead to a submenu, where each of the web scrapers (Asda, Sainsbury’s and Tesco) can be run. It is best to schedule this nightly as each scraper takes approximately 
4-6 hours to finish.
