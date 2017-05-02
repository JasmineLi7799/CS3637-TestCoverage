# CS6367-TestCoverage

Features:

This tool is able to dynamically test the change of the source code files and
return the report of the test classes related to the changed files. The first 
time to run "mvn test", a report will be created to record the absolute paths
of all the classes and the HashCode of each class. After that, if run “mvn test”
again straightly, the report will become empty since no source code file has been
changed. Finally, after changing some source codes, and run “mvn test” once more,
the report will show all the test classes related to the changed classes.

Usage:

Add the following code between the

<build>

  <plugins>

    ...

  </plugins>

<build>

labels:

<plugin>

  <groupId>org.apache.maven.plugins</groupId>

    <artifactId>maven-surefire-plugin</artifactId>

    <configuration>

      <includes>

        <include>**/TestAllPackages.java</include>

          </includes>

          <argLine>-javaagent:"Absolute-path"/code-coverage-1.0-SNAPSHOT.jar=${project.groupId}</argLine>

            <properties>

            <property>

          <name>listener</name>

        <value>edu.utdallas.JUnitExecutionListener</value>

      </property>

    </properties>

  </configuration>

</plugin>

Change the "Absolute-path" to the absolute-path of the jar file.

Add the following code between the

  <dependencies>

      ...

  </dependencies>

labels:

    <dependency>

        <groupId>edu.utdallas</groupId>

           <artifactId>code-coverage</artifactId>

           <version>1.0-SNAPSHOT</version>

    </dependency>

    <dependency>

          <groupId>org.ow2.asm</groupId>

          <artifactId>asm-all</artifactId>

          <version>5.1</version>

    </dependency>

And run "mvn test" after changing the pom.xml file of the project under test.
