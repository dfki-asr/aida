/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida;

import com.hubspot.jinjava.Jinjava;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.aida.services.DTrackActionProvider;
import de.dfki.resc28.aida.services.DebugService;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;
import de.dfki.resc28.igraphstore.jena.TDBGraphStore;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author resc01
 *
 */
@ApplicationPath("/")
public class Server extends Application {

    public static IGraphStore fGraphStore = null;
    public static String fBaseURI = null;

    private static DTrackSDK fDTrackSDK = null;

    public static synchronized DTrackSDK getDTrack() {
        if (fDTrackSDK == null || fDTrackSDK.isDestroyed()) {
            fDTrackSDK = new DTrackSDK();
        }

        return fDTrackSDK;
    }

    public static synchronized DTrackSDK getDTrack(String serverHost, int serverPort, int dataPort) {
        if (fDTrackSDK == null || fDTrackSDK.isDestroyed()) {
            fDTrackSDK = new DTrackSDK(serverHost, serverPort, dataPort);
        }

        return fDTrackSDK;
    }

    @Override
    public Set<Object> getSingletons() {
        configure();
        DTrackActionProvider bla = new DTrackActionProvider(fGraphStore);
        DebugService debugSvc = new DebugService();
        return new HashSet<Object>(Arrays.asList(bla, debugSvc));
    }

    public static synchronized void configure() {
        if (fGraphStore != null) {
            return;
        }

        try {
            String aidaConfigFile = System.getProperty("aida.configuration");
            java.io.InputStream is;
            if (aidaConfigFile != null) {
                is = new java.io.FileInputStream(aidaConfigFile);
                System.out.format("Loading AIDA configuration from %s ...%n", aidaConfigFile);
            } else {
                is = Server.class.getClassLoader().getResourceAsStream("aida.properties");
                System.out.println("Loading AIDA configuration from internal resource file ...");
            }
            java.util.Properties p = new Properties();
            p.load(is);

            String storage = p.getProperty("graphStore");
            String baseURI = p.getProperty("baseURI");
            if (baseURI == null) {
                System.out.println("AIDA: baseURI property is null, use hostName property");
                String hostName = p.getProperty("hostName", "localhost");
                baseURI = "http://" + hostName;
            }
            System.out.format("AIDA: baseURI = %s%n", baseURI);
            fBaseURI = baseURI;

            if (storage.equals("fuseki")) {
                String dataEndpoint = p.getProperty("dataEndpoint");
                String queryEndpoint = p.getProperty("queryEndpoint");
                System.out.format("Use Fuseki backend: dataEndpoint=%s queryEndpoint=%s ...%n", dataEndpoint, queryEndpoint);

                Server.fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
            } else if (storage.equals("tdb")) {
                System.out.format("Use TDB backend: datasetDir=%s ...%n", p.getProperty("datasetDir"));

                if (p.containsKey("datasetDir")) {
                    Server.fGraphStore = new TDBGraphStore(p.getProperty("datasetDir"));
                } else {
                    Server.fGraphStore = new TDBGraphStore();
                }
            }
            initGraphStore(baseURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String streamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static StringReader processResource(Jinjava jinjava, Map<String, Object> context, String name) {
        String tmpl = streamToString(Server.class.getClassLoader().getResourceAsStream(name));
        String result = jinjava.render(tmpl, context);
        return new StringReader(result);
    }

    public static synchronized void initGraphStore(String baseURI) {
        System.out.println("AIDA: Initializing Graph Storage ...");

        Jinjava jinjava = new Jinjava();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("baseURI", baseURI);

        Server.fGraphStore.clearDefaultGraph();

        Model machineModel = ModelFactory.createDefaultModel();

        RDFDataMgr.read(machineModel, processResource(jinjava, context, "model.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/model", machineModel);

        Model initialState = ModelFactory.createDefaultModel();
        RDFDataMgr.read(initialState, processResource(jinjava, context, "init.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/model/initial", initialState);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api", initialState);

        Model configureAction = ModelFactory.createDefaultModel();
        RDFDataMgr.read(configureAction, processResource(jinjava, context, "configure.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/actions/configure", configureAction);

        Model startMeasurementAction = ModelFactory.createDefaultModel();
        RDFDataMgr.read(startMeasurementAction, processResource(jinjava, context, "startMeasurement.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/actions/startMeasurement", startMeasurementAction);

        Model stopMeasurementAction = ModelFactory.createDefaultModel();
        RDFDataMgr.read(stopMeasurementAction, processResource(jinjava, context, "stopMeasurement.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/actions/stopMeasurement", stopMeasurementAction);

        Model tearDownAction = ModelFactory.createDefaultModel();
        RDFDataMgr.read(tearDownAction, processResource(jinjava, context, "tearDown.ttl"), baseURI, Lang.TURTLE);
        Server.fGraphStore.replaceNamedGraph(baseURI + "/api/actions/tearDown", tearDownAction);
        System.out.println("AIDA: Graph Storage Initialized");
    }
}
