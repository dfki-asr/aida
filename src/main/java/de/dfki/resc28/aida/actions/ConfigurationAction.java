/**
 * 
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

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
		allow.add("PATCH");
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{
		Model currentState = fGraphStore.getDefaultGraph();
		Resource tracker = currentState.listSubjectsWithProperty(RDF.type, ART.DTrack2).next().asResource();
		
		// get your inputs from consumable
		String serverHost = consumable.listObjectsOfProperty(ART.serverHost).next().asLiteral().getString();
		int serverPort = consumable.listObjectsOfProperty(ART.serverPort).next().asLiteral().getInt();
		int dataPort = consumable.listObjectsOfProperty(ART.dataPort).next().asLiteral().getInt();

		// do your domain-specific stuff here
		DTrackSDK.getInstance(serverHost, serverPort, dataPort);		
		DTrackSDK.getInstance().stopMeasurement();
		currentState.add(tracker, ART.serverHost, currentState.createTypedLiteral(serverHost));
		currentState.add(tracker, ART.serverPort, currentState.createTypedLiteral(serverPort));
		currentState.add(tracker, ART.dataPort, currentState.createTypedLiteral(dataPort));

		// hand-over modified state to updateState() in super
		return currentState;
	}
}
