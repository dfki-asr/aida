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
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        int outputChannel = consumable.listObjectsOfProperty(ART.outputChannel).next().asLiteral().getInt();

		// do your domain-specific stuff here
        DTrackSDK dtrack = Server.getDTrack(serverHost, serverPort, dataPort);
        String channel = System.getProperty("aida.dtrack.channel");
        if (outputChannel <= 0 && channel != null) {
            try {
                outputChannel = Integer.parseInt(channel);
            } catch (NumberFormatException ex) {
                System.err.format("AIDA: Invalid aida.dtrack.channel property: %s%n", channel);
            }
        }
        if (outputChannel <= 0) {
            outputChannel = dtrack.findInactiveChannel(DTrackSDK.NUM_DTRACK2_OUTPUT_CHANNELS);
            System.out.format("AIDA: Missing or invalid aida.dtrack.channel property, will use first inactive channel %d%n", outputChannel);
        }
        System.out.format("AIDA: Use DTrack channel: %d%n", outputChannel);
        String localhost = System.getProperty("aida.dtrack.localhost");
        if (localhost == null) {
            try {
                localhost = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                System.out.format("AIDA: Could not get host address: %s%n", ex);
            }
        }
        if (localhost != null) {
            dtrack.configureUDPChannel(outputChannel, localhost, dataPort);
            dtrack.activateChannel(outputChannel, "all");
            System.out.format("AIDA: Configured output channel %d for host %s%n", outputChannel, localhost);
        } else {
            System.out.println("AIDA: Could not configure output channel, specify aida.dtrack.localhost property");
        }
        dtrack.stopMeasurement();
        currentState.add(tracker, ART.serverHost, currentState.createTypedLiteral(serverHost));
        currentState.add(tracker, ART.serverPort, currentState.createTypedLiteral(serverPort));
        currentState.add(tracker, ART.dataPort, currentState.createTypedLiteral(dataPort));
        currentState.add(tracker, ART.outputChannel, currentState.createTypedLiteral(outputChannel));

        // hand-over modified state to updateState() in super
        return currentState;
    }
}
