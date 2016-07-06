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
	public static IGraphStore fGraphStore;
	
	@Override
    public Set<Object> getSingletons() 
    {
		configure();
		initGraphStore();
		DTrackActionProvider bla = new DTrackActionProvider(fGraphStore);
		return new HashSet<Object>(Arrays.asList(bla));
    }
	
	public void configure()
	{
		try
		{
			java.io.InputStream is = Server.class.getClassLoader().getResourceAsStream("aida.properties");
			java.util.Properties p = new Properties();
			p.load(is);
		
			String storage = p.getProperty("graphStore");
			
			if (storage.equals("fuseki"))
			{
				Server.fGraphStore = new FusekiGraphStore(p.getProperty("dataEndpoint"), p.getProperty("queryEndpoint"));
			}
			else if (storage.equals("tdb"))
			{
				if (p.containsKey("datasetDir"))
					Server.fGraphStore = new TDBGraphStore(p.getProperty("datasetDir"));
				else
					Server.fGraphStore = new TDBGraphStore();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void initGraphStore()
	{
		Server.fGraphStore.clearDefaultGraph();
		
		Model machineModel = ModelFactory.createDefaultModel();
		RDFDataMgr.read(machineModel, getClass().getClassLoader().getResourceAsStream("model.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/model", machineModel);
		
		Model initialState = ModelFactory.createDefaultModel();
		RDFDataMgr.read(initialState, getClass().getClassLoader().getResourceAsStream("init.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/model/initial", initialState);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api", initialState);
		
		Model configureAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(configureAction, getClass().getClassLoader().getResourceAsStream("configure.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/configure", configureAction);
		
		Model startMeasurementAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(startMeasurementAction, getClass().getClassLoader().getResourceAsStream("startMeasurement.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/startMeasurement", startMeasurementAction);
		
		Model stopMeasurementAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(stopMeasurementAction, getClass().getClassLoader().getResourceAsStream("stopMeasurement.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/stopMeasurement", stopMeasurementAction);
		
		Model tearDownAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(tearDownAction, getClass().getClassLoader().getResourceAsStream("tearDown.ttl"), Lang.TURTLE);
		Server.fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/tearDown", tearDownAction);
	}
}