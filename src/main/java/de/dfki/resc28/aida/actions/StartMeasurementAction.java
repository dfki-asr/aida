package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.sodalite.vocabularies.ACTN;
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
		Model trackerModel = fGraphStore.getNamedGraph("http://localhost:8080/api/model");
		Resource tracker = trackerModel.listSubjectsWithProperty(RDF.type, ART.DTrack2).next();
		Resource startMeasurementAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.StartMeasurementAction).next();
		Resource stopMeasurementAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.StopMeasurementAction).next();
		
		// create containers for targets and coordinateSystems
		Model targetContainerModel = ModelFactory.createDefaultModel();
		Resource targetContainer = targetContainerModel.createResource("http://localhost:8080/api/targets");
		targetContainerModel.add(targetContainer, RDF.type, ART.TargetContainer);
		fGraphStore.createNamedGraph("http://localhost:8080/api/targets", targetContainerModel);
		
		Model coordinateSystemContainerModel = ModelFactory.createDefaultModel();
		Resource coordinateSystemContainer = coordinateSystemContainerModel.createResource("http://localhost:8080/api/coordinateSystems");
		coordinateSystemContainerModel.add(coordinateSystemContainer, RDF.type, ART.CoordinateSystemContainer);
		fGraphStore.createNamedGraph("http://localhost:8080/api/coordinateSystems", coordinateSystemContainerModel);

		// update the device state
		Model trackerState = fGraphStore.getDefaultGraph();
		trackerState.add(tracker, ACTN.action, stopMeasurementAction);
		trackerState.remove(tracker, ACTN.action, startMeasurementAction);
		trackerState.add(tracker, ART.deviceState, trackerState.createTypedLiteral("started"));
		trackerState.remove(tracker, ART.deviceState, trackerState.createTypedLiteral("configured"));
		trackerState.add(tracker, SPATIAL.spatialRelationship, targetContainer);
		trackerState.add(tracker, SPATIAL.coordinateSystem, coordinateSystemContainer);
		fGraphStore.replaceDefaultGraph(trackerState);
		
		return trackerState;
	}
}
