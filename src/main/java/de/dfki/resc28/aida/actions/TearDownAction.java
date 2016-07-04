/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.actions;

import java.util.Set;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import de.dfki.resc28.aida.vocabularies.ART;
import de.dfki.resc28.art4j.DTrackSDK;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.Action;
import de.dfki.resc28.sodalite.actions.IAction;
import de.dfki.resc28.sodalite.vocabularies.ACTN;

public class TearDownAction extends Action implements IAction {

	public TearDownAction(String actionURI, IGraphStore graphStore) 
	{
		super(actionURI, graphStore);
		this.fRDFType = ART.TearDownAction;
	}

	public Set<String> getAllowedMethods() 
	{
		Set<String> allow = super.getAllowedMethods();
		allow.add("PATCH");
	    return allow;
	}

	public Model performTasks(Model consumable) 
	{	
		// TODO: implement tearDown in art4j JNI wrapper!
		// DTrackSDK.getInstance().destroy()
	
		Model currentState = fGraphStore.getDefaultGraph();
		currentState.remove(currentState.listStatements(null, ART.serverHost, (Literal) null));
		currentState.remove(currentState.listStatements(null, ART.serverPort, (Literal) null));
		currentState.remove(currentState.listStatements(null, ART.dataPort, (Literal) null));
		
		// hand-over modified state to updateState() in super
		return currentState;
	}
}
