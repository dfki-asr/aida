/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.flapjack.vocabularies.LDP;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.Action;
import de.dfki.resc28.sodalite.actions.IAction;

public class StopMeasurementAction extends Action implements IAction 
{
	public StopMeasurementAction(String actionURI, IGraphStore graphStore) 
	{
		super(actionURI, graphStore);
		this.fRDFType = ART.StopMeasurementAction;
	}

	public Set<String> getAllowedMethods() 
	{
		Set<String> allow = super.getAllowedMethods();
		allow.add(HttpMethod.POST);
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{
		DTrackSDK.getInstance().stopMeasurement();
		
		Model currentState = fGraphStore.getDefaultGraph();
		Resource tracker = currentState.listSubjectsWithProperty(RDF.type, ART.DTrack2).next().asResource();
		Resource targetContainer = (Resource)currentState.listObjectsOfProperty(SPATIAL.spatialRelationship).next();
		Resource coordinateSystemContainer = (Resource)currentState.listObjectsOfProperty(SPATIAL.coordinateSystem).next();

		// remove all bodyGraphs from graphstore
		StmtIterator bodyIterator = fGraphStore.getNamedGraph(targetContainer.getURI().toString()).listStatements(null, LDP.contains, (RDFNode) null);
		while (bodyIterator.hasNext())
			fGraphStore.deleteNamedGraph(bodyIterator.nextStatement().asTriple().getObject().getURI().toString());
		fGraphStore.deleteNamedGraph(targetContainer.getURI().toString());
		
		// remove all cordinateSystem graphs from graphstore
		StmtIterator coordinateSystemIterator = fGraphStore.getNamedGraph(coordinateSystemContainer.getURI().toString()).listStatements(null, LDP.contains, (RDFNode) null);
		while (coordinateSystemIterator.hasNext())
			fGraphStore.deleteNamedGraph(coordinateSystemIterator.nextStatement().asTriple().getObject().getURI().toString());
		
		// remove containers from current state and graphstore
		currentState.remove(tracker, SPATIAL.spatialRelationship, targetContainer);
		fGraphStore.deleteNamedGraph(targetContainer.getURI().toString());
		
		currentState.remove(tracker, SPATIAL.coordinateSystem, coordinateSystemContainer); 
		fGraphStore.deleteNamedGraph(coordinateSystemContainer.getURI().toString());
		
		return currentState;
	}
}
