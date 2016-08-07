/*
 * This file is part of aida. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.aida.actions;

import de.dfki.resc28.aida.Server;
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
public class ConfigurationAction extends Action implements IAction {

    public ConfigurationAction(String actionURI, IGraphStore graphStore) {
        super(actionURI, graphStore);
        this.fRDFType = ART.ConfigurationAction;
    }

    @Override
    public Model performTasks(Model consumable) {
        Model currentState = fGraphStore.getDefaultGraph();
        Resource tracker = currentState.listSubjectsWithProperty(RDF.type, ART.DTrack2).next().asResource();

        // get your inputs from consumable
        String serverHost = consumable.listObjectsOfProperty(ART.serverHost).next().asLiteral().getString();
        int serverPort = consumable.listObjectsOfProperty(ART.serverPort).next().asLiteral().getInt();
        int dataPort = consumable.listObjectsOfProperty(ART.dataPort).next().asLiteral().getInt();

		// do your domain-specific stuff here
        DTrackSDK dtrack = Server.getDTrack(serverHost, serverPort, dataPort);
        String channel = System.getProperty("aida.dtrack.channel");
        int channelNum = -1;
        if (channel != null) {
            try {
                channelNum = Integer.parseInt(channel);
            } catch (NumberFormatException ex) {
                System.err.format("AIDA: Invalid aida.dtrack.channel property: %s%n", channel);
            }
        }
        if (channelNum <= 0) {
            channelNum = dtrack.findInactiveChannel(-1);
            System.out.format("AIDA: Missing or invalid aida.dtrack.channel property, will use first inactive channel %d%n", channelNum);
        }
        System.out.format("AIDA: Use DTrack channel: %d%n", channelNum);
        String localhost = System.getProperty("aida.dtrack.localhost");
        if (localhost != null) {
            dtrack.configureUDPChannel(channelNum, localhost, dataPort);
            dtrack.activateChannel(channelNum, "all");
        }
        dtrack.stopMeasurement();
        currentState.add(tracker, ART.serverHost, currentState.createTypedLiteral(serverHost));
        currentState.add(tracker, ART.serverPort, currentState.createTypedLiteral(serverPort));
        currentState.add(tracker, ART.dataPort, currentState.createTypedLiteral(dataPort));

        // hand-over modified state to updateState() in super
        return currentState;
    }
}
