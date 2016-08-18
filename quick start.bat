call mvn clean install
call start cmd /K "cd hsqldb & startServer.bat"
call start cmd /K "cd api & mvn jetty:run"
call pause
