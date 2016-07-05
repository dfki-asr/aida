/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.aida.vocabularies.SPATIAL;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.Action;
import de.dfki.resc28.sodalite.actions.IAction;

public class StartMeasurementAction extends Action implements IAction  
{

	public StartMeasurementAction(String actionURI, IGraphStore graphStore) 
	{
		super(actionURI, graphStore);
		this.fRDFType = ART.StartMeasurementAction;
	}

	public Set<String> getAllowedMethods() 
	{
		Set<String> allow = super.getAllowedMethods();
		allow.add(HttpMethod.POST);
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{
		DTrackSDK.getInstance().startMeasurement();
		
		Model currentState = fGraphStore.getDefaultGraph();
		currentState.setNsPrefixes(SPATIAL.NAMESPACE);
		
		Resource tracker = currentState.listSubjectsWithProperty(RDF.type, ART.DTrack2).next().asResource();
		
		// create containers for targets and coordinateSystems
		Model targetContainerModel = ModelFactory.createDefaultModel();
		Resource targetContainer = targetContainerModel.createResource("http://localhost:8080/api/targets");
		targetContainerModel.add(targetContainer, RDF.type, ART.TargetContainer);
		currentState.add(tracker, SPATIAL.spatialRelationship, targetContainer);
		fGraphStore.createNamedGraph(targetContainer.getURI().toString(), targetContainerModel);

		Model coordinateSystemContainerModel = ModelFactory.createDefaultModel();
		Resource coordinateSystemContainer = coordinateSystemContainerModel.createResource("http://localhost:8080/api/coordinateSystems");
		coordinateSystemContainerModel.add(coordinateSystemContainer, RDF.type, ART.CoordinateSystemContainer);
		currentState.add(tracker, SPATIAL.coordinateSystem, coordinateSystemContainer);
		fGraphStore.createNamedGraph(coordinateSystemContainer.getURI().toString(), coordinateSystemContainerModel);	
		
		return currentState;
	}
}
