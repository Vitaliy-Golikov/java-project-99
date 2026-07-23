.PHONY: build checkstyle test setup lint

build:
	cd app && ./gradlew clean build

checkstyle:
	cd app && ./gradlew checkstyleMain

test:
	cd app && ./gradlew test

setup:
	cd app && ./gradlew wrapper --gradle-version 9.5.0

lint:
	cd app && ./gradlew checkstyleMain checkstyleTest
