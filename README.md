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
Don't forget to set `Content-Type` and `Accept` headers to one of the following:

* text/turtle
* text/n3 
* * text/trig 
* application/x-turtle
* application/ld+json 
* application/n-quads 
* application/n-triples 
* application/rdf+json 
* application/rdf+xml 
* application/trix

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
This tells your Linked Data agent, *what* will happen when executing `http://localhost:8080/api/actions/startMeasurement`, and *how* to this affordance. 

Once started, a `GET` on `http://localhost:8080/api` gives you 
```
@prefix SPATIAL: <http://vocab.arvida.de/ns/spatial#> .
@prefix actn:  <http://www.dfki.de/resc01/ns/actions#> .
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix dct:   <http://purl.org/dc/terms/1.1/> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix owl:   <http://www.w3.org/2002/07/owl#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix lts:   <http://www.dfki.de/resc01/ns/lts#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .

<http://localhost:8080/api>
        a                            actn:ActionableResource , ART:DTrack2 ;
        dct:description              "Linked API for DTrack2 Controller" ;
        SPATIAL:coordinateSystem     <http://localhost:8080/api/coordinateSystems> ;
        SPATIAL:spatialRelationship  <http://localhost:8080/api/targets> ;
        ART:dataPort                 "5000"^^xsd:int ;
        ART:deviceState              ART:Started ;
        ART:serverHost               "192.168.81.110" ;
        ART:serverPort               "50105"^^xsd:int ;
        actn:action                  <http://localhost:8080/api/actions/stopMeasurement> ;
        lts:model                    <http://localhost:8080/api/model> .
```

and allows to `GET` all tracked targets from `http://localhost:8080/api/targets`
```
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix LDP:   <http://www.w3.org/ns/ldp#> .

<http://localhost:8080/api/targets>
        a             LDP:Container ;
        LDP:contains  <http://localhost:8080/api/targets/0> , 
        	      <http://localhost:8080/api/targets/1> , 
        	      <http://localhost:8080/api/targets/2> .
```

Choose your favourite target and `GET` its tracking data from `http://localhost:8080/api/targets/0`
```
@prefix SPATIAL: <http://vocab.arvida.de/ns/spatial#> .
@prefix MATHS: <http://vocab.arvida.de/ns/maths#> .
@prefix ART:   <http://www.ar-tracking.com/ns#> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix VOM:   <http://vocab.arvida.de/ns/vom#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .

<http://localhost:8080/api/targets/0>
        a                            ART:Target , ART:TreeTarget ;
        SPATIAL:spatialRelationship  [ a                               SPATIAL:SpatialRelationship ;
                                       SPATIAL:rotation                [ a                  SPATIAL:Rotation3D ;
                                                                         VOM:quantityValue  [ a          MATHS:Matrix3D ;
                                                                                              MATHS:a11  "0.554293"^^xsd:double ;
                                                                                              MATHS:a12  "-0.831728"^^xsd:double ;
                                                                                              MATHS:a13  "0.031426"^^xsd:double ;
                                                                                              MATHS:a21  "0.616607"^^xsd:double ;
                                                                                              MATHS:a22  "0.435704"^^xsd:double ;
                                                                                              MATHS:a23  "0.655712"^^xsd:double ;
                                                                                              MATHS:a31  "-0.559067"^^xsd:double ;
                                                                                              MATHS:a32  "-0.344079"^^xsd:double ;
                                                                                              MATHS:a33  "0.754357"^^xsd:double
                                                                                            ]
                                                                       ] ;
                                       SPATIAL:sourceCoordinateSystem  [ a  MATHS:CoordinateSystem ] ;
                                       SPATIAL:targetCoordinateSystem  [ a  MATHS:CoordinateSystem ] ;
                                       SPATIAL:translation             [ a                  SPATIAL:Translation3D ;
                                                                         VOM:quantityValue  [ a        MATHS:Vector3D ;
                                                                                              MATHS:x  "165.987"^^xsd:double ;
                                                                                              MATHS:y  "318.763"^^xsd:double ;
                                                                                              MATHS:z  "-15.187"^^xsd:double
                                                                                            ]
                                                                       ]
                                     ] ;
        ART:bodyID                   "0"^^xsd:int .
```

