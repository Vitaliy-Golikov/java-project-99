.PHONY: build checkstyle test setup lint

build:
	cd code && ./gradlew clean build

checkstyle:
	cd code && ./gradlew checkstyleMain

test:
	cd code && ./gradlew test

setup:
	cd code && ./gradlew wrapper --gradle-version 9.5.0

lint:
	cd code && ./gradlew checkstyleMain checkstyleTest
