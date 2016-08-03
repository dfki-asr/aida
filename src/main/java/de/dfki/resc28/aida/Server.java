/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida;

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
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;
import de.dfki.resc28.igraphstore.jena.TDBGraphStore;


/**
 * @author resc01
 *
 */
@ApplicationPath("/")
public class Server extends Application
{
	public static IGraphStore fGraphStore = null;

	@Override
    public Set<Object> getSingletons()
    {
		configure();
		DTrackActionProvider bla = new DTrackActionProvider(fGraphStore);
                DebugService debugSvc = new DebugService();
		return new HashSet<Object>(Arrays.asList(bla, debugSvc));
    }

	public static synchronized void configure()
	{
            if (fGraphStore != null)
                return;

		try
		{

			String aidaConfigFile = System.getProperty("aida.configuration");
			java.io.InputStream is;
			if (aidaConfigFile != null)
			{
                            is = new java.io.FileInputStream(aidaConfigFile);
                            System.out.format("Loading AIDA configuration from %s ...%n", aidaConfigFile);
                        }
                        else
                        {
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

			if (storage.equals("fuseki"))
			{
                                String dataEndpoint = p.getProperty("dataEndpoint");
                                String queryEndpoint = p.getProperty("queryEndpoint");
                                System.out.format("Use Fuseki backend: dataEndpoint=%s queryEndpoint=%s ...%n", dataEndpoint, queryEndpoint);

				Server.fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
			}
			else if (storage.equals("tdb"))
			{
                                System.out.format("Use TDB backend: datasetDir=%s ...%n", p.getProperty("datasetDir"));

				if (p.containsKey("datasetDir"))
					Server.fGraphStore = new TDBGraphStore(p.getProperty("datasetDir"));
				else
					Server.fGraphStore = new TDBGraphStore();
			}
                        initGraphStore(baseURI);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized void initGraphStore(String baseURI)
	{
		System.out.println("AIDA: Initializing Graph Storage ...");
		Server.fGraphStore.clearDefaultGraph();

		Model machineModel = ModelFactory.createDefaultModel();
		RDFDataMgr.read(machineModel, Server.class.getClassLoader().getResourceAsStream("model.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/model", machineModel);

		Model initialState = ModelFactory.createDefaultModel();
		RDFDataMgr.read(initialState, Server.class.getClassLoader().getResourceAsStream("init.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/model/initial", initialState);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api", initialState);

		Model configureAction = ModelFactory.createDefaultModel();
		RDFDataMgr.read(configureAction, Server.class.getClassLoader().getResourceAsStream("configure.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/actions/configure", configureAction);

		Model startMeasurementAction = ModelFactory.createDefaultModel();
		RDFDataMgr.read(startMeasurementAction, Server.class.getClassLoader().getResourceAsStream("startMeasurement.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/actions/startMeasurement", startMeasurementAction);

		Model stopMeasurementAction = ModelFactory.createDefaultModel();
		RDFDataMgr.read(stopMeasurementAction, Server.class.getClassLoader().getResourceAsStream("stopMeasurement.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/actions/stopMeasurement", stopMeasurementAction);

		Model tearDownAction = ModelFactory.createDefaultModel();
		RDFDataMgr.read(tearDownAction, Server.class.getClassLoader().getResourceAsStream("tearDown.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph(baseURI+"/api/actions/tearDown", tearDownAction);
                System.out.println("AIDA: Graph Storage Initialized");
	}
}
