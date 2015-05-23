Concordion can be run as a regular JUnit test either by extending `ConcordionTestCase` (with JUnit 3) or using the annotation `@RunWith(ConcordionRunner.class)` (with JUnit 4)

Sometimes I wish to execute the unit tests of my project first, and then execute the specifications I've written using Concordion. If using ant is not a big deal, but when using maven I need to configure carefully the [surefire plugin](http://maven.apache.org/plugins/maven-surefire-plugin/).

For example, suppose I want to execute the unit tests in the Concordion project I have just checked out but I do not want to execute all the tests for the specs. Let's say unit-tests to the first ones and acceptance-tests to the latest (the specs). Unit-tests are regular JUnit tests but acceptance-tests are run with Concordion.

I will configure (in the pom.xml file) the surefire plugin with the following lines:

```
<plugin>
	<artifactId>maven-surefire-plugin</artifactId>
	<configuration>
		<skip>true</skip>
```
This skip = true is needed so the rest of the executions can be done.
```
		<systemProperties>
			<property>
				<name>concordion.output.dir</name>
				<value>target/concordion</value>
			</property>
		</systemProperties>
```
This should be a surefire bug, but I must put here the value for the system property that we use to tell Concordion where to leave the results (`concordion.output.dir`). If I try to put these elements in the acceptance-test section, it won't work.
```
	</configuration>
	<executions>
		<execution>
			<id>unit-test</id>
			<phase>test</phase>
			<goals>
				<goal>test</goal>
			</goals>
```
The [phase](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) **test** is executed after compiling and before packaging and is different to the phase **integration-test** which is executed after packaging. (We can also use `pre-integration-test` and `post-integration-test`).
The goal `test` is the command to the surefire plugin that executes the tests.
```
			<configuration>
				<skip>false</skip>
				<includes>
					<include>**/*Test.java</include>
				</includes>
				<excludes>
					<exclude>**/spec/**/*Test.java</exclude>
					<exclude>**/Abstract*</exclude>
				</excludes>
			</configuration>
```
With skip = false I'm saying surefire to execute with this configuration: including the regular JUnit classes, but excluding the ones extending `ConcordionTestCase` or with `@RunWith(ConcordionRunner.class)`.
```
		</execution>
		<execution>
			<id>acceptance-test</id>
			<phase>integration-test</phase>
			<goals>
				<goal>test</goal>
			</goals>
			<configuration>
				<skip>false</skip>
				<includes>
					<include>**/spec/**/*Test.java</include>
				</includes>
				<excludes>
					<exclude>**/Abstract*</exclude>
				</excludes>
			</configuration>
		</execution>
```
Similar to the execution `unit-test`, the execution `acceptance-test` executes the classes under the `spec` folder, but in a different moment: the **integration-test** phase, after having executed the unit tests.
```
	</executions>
</plugin>
```


  * When I want to execute only the unit-tests, I run "mvn clean test".
  * When I want to execute both unit and acceptance-tests, I run "mvn clean integration-test"
  * If I want to run only the acceptance-tests, I change the flag skip = false to true.