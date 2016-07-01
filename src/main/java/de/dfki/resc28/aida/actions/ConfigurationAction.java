/**
 * 
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.sodalite.vocabularies.ACTN;
import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.Action;
import de.dfki.resc28.sodalite.actions.IAction;

/**
 * @author resc01
 *
 */

public class ConfigurationAction extends Action implements IAction 
{
	public ConfigurationAction(String actionURI, IGraphStore graphStore) 
	{
		super(actionURI, graphStore);
		this.fRDFType = ART.ConfigurationAction;
	}

	public Set<String> getAllowedMethods() 
	{
		Set<String> allow = super.getAllowedMethods();
		allow.add(HttpMethod.POST);
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{
		// get your inputs from consumable
		String serverHost = consumable.listObjectsOfProperty(ART.serverHost).next().asLiteral().getString();
		int serverPort = consumable.listObjectsOfProperty(ART.serverPort).next().asLiteral().getInt();
		int dataPort = consumable.listObjectsOfProperty(ART.dataPort).next().asLiteral().getInt();

		// do your domain-specific stuff here
		DTrackSDK.getInstance(serverHost, serverPort, dataPort);		
		DTrackSDK.getInstance().stopMeasurement();

		return consumable;
	}

	public Model updateState(Model consumable) 
	{
		// TODO: how to access the URI space in a better way?
		Model trackerModel = fGraphStore.getNamedGraph("http://localhost:8080/api/model");
		Resource tracker = trackerModel.listSubjectsWithProperty(RDF.type, ART.DTrack2).next();
		Resource configurationAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.ConfigurationAction).next();
		Resource startMeasurementAction = trackerModel.listSubjectsWithProperty(RDF.type, ART.StartMeasurementAction).next();
		
		// update tracker state in default graph
		String serverHost = consumable.listObjectsOfProperty(ART.serverHost).next().asLiteral().getString();
		int serverPort = consumable.listObjectsOfProperty(ART.serverPort).next().asLiteral().getInt();
		int dataPort = consumable.listObjectsOfProperty(ART.dataPort).next().asLiteral().getInt();
		
		Model trackerState = fGraphStore.getDefaultGraph();
		trackerState.add(tracker, ACTN.action, startMeasurementAction);
		trackerState.add(tracker, ART.serverHost, trackerState.createTypedLiteral(serverHost));
		trackerState.add(tracker, ART.serverPort, trackerState.createTypedLiteral(serverPort));
		trackerState.add(tracker, ART.dataPort, trackerState.createTypedLiteral(dataPort));
		trackerState.add(tracker, ART.deviceState, trackerState.createTypedLiteral("configured"));
		trackerState.remove(tracker, ACTN.action, configurationAction);
		trackerState.remove(tracker, ART.deviceState, trackerState.createTypedLiteral("unconfigured"));
		fGraphStore.replaceDefaultGraph(trackerState);
		
		return trackerState;
	}
}
