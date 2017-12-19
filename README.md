HJB4U - HyperJAXB 4 Users
=========================

HJB4U is a toolchain for the creation of a HyperJAXB front end for database 
users. It was created because we had a number of databases users that had to 
convert their data into XML for government reporting purposes. Unfortunately the 
schema was constantly evolving, with monthly updates over the course of a year(s). 
I simply didn't have the time to tweak the code every time they decided to update
the schema, so HJB4U was born.

# Installation
To install HJB4U you will need [Apache Maven](https://maven.apache.org).

## Java Transaction API
One of the dependencies of HJB4U depends on Oracls's JTA library. 
To install the JTA dependency go to this [Oracle](http://www.oracle.com/technetwork/java/javaee/jta/index.html) site and download the 1.0.1B class files.

Then on the command line go to the directory where `jta-1_0_1B-classes.zip` downloaded to and execute:

     mvn install:install-file -Dfile=jta-1_0_1B-classes.zip -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar

## Install the Project

To install the project checkout the source and run maven install:

    git clone https://github.com/nigelb/HJB4U.git
    cd HJB4U
    mvn install

# Using HJB4U
To use HJB4U you will need an XML schema file.

## Create a new project from the Archetype

On the command line we will generate the new project:

    mvn archetype:generate -DarchetypeGroupId=hjb4u -DarchetypeArtifactId=hjb4u-archetype -DarchetypeVersion=0.0.2 -DgroupId=hjb4u -DartifactId=hjb4u-test -Dversion=0.0.1-SNAPSHOT
    
If it asks you to confirm the details, ensure they are correct and press Y then press `enter`. Then it will generate the following files:

    $ find hjb4u-test/
    hjb4u-test/
    hjb4u-test/pom.xml
    hjb4u-test/src
    hjb4u-test/src/main
    hjb4u-test/src/main/assembly
    hjb4u-test/src/main/assembly/config.xml
    hjb4u-test/src/main/resources
    hjb4u-test/src/main/resources/META-INF
    hjb4u-test/src/main/resources/META-INF/hjb4u
    hjb4u-test/src/main/resources/META-INF/hjb4u/conf
    hjb4u-test/src/main/resources/META-INF/hjb4u/conf/persistence.properties
    hjb4u-test/src/main/resources/META-INF/hjb4u/conf/resources.xml
    hjb4u-test/src/main/resources/META-INF/hjb4u/schema
    hjb4u-test/src/main/resources/META-INF/hjb4u/xslt
    hjb4u-test/src/main/resources/META-INF/hjb4u/xslt/999_remove_HJID.xsl
    
Next we will copy our schema into the `hjb4u-test/src/main/resources/META-INF/hjb4u/schema
` directory.


