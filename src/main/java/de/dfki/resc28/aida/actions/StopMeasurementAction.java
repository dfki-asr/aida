/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.art4j.DTrackSDK;
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
		Model currentState = fGraphStore.getDefaultGraph();
		Resource tracker = currentState.listSubjectsWithProperty(RDF.type, ART.DTrack2).next().asResource();
		
		DTrackSDK.getInstance().stopMeasurement();
		
		Resource targetContainer = (Resource)currentState.listObjectsOfProperty(SPATIAL.spatialRelationship).next();
		Resource coordinateSystemContainer = (Resource)currentState.listObjectsOfProperty(SPATIAL.coordinateSystem).next();
		currentState.remove(tracker, SPATIAL.spatialRelationship, targetContainer);
		currentState.remove(tracker, SPATIAL.coordinateSystem, coordinateSystemContainer);
		
		fGraphStore.deleteNamedGraph(targetContainer.getURI().toString());
		fGraphStore.deleteNamedGraph(coordinateSystemContainer.getURI().toString());
		
		return currentState;
	}
}
