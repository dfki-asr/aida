package de.dfki.resc28.aida.actions;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.Action;
import de.dfki.resc28.sodalite.actions.ActionController;
import de.dfki.resc28.sodalite.actions.IAction;
import de.dfki.resc28.sodalite.vocabularies.ACTN;

public class DTrackActionController extends ActionController 
{

	public DTrackActionController(IGraphStore graphStore) 
	{
		super(graphStore);
	}

	public IAction get(String actionURI)
	{
		Model actionModel = fGraphStore.getNamedGraph(actionURI);
		
		if (actionModel == null)
			return null;
		
		Resource action = actionModel.getResource(actionURI);

		if (action.hasProperty(RDF.type, ART.ConfigurationAction))
		{
			return new ConfigurationAction(actionURI, fGraphStore);
		}
		else if (action.hasProperty(RDF.type, ART.StopMeasurementAction))
		{
			return new StopMeasurementAction(actionURI, fGraphStore);
		}
		else if (action.hasProperty(RDF.type, ART.StartMeasurementAction))
		{
			return new StartMeasurementAction(actionURI, fGraphStore);
		}
		else if (action.hasProperty(RDF.type, ACTN.Action))
		{
			return new Action(actionURI, fGraphStore);
		}
		else
		{
			return null;
		}
	}
}
