/**
 * Copyright 2012 - 2013 Markus Geiss
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

import com.github.mgeiss.norn.util.NornConditions;
import com.github.mgeiss.norn.util.NornConfiguration;
import com.github.mgeiss.norn.util.NornProperties;
import com.github.mgeiss.norn.util.NornUtility;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * <code>LocateNorn</code> is used to obtain a reference to a <code>NornNode</code> on a specific multicast address,
 * or to create a <code>NornNode</code> that accepts requests on a specific port.
 * <p/>
 * <p>Note that also a reference to a remote object registry will be obtained.
 *
 * @author Markus Geiss
 * @version 2.0.0
 * @see com.github.mgeiss.norn.NornNode
 * @see com.github.mgeiss.norn.util.NornConfiguration
 */
public final class LocateNorn {

    private static NornProperties nornProperties = NornProperties.load();

    /**
     * Private constructor to disable public construction.
     */
    private LocateNorn() {
        super();
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host. All values from
     * <code>NornProperties</code> will be used.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host.
     *
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode createNode()
            throws RemoteException {
        return LocateNorn.createNode(LocateNorn.nornProperties.getMulticastAddress(),
                LocateNorn.nornProperties.getMulticastPort(),
                LocateNorn.nornProperties.getRmiRegistryPort(),
                LocateNorn.nornProperties.isMasterNode());
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host. All values from
     * <code>NornProperties</code> will be used. <code>master</code> will override the default value from
     * <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host.
     *
     * @param master flags this node as a cluster master
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode createNode(final boolean master)
            throws RemoteException {
        return LocateNorn.createNode(LocateNorn.nornProperties.getMulticastAddress(),
                LocateNorn.nornProperties.getMulticastPort(),
                LocateNorn.nornProperties.getRmiRegistryPort(),
                master);
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host. All values from
     * <code>NornProperties</code> will be used. <code>multicastAddress</code> will override the default value from
     * <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host.
     *
     * @param multicastAddress address for the multicast group
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConfiguration
     * @see com.github.mgeiss.norn.util.NornConditions
     */
    public static NornNode createNode(final String multicastAddress)
            throws RemoteException {
        return LocateNorn.createNode(multicastAddress,
                LocateNorn.nornProperties.getMulticastPort(),
                LocateNorn.nornProperties.getRmiRegistryPort(),
                LocateNorn.nornProperties.isMasterNode());
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host. All values from
     * <code>NornProperties</code> will be used. <code>multicastAddress</code> and <code>multicastPort</code> will
     * override the default values from <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host.
     *
     * @param multicastAddress address for the multicast group
     * @param multicastPort    port on which the udp multicast accepts requests
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConfiguration
     * @see com.github.mgeiss.norn.util.NornConditions
     */
    public static NornNode createNode(final String multicastAddress, final int multicastPort)
            throws RemoteException {
        return LocateNorn.createNode(multicastAddress,
                multicastPort,
                LocateNorn.nornProperties.getRmiRegistryPort(),
                LocateNorn.nornProperties.isMasterNode());
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host based on the given
     * <code>NornConfiguration</code>.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host.
     *
     * @param nornConfiguration configuration to use
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode createNode(final NornConfiguration nornConfiguration)
            throws RemoteException {
        return LocateNorn.createNode(nornConfiguration.getMulticastAddress(),
                nornConfiguration.getMulticastPort(),
                nornConfiguration.getRmiRegistryPort(),
                nornConfiguration.isMaster());
    }

    /**
     * Creates and exports a <code>NornNode</code> instance on the local host that accepts requests on
     * the specified <code>mulitcastPort</code>. This instance will join the multicast group on the specified
     * <code>multicastAddress</code>.
     * <p/>
     * <p>Note that also a <code>Registry</code> instance will be created and exported on the local
     * host that accepts requests on the specified <code>regsitryPort</code>.
     *
     * @param multicastAddress address for the multicast group
     * @param multicastPort    port on which the multicast accepts requests
     * @param registryPort     port on which the registry accepts requests
     * @param master           flags this node as a cluster master
     * @return the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornConditions
     */
    public static NornNode createNode(final String multicastAddress, final int multicastPort, final int registryPort,
                                      final boolean master)
            throws RemoteException {
        NornConditions.checkMulticastAddress(multicastAddress);
        NornConditions.checkMulticastPort(multicastPort);
        NornConditions.checkRMIRegistryPort(registryPort);

        NornNode node;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        final NornNodeInfo nornNodeInfo = new NornNodeInfo();
        nornNodeInfo.setMulticastAddress(multicastAddress);
        nornNodeInfo.setMulticastPort(multicastPort);

        try {
            final String registryAddress = InetAddress.getLocalHost().getHostAddress();
            nornNodeInfo.setRegistryAddress(registryAddress);
        } catch (UnknownHostException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }

        nornNodeInfo.setRegistryPort(registryPort);
        nornNodeInfo.setLoad(NornUtility.calculateJVMLoad());
        nornNodeInfo.setMaster(master);

        final Registry registry = LocateRegistry.createRegistry(registryPort);

        final NornNodeThread nodeThread = new NornNodeThread(nornNodeInfo);

        node = new NornNode(nornNodeInfo, registry, nodeThread);
        node.start();

        return node;
    }

    /**
     * Returns a reference to the <code>NornNode</code>. All values from <code>NornProperties</code> will be used.
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created on the host and port
     * specified by the <code>NornNode</code>.
     *
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode getNode()
            throws RemoteException {
        return LocateNorn.getNode(LocateNorn.nornProperties.getMulticastAddress(),
                LocateNorn.nornProperties.getMulticastPort(),
                LocateNorn.nornProperties.getSocketTimeout());
    }

    /**
     * Returns a reference to the <code>NornNode</code>. All values from <code>NornProperties</code> will be used.
     * <code>socketTimeout</code> will override the default value from <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created on the host and port
     * specified by the<code>NornNode</code>.
     *
     * @param socketTimeout the specified timeout in milliseconds
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConditions
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode getNode(int socketTimeout)
            throws RemoteException {
        return LocateNorn.getNode(LocateNorn.nornProperties.getMulticastAddress(),
                LocateNorn.nornProperties.getMulticastPort(),
                socketTimeout);
    }

    /**
     * Returns a reference to the <code>NornNode</code>. All values from <code>NornProperties</code> will be used.
     * <code>multicastAddress</code> will override the default value from <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created on the host and port
     * specified by the <code>NornNode</code>.
     *
     * @param multicastAddress address for the multicast group
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConditions
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode getNode(String multicastAddress)
            throws RemoteException {
        return LocateNorn.getNode(multicastAddress,
                LocateNorn.nornProperties.getMulticastPort(),
                LocateNorn.nornProperties.getSocketTimeout());
    }

    /**
     * Returns a reference to the <code>NornNode</code>. All values from <code>NornProperties</code> will be used.
     * <code>multicastAddress</code> and <code>multicastPort</code> will override the default values from
     * <code>NornProperties</code>.
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created on the host and port
     * specified by the <code>NornNode</code>.
     *
     * @param multicastAddress address for the multicast group
     * @param multicastPort    port on which the multicast accepts requests
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornProperties
     * @see com.github.mgeiss.norn.util.NornConditions
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode getNode(String multicastAddress, int multicastPort)
            throws RemoteException {
        return LocateNorn.getNode(multicastAddress,
                multicastPort,
                LocateNorn.nornProperties.getSocketTimeout());
    }

    /**
     * Returns a reference to the <code>NornNode</code> based on the given <code>NornConfiguration</code>
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created.
     *
     * @param nornConfiguration configuration to use
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see java.rmi.registry.Registry
     * @see com.github.mgeiss.norn.util.NornConditions
     * @see com.github.mgeiss.norn.util.NornConfiguration
     */
    public static NornNode getNode(NornConfiguration nornConfiguration)
            throws RemoteException {
        return LocateNorn.getNode(nornConfiguration.getMulticastAddress(),
                nornConfiguration.getMulticastPort(),
                nornConfiguration.getSocketTimeout());
    }

    /**
     * Returns a reference to the <code>NornNode</code> for the specified <code>multicastAddress</code> on the specified
     * <code>multicastPort</code>. <code>getNode</code> will block for the specified <code>socketTimeout</code>
     * to receive node information.
     * <p/>
     * <p>Note that also a reference for the remote object <code>Registry</code> will be created on the host and port
     * specified by the <code>NornNode</code>.
     *
     * @param multicastAddress address for the multicast group
     * @param multicastPort    port on which the multicast accepts requests
     * @param socketTimeout    the specified timeout in milliseconds
     * @return reference to the norn node
     * @throws java.rmi.RemoteException
     * @throws java.lang.IllegalArgumentException if an argument is not valid.
     * @see com.github.mgeiss.norn.util.NornConditions
     */
    public static NornNode getNode(final String multicastAddress, final int multicastPort, final int socketTimeout)
            throws RemoteException {
        NornConditions.checkMulticastAddress(multicastAddress);
        NornConditions.checkMulticastPort(multicastPort);
        NornConditions.checkSocketTimeout(socketTimeout);

        NornNode node;

        try (final MulticastSocket multicastSocket = new MulticastSocket()) {
            final InetAddress address = InetAddress.getByName(multicastAddress);

            final DatagramPacket ping = new DatagramPacket(new byte[0], 0, address, multicastPort);

            multicastSocket.send(ping);

            final byte[] messageBuffer = new byte[1024];
            DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length);

            final ArrayList<NornNodeInfo> nodeInfos = new ArrayList<>();

            multicastSocket.setSoTimeout(socketTimeout);
            while (true) {
                try {
                    multicastSocket.receive(message);
                } catch (SocketTimeoutException stex) {
                    break;
                }
                nodeInfos.add(NornUtility.byteArray2NodeInfo(messageBuffer));
            }

            final NornNodeInfo nodeInfo = NornUtility.getRecentNodeInfo(nodeInfos);

            final Registry registry = LocateRegistry.getRegistry(nodeInfo.getRegistryAddress(), nodeInfo.getRegistryPort());

            node = new NornNode(nodeInfo, registry);
        } catch (IOException | ClassNotFoundException ex) {
            throw new RemoteException(ex.getMessage(), ex);
        }

        return node;
    }
}
