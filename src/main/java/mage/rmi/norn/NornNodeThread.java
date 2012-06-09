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
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NornNodeThread extends Thread {

    private NornNodeInfo nodeInfo;
    private MulticastSocket multicastSocket;
    private boolean listen = true;
    
    NornNodeThread(NornNodeInfo nodeInfo) {
        super();
        this.nodeInfo = nodeInfo;
    }
    
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
            Logger.getLogger(NornUtil.NORN_LOGGER).log(Level.SEVERE, ioex.getMessage(), ioex);
        }

        if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
            this.multicastSocket.close();
        }
    }

    @Override
    public void interrupt() {
        if (this.multicastSocket != null && !this.multicastSocket.isClosed()) {
            this.multicastSocket.close();
        }
        this.listen = false;
        super.interrupt();
    }

    private void sendNodeInfo(InetAddress address, int port) throws IOException {
        this.nodeInfo.setLoad(NornUtil.calculateJVMLoad());

        byte[] messageBuffer = NornUtil.nodeInfo2ByteArray(nodeInfo);

        DatagramPacket message = new DatagramPacket(messageBuffer, messageBuffer.length, address, port);
        try (DatagramSocket datagrammSocket = new DatagramSocket()) {
            datagrammSocket.send(message);
            datagrammSocket.close();
        }
    }
}
