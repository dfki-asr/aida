package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.sodalite.vocabularies.ACTN;
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
		allow.add(HttpMethod.GET);
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{
		DTrackSDK.getInstance().startMeasurement();
		return consumable;
	}

	public Model updateState(Model consumable) 
	{
		// update the device state
		Model trackerModel = fGraphStore.getNamedGraph("http://localhost:8080/api/model");
		Resource tracker = trackerModel.listSubjectsWithProperty(RDF.type, ART.DTrack2).next();
		Resource startMeasurementAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.StartMeasurementAction).next();
		Resource stopMeasurementAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.StopMeasurementAction).next();
		Resource targetContainer = (Resource)trackerModel.listObjectsOfProperty(SPATIAL.spatialRelationship).next();
		Resource coordinateSystemContainer = (Resource)trackerModel.listObjectsOfProperty(SPATIAL.coordinateSystem).next();
		
		Model trackerState = fGraphStore.getDefaultGraph();
		trackerState.add(tracker, ACTN.action, startMeasurementAction);
		trackerState.remove(tracker, ACTN.action, stopMeasurementAction);
		trackerState.add(tracker, ART.deviceState, trackerState.createTypedLiteral("configured"));
		trackerState.remove(tracker, ART.deviceState, trackerState.createTypedLiteral("started"));
		trackerState.remove(tracker, SPATIAL.spatialRelationship, targetContainer);
		trackerState.remove(tracker, SPATIAL.coordinateSystem, coordinateSystemContainer);
		
		fGraphStore.deleteNamedGraph("http://localhost:8080/api/coordinateSystems");
		fGraphStore.deleteNamedGraph("http://localhost:8080/api/targets");
		fGraphStore.replaceDefaultGraph(trackerState);
		
		return trackerState;
	}
}
