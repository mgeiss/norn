/**
 * Copyright 2012 Markus Geiss
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.mgeiss.norn;

import java.rmi.*;
import java.rmi.registry.Registry;

/**
 * <code>NornNode</code> is a reference to a node that provides methods for
 * storing and retrieving remote object references bound with arbitrary string
 * names. The
 * <code>bind</code>,
 * <code>unbind</code>, and
 * <code>rebind</code> methods are used to alter the name bindings in the
 * registry, and the
 * <code>lookup</code> and
 * <code>list</code> methods are used to query the current name bindings.
 *
 * <code>start</code> is used to start the multicast listener thread and
 * <code>stop</code> will stop it.
 *
 * <p>The names used for bindings in a
 * <code>Registry</code> are pure strings, not parsed. A service which stores
 * its remote reference in a
 * <code>NornNode</code> may wish to use a package name as a prefix in the name
 * binding to reduce the likelihood of name collisions in the registry.
 *
 * <p>Note that
 * <code>NornNode</code> uses a remote object registry to delegate all remote
 * object handling.
 *
 * @author Markus Geiss
 * @version 1.0
 * @see java.rmi.registry.Registry
 */
public class NornNode {

    private Registry registry;
    private NornNodeInfo nodeInfo;
    private NornNodeThread nodeThread;

    /**
     * Trusted package private constructor.
     *
     * @param nodeInfo node information for this node.
     * @param registry the remote object registry to be used.
     */
    NornNode(NornNodeInfo nodeInfo, Registry registry) {
        this(nodeInfo, registry, null);
    }

    /**
     * Trusted package private constructor.
     *
     * @param nodeInfo node information for this node.
     * @param registry the remote object registry to be used.
     * @param nodeThread the multicast listener thread that accepts all
     * requests.
     */
    NornNode(NornNodeInfo nodeInfo, Registry registry, NornNodeThread nodeThread) {
        super();
        this.nodeInfo = nodeInfo;
        this.registry = registry;
        this.nodeThread = nodeThread;
    }

    /**
     * Binds a remote reference to the specified
     * <code>name</code> in this node.
     *
     * @param name the name to associate with the remote reference
     * @param obj a reference to a remote object
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws AccessException
     */
    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
        this.registry.bind(name, obj);
    }

    /**
     * Removes the binding for the specified
     * <code>name</code> in this node.
     *
     * @param name the name to associate with the remote reference
     * @throws RemoteException
     * @throws NotBoundException
     * @throws AccessException
     */
    public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
        this.registry.unbind(name);
    }

    /**
     * Replaces the binding for the specified
     * <code>name</code> in this node with the supplied remote reference. If
     * there is an existing binding for the specified
     * <code>name</code>, it is discarded.
     *
     * @param name the name to associate with the remote reference
     * @param obj a reference to a remote object
     * @throws RemoteException
     * @throws AccessException
     */
    public void rebind(String name, Remote obj) throws RemoteException, AccessException {
        this.registry.rebind(name, obj);
    }

    /**
     * Returns the remote reference bound to the specified
     * <code>name</code> in this node.
     *
     * @param name the name to associate with the remote reference
     * @return a reference to a remote object
     * @throws RemoteException
     * @throws NotBoundException
     * @throws AccessException
     */
    public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
        return this.registry.lookup(name);
    }

    /**
     * Returns an array of the names bound in this node. The array will contain
     * a snapshot of the names bound in this node at the time of the given
     * invocation of this method.
     *
     * @return an array of the names bound in this registry
     * @throws RemoteException
     * @throws AccessException
     */
    public String[] list() throws RemoteException, AccessException {
        return this.registry.list();
    }

    /**
     * Retunrs a reference to this nodes information containing the multicast
     * address, the mulitcast port, the regesitry address, the registry port and
     * the calculated JVM load.
     *
     * @return a reference to this nodes information
     */
    public NornNodeInfo getNodeInfo() {
        return nodeInfo;
    }

    /**
     * Start the listening thread to accept requests.
     */
    void start() {
        if (this.nodeThread != null) {
            this.nodeThread.start();
        }
    }

    /**
     * Stop the listening thread.
     */
    public void stop() {
        if (this.nodeThread != null) {
            this.nodeThread.interrupt();
        }
    }
}
