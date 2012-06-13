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
package com.github.mgeiss.norn;

import com.github.mgeiss.norn.util.NornUtility;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * <code>LocateNorn</code> is used to obtain a reference to a <code>NornNode</code>
 * on a specific multicast address, or to create a <code>NornNode</code> that 
 * accepts requests on a specific port.
 * 
 * <p>Note that also a reference to a remote object registry will be obtained.
 * 
 * @author Markus Geiss
 * @version 1.0
 * @see com.github.mgeiss.norn.NornNode
 */
public final class LocateNorn {
    
    private static NornNodeThread nornNodeThread;

    /**
     * Private constructor to disable public construction.
     */
    private LocateNorn() {
        super();
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host
     * that accepts requests on port 42000. This instance will join the 
     * multicast group on 234.5.6.7.
     * 
     * <p>Note that also a <code>Registry</code> instance  will be created and
     * exported on the local host that accepts requests on port 1099. 
     * 
     * @return the norn node
     * @throws RemoteException
     */
    public static NornNode createNode() throws RemoteException {
        return LocateNorn.createNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT, NornNode.REGISTRY_PORT);
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host
     * that accepts requests on port 42000. This instance will join the
     * multicast group on the specified <code>multicastAddress</code>.
     * 
     * <p>Note that also a <code>Registry</code> instance  will be created and
     * exported on the local host that accepts requests on port 1099. 
     * 
     * @param multicastAddress address for the multicast group
     * @return the norn node
     * @throws RemoteException 
     */
    public static NornNode createNode(String multicastAddress) throws RemoteException {
        return LocateNorn.createNode(multicastAddress, NornNode.MULTICAST_PORT, NornNode.REGISTRY_PORT);
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host
     * that accepts requests on the specified <code>mulitcastPort</code>. This 
     * instance will join the multicast group on the specified
     * <code>multicastAddress</code>.
     * 
     * <p>Note that also a <code>Registry</code> instance  will be created and
     * exported on the local host that accepts requests on port 1099. 
     * 
     * @param multicastAddress address for the multicast group
     * @param multicastPort port on which the udp multicast accepts requests
     * @return the norn node
     * @throws RemoteException 
     */
    public static NornNode createNode(String multicastAddress, int multicastPort) throws RemoteException {
        return LocateNorn.createNode(multicastAddress, multicastPort, NornNode.REGISTRY_PORT);
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host
     * that accepts requests on the specified <code>mulitcastPort</code>. This 
     * instance will join the multicast group on the specified
     * <code>multicastAddress</code>.
     * 
     * <p>Note that also a <code>Registry</code> instance  will be created and
     * exported on the local host that accepts requests on the specified
     * <code>regsitryPort</code>. 
     * 
     * @param multicastAddress address for the multicast group
     * @param multicastPort port on which the multicast accepts requests
     * @param registryPort port on which the registry accepts requests 
     * @return the norn node
     * @throws RemoteException 
     */
    public static NornNode createNode(String multicastAddress, int multicastPort, int registryPort) throws RemoteException {
        NornNode node = null;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        if (multicastAddress == null || multicastAddress.length() == 0) {
            multicastAddress = NornNode.MULTICAST_ADDRESS;
        }

        if (multicastPort <= 0) {
            multicastPort = NornNode.MULTICAST_PORT;
        }

        if (registryPort <= 0) {
            registryPort = NornNode.REGISTRY_PORT;
        }

        NornNodeInfo nodeInfo = new NornNodeInfo();
        nodeInfo.setMulticastAddress(multicastAddress);
        nodeInfo.setMulticastPort(multicastPort);

        try {
            String registryAddress = InetAddress.getLocalHost().getHostAddress();
            nodeInfo.setRegistryAddress(registryAddress);
        } catch (UnknownHostException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }

        nodeInfo.setRegistryPort(registryPort);
        nodeInfo.setLoad(NornUtility.calculateJVMLoad());

        Registry registry = LocateRegistry.createRegistry(registryPort);

        NornNodeThread nodeThread = new NornNodeThread(nodeInfo);

        node = new NornNode(nodeInfo, registry, nodeThread);
        node.start();

        return node;
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the default multicast 
     * address 234.5.6.7 on the default multicast port 42000. <code>getNode</code>
     * will block for 5 seconds to receive node informations.
     * 
     * <p>Note that also a reference for the remote object <code>Registry</code>
     * will be created on the host and port specified by the <code>NornNode</code>. 
     * 
     * @return reference to the norn node
     * @throws RemoteException 
     */
    public static NornNode getNode() throws RemoteException {
        return LocateNorn.getNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT);
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the default multicast 
     * address 234.5.6.7 on the default multicast port 42000. <code>getNode</code>
     * will block for the specified <code>socketTimeout</code> to receive node
     * informations.
     * 
     * <p>Note that also a reference for the remote object <code>Registry</code>
     * will be created on the host and port specified by the <code>NornNode</code>. 
     * 
     * @param socketTimeout the specified timeout in milliseconds
     * @return reference to the norn node
     * @throws RemoteException 
     */
    public static NornNode getNode(int socketTimeout) throws RemoteException {
        return LocateNorn.getNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT, socketTimeout);
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the specified 
     * <code>multicastAddress</code> on the default multicast port 42000.
     * <code>getNode</code> will block for 5 seconds to receive node informations.
     * 
     * <p>Note that also a reference for the remote object <code>Registry</code>
     * will be created on the host and port specified by the <code>NornNode</code>. 
     * 
     * @param multicastAddress address for the multicast group
     * @return reference to the norn node
     * @throws RemoteException 
     */
    public static NornNode getNode(String multicastAddress) throws RemoteException {
        return LocateNorn.getNode(multicastAddress, NornNode.MULTICAST_PORT, NornNode.SOCKET_TIMEOUT);
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the specified 
     * <code>multicastAddress</code> on the specified <code>multicastPort</code>.
     * <code>getNode</code> will block for 5 seconds to receive node informations.
     * 
     * <p><p>Note that also a reference for the remote object <code>Registry</code>
     * will be created on the host and port specified by the <code>NornNode</code>. 
     * 
     * @param multicastAddress address for the multicast group
     * @param multicastPort port on which the multicast accepts requests
     * @return reference to the norn node
     * @throws RemoteException 
     */
    public static NornNode getNode(String multicastAddress, int multicastPort) throws RemoteException {
        return LocateNorn.getNode(multicastAddress, multicastPort, NornNode.SOCKET_TIMEOUT);
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the specified 
     * <code>multicastAddress</code> on the specified <code>multicastPort</code>.
     * <code>getNode</code> will block for the specified <code>socketTimeout</code>
     * to receive node informations.
     * 
     * <p>Note that also a reference for the remote object <code>Registry</code>
     * will be created on the host and port specified by the <code>NornNode</code>. 
     * 
     * @param multicastAddress address for the multicast group
     * @param multicastPort port on which the multicast accepts requests
     * @param socketTimeout the specified timeout in milliseconds
     * @return reference to the norn node
     * @throws RemoteException 
     */
    public static NornNode getNode(String multicastAddress, int multicastPort, int socketTimeout) throws RemoteException {
        NornNode node = null;

        if (multicastAddress == null || multicastAddress.length() == 0) {
            multicastAddress = NornNode.MULTICAST_ADDRESS;
        }

        if (multicastPort <= 0) {
            multicastPort = NornNode.MULTICAST_PORT;
        }

        if (socketTimeout <= 0) {
            socketTimeout = NornNode.SOCKET_TIMEOUT;
        }

        MulticastSocket multicastSocket = null;
        try {
            InetAddress address = InetAddress.getByName(multicastAddress);

            multicastSocket = new MulticastSocket();

            DatagramPacket ping = new DatagramPacket(new byte[0], 0, address, multicastPort);

            multicastSocket.send(ping);

            byte[] messageBuffer = new byte[1024];
            DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length);

            ArrayList<NornNodeInfo> nodeInfos = new ArrayList<>();

            multicastSocket.setSoTimeout(socketTimeout);
            while (true) {
                try {
                    multicastSocket.receive(message);
                } catch (SocketTimeoutException stex) {
                    break;
                }
                nodeInfos.add(NornUtility.byteArray2NodeInfo(messageBuffer));
            }

            NornNodeInfo nodeInfo = NornUtility.getRecentNodeInfo(nodeInfos);
            
            Registry registry = LocateRegistry.getRegistry(nodeInfo.getRegistryAddress(), nodeInfo.getRegistryPort());
            
            node = new NornNode(nodeInfo, registry);

        } catch (IOException | ClassNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }

        if (multicastSocket != null) {
            multicastSocket.close();
        }

        return node;
    }
}
