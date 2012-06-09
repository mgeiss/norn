/**
 * Copyright 2012 Markus Geiss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mage.rmi.norn;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class NornNode {

    public static final String MULTICAST_ADDRESS = "234.5.6.7";
    public static final int MULTICAST_PORT = 42000;
    public static final int REGISTRY_PORT = Registry.REGISTRY_PORT;
    public static final int SOCKET_TIMEOUT = 5000;
    
    private Registry registry;
    private NornNodeInfo nodeInfo;
    private NornNodeThread nodeThread;

    NornNode(NornNodeInfo nodeInfo, Registry registry) {
        this(nodeInfo, registry, null);
    }

    NornNode(NornNodeInfo nodeInfo, Registry registry, NornNodeThread nodeThread) {
        super();
        this.nodeInfo = nodeInfo;
        this.registry = registry;
        this.nodeThread = nodeThread;
    }

    public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
        this.registry.bind(name, obj);
    }

    public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
        this.registry.unbind(name);
    }

    public void rebind(String name, Remote obj) throws RemoteException, AccessException {
        this.registry.rebind(name, obj);
    }

    public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
        return this.registry.lookup(name);
    }

    public String[] list() throws RemoteException, AccessException {
        return this.registry.list();
    }

    public NornNodeInfo getNodeInfo() {
        return nodeInfo;
    }
    
    void start() {
        if (this.nodeThread != null) {
            this.nodeThread.start();
        }
    }

    public void stop() {
        if (this.nodeThread != null) {
            this.nodeThread.interrupt();
        }
    }
}
