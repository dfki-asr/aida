/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.services;

import javax.ws.rs.Path;

import de.dfki.resc28.aida.actions.DTrackActionController;
import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.sodalite.actions.ActionController;
import de.dfki.resc28.sodalite.services.ActionService;



@Path("actions/{actionID: .+}")
public class DTrackActionProvider extends ActionService 
{

	public DTrackActionProvider(IGraphStore graphStore) 
	{
		super(graphStore);
	}
	
	protected ActionController getActionController() 
	{
		return new DTrackActionController(this.fGraphStore);
	}
	
}
