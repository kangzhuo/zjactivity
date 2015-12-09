#!/bin/sh
mvn install:install-file -Dfile=libs/oracle.jar -DgroupId=oracle.jdbc.driver -DartifactId=ojdbc14 -Dversion=10.2.0 -Dpackaging=jar

mvn install:install-file -Dfile=libs/java_memcached-release_2.6.6.jar -DgroupId=java -DartifactId=memcached -Dversion=2.6.6 -Dpackaging=jar
