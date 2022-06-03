@echo OFF
call mvnw clean package
java -jar target/borysov-veda-test-task-0.0.1-SNAPSHOT.jar