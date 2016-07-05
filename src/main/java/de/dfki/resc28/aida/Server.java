/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.aida.services.DTrackActionProvider;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;


/**
 * @author resc01
 *
 */
@ApplicationPath("/")
public class Server extends Application 
{
	public static String dataEndpoint = "http://localhost:3030/ART/data";
	public static String queryEndpoint = "http://localhost:3030/ART/sparql";
	
	
	@Override
    public Set<Object> getSingletons() 
    {
		initGraphStore();
		
		DTrackActionProvider bla = new DTrackActionProvider(new FusekiGraphStore(dataEndpoint, queryEndpoint));
		return new HashSet<Object>(Arrays.asList(bla));
    }
	
	public void initGraphStore()
	{
		FusekiGraphStore fGraphStore = new FusekiGraphStore(dataEndpoint, queryEndpoint);
		fGraphStore.clearDefaultGraph();
		
		Model machineModel = ModelFactory.createDefaultModel();
		RDFDataMgr.read(machineModel, getClass().getClassLoader().getResourceAsStream("model.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/model", machineModel);
		
		Model initialState = ModelFactory.createDefaultModel();
		RDFDataMgr.read(initialState, getClass().getClassLoader().getResourceAsStream("init.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/model/initial", initialState);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api", initialState);
		
		Model configureAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(configureAction, getClass().getClassLoader().getResourceAsStream("configure.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/configure", configureAction);
		
		Model startMeasurementAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(startMeasurementAction, getClass().getClassLoader().getResourceAsStream("startMeasurement.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/startMeasurement", startMeasurementAction);
		
		Model stopMeasurementAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(stopMeasurementAction, getClass().getClassLoader().getResourceAsStream("stopMeasurement.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/stopMeasurement", stopMeasurementAction);
		
		Model tearDownAction = ModelFactory.createDefaultModel(); 
		RDFDataMgr.read(tearDownAction, getClass().getClassLoader().getResourceAsStream("tearDown.ttl"), Lang.TURTLE);
		fGraphStore.replaceNamedGraph("http://localhost:8080/api/actions/tearDown", tearDownAction);
	}

}
