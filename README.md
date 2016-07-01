# aida
AIDA is a dtrAck2 lInked Device Api. In other words, you can configure and access your Advanced Realtime Tracking system via our Linked (Data REST) API.

## Installation & Running
Run AIDA on your localhost by typing
```
mvn clean package tomcat7:run
```

You can modify AIDA listening port in the `pom.xml`
```
<build>
	...
	<plugins>
		...
		<plugin>
			<groupId>org.apache.tomcat.maven</groupId>
			<artifactId>tomcat7-maven-plugin</artifactId>
			<version>2.2</version>  	
            		<configuration>
				<server>fReSCO</server>
				<port>8080</port>
				<path>/</path>
			</configuration>
		</plugin>
		...
	</plugins>
</build>
```

## Usage
Get AIDA up and running. Then, use your favourite REST client against one of our REST APIs.

### HTML UI (work in progress)
Point your good ol' browser to `http://localhost:8080` for some self-explanatory HTML UI.

### Linked API
Point your Linked Data client to `http://localhost:8080/api` and `GET` some RDF.

## Contributing
Contributions are very welcome.

## License
This source distribution is subject to the license terms in the LICENSE file found in the top-level directory of this distribution.
You may not use this file except in compliance with the License.

## Third-party Contents
This source distribution includes the third-party items with respective licenses as listed in the THIRD-PARTY file found in the top-level directory of this distribution.