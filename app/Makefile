.PHONY: build checkstyle test

run-dist:
	./build/install/java-project-99/bin/java-project-99

build:
	cd app && ./gradlew clean build

checkstyle:
	cd app && ./gradlew checkstyleMain

test:
	cd app && ./gradlew test

report:
	cd app && ./gradlew jacocoTestReport

lint:
	cd app && ./gradlew checkstyleMain checkstyleTest