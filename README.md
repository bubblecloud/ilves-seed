Ilves Seed Project
==================

Ilves simplifies Java web site creation. This is seed project to simplify new project setup.

Preconditions
------------

1. Git
2. JDK 7
3. Maven 3

Features
--------

1. Example of navigation, page, localization and theme icon customization.
2. Procfile for running the project in Heroku.
3. Build file to generate executable jar.
4. Configuration examples for HSQL (default), PostgreSQL and MySQL.

Clone
-----

Clone projec to your local workstation from command line with the following command:

git clone https://github.com/bubblecloud/ilves-seed.git

Integrated Development Environments
-----------------------------------

Import project to IDE of choice (IntelliJ Idea, Eclipse, NetBeans...) by importing the maven pom.xml.

Building
--------

Build from command line with the following command:

mvn clean install

Executing
---------

Execute from command line with the following command:

ilves

Debugging
---------

Debug from IDE by executing IlvesMain class in debug mode.

Browse
------

Open browser at https://localhost:8443/