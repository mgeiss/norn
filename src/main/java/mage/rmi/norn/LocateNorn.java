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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public final class LocateNorn {
    
    private static NornNodeThread nornNodeThread;

    private LocateNorn() {
        super();
    }

    public static NornNode createNode() throws RemoteException {
        return LocateNorn.createNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT, NornNode.REGISTRY_PORT);
    }

    public static NornNode createNode(String multicastAddress) throws RemoteException {
        return LocateNorn.createNode(multicastAddress, NornNode.MULTICAST_PORT, NornNode.REGISTRY_PORT);
    }

    public static NornNode createNode(int multicastPort) throws RemoteException {
        return LocateNorn.createNode(NornNode.MULTICAST_ADDRESS, multicastPort, NornNode.REGISTRY_PORT);
    }

    public static NornNode createNode(String multicastAddress, int multicastPort) throws RemoteException {
        return LocateNorn.createNode(multicastAddress, multicastPort, NornNode.REGISTRY_PORT);
    }

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
        nodeInfo.setLoad(NornUtil.calculateJVMLoad());

        Registry registry = LocateRegistry.createRegistry(registryPort);

        NornNodeThread nodeThread = new NornNodeThread(nodeInfo);

        node = new NornNode(nodeInfo, registry, nodeThread);
        node.start();

        return node;
    }

    public static NornNode getNode() throws RemoteException {
        return LocateNorn.getNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT);
    }

    public static NornNode getNode(int socketTimeout) throws RemoteException {
        return LocateNorn.getNode(NornNode.MULTICAST_ADDRESS, NornNode.MULTICAST_PORT, socketTimeout);
    }

    public static NornNode getNode(String multicastAddress) throws RemoteException {
        return LocateNorn.getNode(multicastAddress, NornNode.MULTICAST_PORT, NornNode.SOCKET_TIMEOUT);
    }

    public static NornNode getNode(String multicastAddress, int multicastPort) throws RemoteException {
        return LocateNorn.getNode(multicastAddress, multicastPort, NornNode.SOCKET_TIMEOUT);
    }

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
                nodeInfos.add(NornUtil.byteArray2NodeInfo(messageBuffer));
            }

            NornNodeInfo nodeInfo = NornUtil.getRecentNodeInfo(nodeInfos);
            
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
