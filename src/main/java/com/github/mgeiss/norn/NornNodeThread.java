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

import com.github.mgeiss.norn.util.NornUtility;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>NornNodeThread</code> is the listening thread for incoming client
 * requests. When a client touches this node, th node will send an answer
 * containing informations about this node back to the client.
 *
 * @author Markus Geiss
 * @version 1.0
 * @see com.github.mgeiss.norn.NornNodeInfo
 * @see java.lang.Thread
 */
public class NornNodeThread extends Thread {

    private NornNodeInfo nodeInfo;
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
     * <code>run</code> start a listener on the multicast address and joins the
     * group. It will acccept requests on the multicast port.
     */
    @Override
    public void run() {
        try {
            InetAddress multicastAddress = InetAddress.getByName(this.nodeInfo.getMulticastAddress());

            this.multicastSocket = new MulticastSocket(this.nodeInfo.getMulticastPort());
            this.multicastSocket.joinGroup(multicastAddress);

            byte[] messageBuffer = new byte[1024];
            DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length);

            while (this.listen) {
                try {
                    this.multicastSocket.receive(message);
                } catch (SocketException sex) {
                    // do nothing, somebody stoped this thread
                }

                InetAddress address = message.getAddress();
                int port = message.getPort();

                this.sendNodeInfo(address, port);
            }

        } catch (IOException ioex) {
            Logger.getGlobal().log(Level.SEVERE, ioex.getMessage(), ioex);
        }

        if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
            this.multicastSocket.close();
        }
    }

    /**
     * <code>interrupt</code> will stop the listener and closes the socket.
     */
    @Override
    public void interrupt() {
        if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
            this.multicastSocket.close();
        }
        this.listen = false;
        super.interrupt();
    }

    /**
     * <code>sendNodeInfo</code> sends the information about this node to the
     * client.
     *
     * @param address the address of the client
     * @param port the port on which the client will accept requests
     * @throws IOException
     * @see mage.rmi.norn.NornNodeInfo
     */
    private void sendNodeInfo(InetAddress address, int port) throws IOException {
        this.nodeInfo.setLoad(NornUtility.calculateJVMLoad());

        byte[] messageBuffer = NornUtility.nodeInfo2ByteArray(nodeInfo);

        DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length, address, port);
        try (DatagramSocket datagrammSocket = new DatagramSocket()) {
            datagrammSocket.send(message);
            datagrammSocket.close();
        }
    }
}
