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

import com.github.mgeiss.norn.util.NornUtility;

import java.io.IOException;
import java.net.*;

/**
 * <code>NornNodeThread</code> is the listening thread for incoming client requests. When a client touches this node,
 * the node will send an answer containing information about this node back to the client.
 *
 * @author Markus Geiss
 * @version 1.0.0
 * @see com.github.mgeiss.norn.NornNodeInfo
 * @see java.lang.Thread
 */
public final class NornNodeThread
        extends Thread {

    private final NornNodeInfo nodeInfo;
    private MulticastSocket multicastSocket;
    private boolean listen = true;

    /**
     * Package private constructor.
     *
     * @param nodeInfo information of the node
     */
    NornNodeThread(NornNodeInfo nodeInfo) {
        super();
        this.nodeInfo = nodeInfo;
    }

    /**
     * <code>run</code> starts a listener on the multicast address and joins the group. It will accept requests on
     * the multicast port.
     */
    @Override
    public void run() {
        try {
            final InetAddress multicastAddress = InetAddress.getByName(this.nodeInfo.getMulticastAddress());

            this.multicastSocket = new MulticastSocket(this.nodeInfo.getMulticastPort());
            this.multicastSocket.joinGroup(multicastAddress);

            final byte[] messageBuffer = new byte[1024];
            final DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length);

            while (this.listen) {
                try {
                    this.multicastSocket.receive(message);
                    final InetAddress address = message.getAddress();
                    final int port = message.getPort();

                    try {
                        this.sendNodeInfo(address, port);
                    } catch (IOException ioex) {
                        // intentionally left blank, just ignore only sending failed
                    }
                } catch (SocketException sex) {
                    // somebody stopped this thread
                    break;
                }
            }
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        } finally {
            if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
                this.multicastSocket.close();
            }
        }
    }

    /**
     * <code>interrupt</code> will stop the listener and closes the socket.
     */
    @Override
    public void interrupt() {
        this.listen = false;
        if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
            this.multicastSocket.close();
        }
        super.interrupt();
    }

    /**
     * <code>sendNodeInfo</code> sends the information about this node to the client.
     *
     * @param address the address of the client
     * @param port    the port on which the client will accept requests
     * @throws java.io.IOException
     * @see com.github.mgeiss.norn.NornNodeInfo
     */
    private void sendNodeInfo(final InetAddress address, final int port)
            throws IOException {
        this.nodeInfo.setLoad(NornUtility.calculateJVMLoad());

        final byte[] messageBuffer = NornUtility.nodeInfo2ByteArray(nodeInfo);

        final DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length, address, port);
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.send(message);
            datagramSocket.close();
        }
    }
}