By setting your client's `Accept` header to, let's say, `application/ld+json`, you get your tracking data in JSON:
```
{
    "@graph": [
        {
            "@id": "_:b0",
            "@type": "SPATIAL:SpatialRelationship",
            "rotation": "_:b1",
            "sourceCoordinateSystem": "_:b4",
            "targetCoordinateSystem": "_:b3",
            "translation": "_:b2"
        },
        {
            "@id": "_:b1",
            "@type": "SPATIAL:Rotation3D",
            "quantityValue": "_:b5"
        },
        {
            "@id": "_:b2",
            "@type": "SPATIAL:Translation3D",
            "quantityValue": "_:b6"
        },
        {
            "@id": "_:b3",
            "@type": "MATHS:CoordinateSystem"
        },
        {
            "@id": "_:b4",
            "@type": "MATHS:CoordinateSystem"
        },
        {
            "@id": "_:b5",
            "@type": "MATHS:Matrix3D",
            "MATHS:a11": 0.554469,
            "MATHS:a12": -0.831607,
            "MATHS:a13": 0.03152,
            "MATHS:a21": 0.616407,
            "MATHS:a22": 0.435843,
            "MATHS:a23": 0.655807,
            "MATHS:a31": -0.559111,
            "MATHS:a32": -0.344196,
            "MATHS:a33": 0.75427
        },
        {
            "@id": "_:b6",
            "@type": "MATHS:Vector3D",
            "MATHS:x": 166.068,
            "MATHS:y": 318.686,
            "MATHS:z": -14.814
        },
        {
            "@id": "http://localhost:8080/api/targets/0",
            "@type": [
                "ART:Target",
                "ART:TreeTarget"
            ],
            "spatialRelationship": "_:b0",
            "bodyID": "0"
        }
    ],
    "@context": {
        "rotation": {
            "@id": "http://vocab.arvida.de/ns/spatial#rotation",
            "@type": "@id"
        },
        "translation": {
            "@id": "http://vocab.arvida.de/ns/spatial#translation",
            "@type": "@id"
        },
        "targetCoordinateSystem": {
            "@id": "http://vocab.arvida.de/ns/spatial#targetCoordinateSystem",
            "@type": "@id"
        },
        "sourceCoordinateSystem": {
            "@id": "http://vocab.arvida.de/ns/spatial#sourceCoordinateSystem",
            "@type": "@id"
        },
        "a32": {
            "@id": "http://vocab.arvida.de/ns/maths#a32",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a33": {
            "@id": "http://vocab.arvida.de/ns/maths#a33",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a11": {
            "@id": "http://vocab.arvida.de/ns/maths#a11",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a21": {
            "@id": "http://vocab.arvida.de/ns/maths#a21",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a31": {
            "@id": "http://vocab.arvida.de/ns/maths#a31",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a22": {
            "@id": "http://vocab.arvida.de/ns/maths#a22",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a13": {
            "@id": "http://vocab.arvida.de/ns/maths#a13",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a23": {
            "@id": "http://vocab.arvida.de/ns/maths#a23",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "a12": {
            "@id": "http://vocab.arvida.de/ns/maths#a12",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "z": {
            "@id": "http://vocab.arvida.de/ns/maths#z",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "y": {
            "@id": "http://vocab.arvida.de/ns/maths#y",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "x": {
            "@id": "http://vocab.arvida.de/ns/maths#x",
            "@type": "http://www.w3.org/2001/XMLSchema#double"
        },
        "spatialRelationship": {
            "@id": "http://vocab.arvida.de/ns/spatial#spatialRelationship",
            "@type": "@id"
        },
        "bodyID": {
            "@id": "http://www.ar-tracking.com/ns#bodyID",
            "@type": "http://www.w3.org/2001/XMLSchema#int"
        },
        "quantityValue": {
            "@id": "http://vocab.arvida.de/ns/vom#quantityValue",
            "@type": "@id"
        },
        "MATHS": "http://vocab.arvida.de/ns/maths#",
        "SPATIAL": "http://vocab.arvida.de/ns/spatial#",
        "ART": "http://www.ar-tracking.com/ns#",
        "VOM": "http://vocab.arvida.de/ns/vom#",
        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
        "xsd": "http://www.w3.org/2001/XMLSchema#"
    }
}
```

Happy tracking!

### HTML UI (work in progress)
Point your good ol' browser to `http://localhost:8080` for some self-explanatory HTML UI.

## Contributing
Contributions are very welcome.

## License
This source distribution is subject to the license terms in the LICENSE file found in the top-level directory of this distribution.
You may not use this file except in compliance with the License.

## Third-party Contents
This source distribution includes the third-party items with respective licenses as listed in the THIRD-PARTY file found in the top-level directory of this distribution.
