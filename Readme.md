# how to build war package
Run "mvn clean install" under project root directory, you will get the war package under \api\target

# how to run this web application
Run the "quick start.bat" under project root directory, the hsqldb will be started automatically, and then a jetty server will be started.
After that, you can test the REST APIs via Postman or Postclient through http://localhost:8080/??? urls

# how to browse the data stores in database
Run the "startManager.bat" under hsqldb folder, specify "HSQL Database Engine Server" type, URL "jdbc:hsqldb:hsql://localhost/testdb" to open the database