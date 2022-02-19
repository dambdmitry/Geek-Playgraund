# Geek-Playgraund
##My diploma project 2022

###Requirements:
-java 11 with installed system variable JAVA_HOME

-installed maven

###Deploy and run
1) Open terminal in root repository
2) run following commands
   - set CATALINA_HOME=%cd%/web-server
   - ./web-server/bin/startup.bat
   - cd web-app
   - mvn tomcat7:deploy
3) open localhost:8080/Playground