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
package mage.rmi.norn;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 * @see java.rmi.registry.Registry
 */
public class NornNode {

    /**
     * Default address for multicast (234.5.6.7).
     */
    public static final String MULTICAST_ADDRESS = "234.5.6.7";
    
    /**
     * Default port for multicast (42000).
     */
    public static final int MULTICAST_PORT = 42000;
    
    /**
     * Default port for registry (1099).
     */
    public static final int REGISTRY_PORT = Registry.REGISTRY_PORT;
    
    /**
     * Default timeout for multicast socket (5 seconds).
     */
    public static final int SOCKET_TIMEOUT = 5000;
    
    private Registry registry;
    private NornNodeInfo nodeInfo;
    private NornNodeThread nodeThread;

    /**
     * 
     * @param nodeInfo
     * @param registry 
     */
    NornNode(NornNodeInfo nodeInfo, Registry registry) {
        this(nodeInfo, registry, null);
    }

    /**
     * 
     * @param nodeInfo
     * @param registry
     * @param nodeThread 
     */
    NornNode(NornNodeInfo nodeInfo, Registry registry, NornNodeThread nodeThread) {
        super();
        this.nodeInfo = nodeInfo;
        this.registry = registry;
        this.nodeThread = nodeThread;
    }

    /**
     * Binds a remote reference to the specified <code>name</code> in this node.
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
     * Removes the binding for the specified <code>name</code> in this node.
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
     * Replaces the binding for the specified <code>name</code> in this node
     * with the supplied remote reference. If there is an existing binding for 
     * the specified <code>name</code>, it is discarded.
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
     * Returns the remote reference bound to the specified <code>name</code> in 
     * this node.
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
