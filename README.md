# aida
AIDA (dtrAck2 lInked Device Api) provides a Linked API for the [ART Controller](http://www.ar-tracking.com/products/tracking-systems/arttrack-system/art-controller/).

You can use AIDA to configure and access your [Advanced Realtime Tracking](www.ar-tracking.com) system.
AIDA conforms to the [ARVIDA](http://ww.arvida.de) domain model, and it provides in many aspects an implementation of the upcoming [W3C Web Thing Model](https://www.w3.org/Submission/wot-model/).

AIDA's behavior is modeled using a *labeled transition system* that consists of states and transitions between states.
Transitions are labeled with *actions*. 
An action is an action possibility (or affordance) that AIDA may afford to a client. 
Such affordances describe an end and some means to accomplish that end, e.g. via HTTP-based interaction pattern(s). 

AIDA depends on [art4j](https://github.com/rmrschub/art4j), [fLAPjACk](https://github.com/rmrschub/flapjack) and on [Sodalite](https://github.com/rmrschub/sodalite).


## Setup 

### Installation
Clone the AIDA repository to your machine: 
```
git clone https://github.com/rmrschub/aida.git
```

### Configuration
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
				<server>aida</server>
				<port>8080</port>
				<path>/</path>
			</configuration>
		</plugin>
		...
	</plugins>
</build>
```

AIDA requires an [IGraphStore](https://github.com/rmrschub/igraphstore) for its operation.
For that, you can choose from one of the following options: 
* [Fuseki Server](https://jena.apache.org/documentation/fuseki2/index.html)
* Non-persistent (in-memory) [TDB](https://jena.apache.org/documentation/tdb/index.html)
* Persistent (disk-based) [TDB](https://jena.apache.org/documentation/tdb/index.html)

#### Fuseki
Simply set the properties in AIDA's `pom.xml` as follows
```
  	<properties>
  		<graphStore>fuseki</graphStore>
    	<dataEndpoint>http://localhost:3030/ART/data</dataEndpoint>
    	<queryEndpoint>http://localhost:3030/ART/sparql</queryEndpoint>
	</properties>
```

#### Non-persistent TDB
Simply set the properties in AIDA's `pom.xml` as follows
```
  	<properties>
  		<graphStore>tdb</graphStore>
	</properties>
```

#### Non-persistent TDB
Simply set the properties in AIDA's `pom.xml` as follows
```
  	<properties>
  		<graphStore>tdb</graphStore>
  		<datasetDir>{PUT_YOUR_PATH_HERE}</datasetDir>
	</properties>
```

### Running
Run AIDA on your localhost by typing
```
mvn clean package tomcat7:run
```

## Usage
Get AIDA up and running. Then, use your favourite REST client against one of our REST APIs.

### HTML UI (work in progress)
Point your good ol' browser to `http://localhost:8080` for some self-explanatory HTML UI.

### Linked API
Point your Linked Data client to `http://localhost:8080/api` and `GET` some RDF like this
```
@prefix actn:  <http://www.dfki.de/resc01/ns/actions#> .
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix dct:   <http://purl.org/dc/terms/1.1/> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix owl:   <http://www.w3.org/2002/07/owl#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix lts:   <http://www.dfki.de/resc01/ns/lts#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .

<http://localhost:8080/api>
        a                ART:DTrack2 , actn:ActionableResource ;
        dct:description  "Linked API for DTrack2 Controller" ;
        ART:deviceState  ART:Unconfigured ;
        actn:action      <http://localhost:8080/api/actions/configure> ;
        lts:model        <http://localhost:8080/api/model> .
```

You can `GET` AIDA's *labelled transition system* from `http://localhost:8080/api/model`:
```
@prefix actn:  <http://www.dfki.de/resc01/ns/actions#> .
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix lts:   <http://www.dfki.de/resc01/ns/lts#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .

_:b0    a            lts:Simple ;
        lts:stateID  ART:Started .

_:b1    a            lts:Initial ;
        lts:body     <http://localhost:8080/api/model/initial> ;
        lts:stateID  ART:Unconfigured .

_:b2    a            lts:Simple ;
        lts:stateID  ART:Configured .

<http://localhost:8080/api>
        a                   lts:StateMachine ;
        lts:contains        _:b1 , _:b2 , _:b0 ;
        lts:contains        [ a           lts:Transition ;
                              lts:label   <http://localhost:8080/api/actions/stopMeasurement> ;
                              lts:source  _:b0 ;
                              lts:target  _:b2
                            ] ;
        lts:contains        [ a           lts:Transition ;
                              lts:label   <http://localhost:8080/api/actions/tearDown> ;
                              lts:source  _:b2 ;
                              lts:target  _:b1
                            ] ;
        lts:contains        [ a           lts:Transition ;
                              lts:label   <http://localhost:8080/api/actions/startMeasurement> ;
                              lts:source  _:b2 ;
                              lts:target  _:b0
                            ] ;
        lts:contains        [ a           lts:Transition ;
                              lts:label   <http://localhost:8080/api/actions/configure> ;
                              lts:source  _:b1 ;
                              lts:target  _:b2
                            ] ;
        lts:stateIndicator  ART:deviceState .
```

You can configure AIDA by `PATCH`ing something like this against `http://localhost:8080/api/actions/configure`
```
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix dtrack: <http://localhost:8080/> .

dtrack:api
  ART:serverHost "192.168.81.110"^^xsd:string ;
  ART:serverPort "50105"^^xsd:int ;
  ART:dataPort "5000"^^xsd:int .
```

Once configured, AIDA will provide more affordances 
```
@prefix actn:  <http://www.dfki.de/resc01/ns/actions#> .
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix dct:   <http://purl.org/dc/terms/1.1/> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix owl:   <http://www.w3.org/2002/07/owl#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix lts:   <http://www.dfki.de/resc01/ns/lts#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .

<http://localhost:8080/api>
        a                actn:ActionableResource , ART:DTrack2 ;
        dct:description  "Linked API for DTrack2 Controller" ;
        ART:dataPort     "5000"^^xsd:int ;
        ART:deviceState  ART:Configured ;
        ART:serverHost   "192.168.81.110" ;
        ART:serverPort   "50105"^^xsd:int ;
        actn:action      <http://localhost:8080/api/actions/tearDown> , <http://localhost:8080/api/actions/startMeasurement> ;
        lts:model        <http://localhost:8080/api/model> .
```

For each of AIDA's affordances, you can always explore the required HTTP methods and RDF data AIDA will consume.
E.g., simply perform a `GET` against `http://localhost:8080/api/actions/startMeasurement`
```
<http://localhost:8080/api/actions/startMeasurement>
        a                ART:StartMeasurementAction , actn:Idempotent , actn:NonSafe ;
        dct:description  "Allows to start measurements using the DTrack2 Controller" ;
        actn:binding     [ a                 http:Request , actn:Binding ;
                           http:headers      [ http:fieldValue  "text/turtle" ;
                                               http:hdrNme      http-headers:accept
                                             ] ;
                           http:httpVersion  "1.1" ;
                           http:mthd         http-methods:POST ;
                           http:requestURI   <http://localhost:8080/api/actions/startMeasurement>
                         ] ;
        actn:consumes    [ dct:description "No input required!"
                           a         sp:Ask ;
                           sp:where  ()
                         ] ;
        actn:produces    [ a         sp:Ask ;
                           sp:text   "PREFIX dtrack: <http://localhost:8080/>
                                      PREFIX actions: <http://localhost:8080/api/actions/>
                                      ...
                                      ASK {
                                        dtrack:api ART:deviceState ART:Started ;
                                        SPATIAL:coordinateSystem   <http://localhost:8080/api/coordinateSystems> ;
                                        SPATIAL:spatialRelationship  <http://localhost:8080/api/targets> ;
                                        actn:action actions:stopMeasurement .
                                        FILTER NOT EXISTS { dtrack:api actn:action actions:tearDown , actions:startMeasurement . } }" ;                            
                           sp:where  ( _:b1 _:b2 _:b3 _:b0 _:b4 )
                         ] .
```
This tells your Linked Data agent, *what* will happen when executing `http://localhost:8080/api/actions/startMeasurement`, and of course how to this affordance. 

## Contributing
Contributions are very welcome.

## License
This source distribution is subject to the license terms in the LICENSE file found in the top-level directory of this distribution.
You may not use this file except in compliance with the License.

## Third-party Contents
This source distribution includes the third-party items with respective licenses as listed in the THIRD-PARTY file found in the top-level directory of this distribution.