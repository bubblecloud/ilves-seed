Ilves Seed Project
==================

Ilves simplifies Java web site creation. This is seed project for east new project setup.

Requirements
------------

1. Java 7
2. Maven 3
3. Postgresql (JPA, Eclipse Link and Liquibase used but other databases not tested.)

Features
--------

1. Example of navigation, page, localization and theme icon customization.
2. Procfile for running the project in Heroku.
3. Build file to generate executable jar.

Usage
-----

1. Import project to IDE of choice (IntelliJ Idea, Eclipse, NetBeans...) by importing the maven pom.xml.
2. Build from command line: 'mvn clean install'
3. Execute from command line: 'java -jar target/ilves-seed.jar'