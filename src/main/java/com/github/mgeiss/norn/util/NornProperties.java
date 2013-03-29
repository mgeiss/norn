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
package com.github.mgeiss.norn.util;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * The <code>NornProperties</code> class represents a persistent set of properties.
 *
 * @author Markus Geiss
 * @version 2.0.0
 */
public final class NornProperties {

    private String multicastAddress = NornConfiguration.DEFAULT_MULTICAST_ADDRESS;
    private int multicastPort = NornConfiguration.DEFAULT_MULTICAST_PORT;
    private int socketTimeout = NornConfiguration.DEFAULT_SOCKET_TIMEOUT;
    private int rmiRegistryPort = NornConfiguration.DEFAULT_RMI_REGISTRY_PORT;
    private boolean masterNode;

    private NornProperties() {
        super();
    }

    /**
     * Reads the property list (key and element pairs) from
     * <tt>norn.properties</tt>. The file is assumed to use the ISO 8859-1
     * character encoding; that is each byte is one Latin1 character. Characters
     * not in Latin1, and certain special characters, are represented in keys
     * and elements using Unicode escapes.
     *
     * @return a new instance of <code>NornProperties</code> or <tt>null</tt>
     * @see java.util.Properties
     */
    public static NornProperties load() {
        final NornProperties nornProperties = new NornProperties();

        final Properties properties = new Properties();
        try {
            properties.load(NornProperties.class.getResourceAsStream("/norn.properties"));

            final String multicastAddressProperty = properties.getProperty("com.github.mgeiss.norn.multicast.address");
            final Integer multicastPortProperty = Integer.valueOf(properties.getProperty("com.github.mgeiss.norn" +
                    ".multicast.port"));
            final Integer socketTimeoutProperty = Integer.valueOf(properties.getProperty("com.github.mgeiss.norn" +
                    ".multicast.timeout"));
            final Integer rmiRegistryPortProperty = Integer.valueOf(properties.getProperty("com.github.mgeiss.norn" +
                    ".rmi.registry.port"));
            final Boolean masterNodeProperty = Boolean.valueOf(properties.getProperty("com.github.mgeiss.norn.node" +
                    ".master"));

            nornProperties.setMulticastAddress(multicastAddressProperty);
            nornProperties.setMulticastPort(multicastPortProperty);
            nornProperties.setRmiRegistryPort(rmiRegistryPortProperty);
            nornProperties.setSocketTimeout(socketTimeoutProperty);
            nornProperties.setMaster(masterNodeProperty);
        } catch (IOException ex) {
            // intentionally left blank, using default values
        }

        return nornProperties;
    }

    /**
     * Returns the multicast address.
     *
     * @return the host name or IP address or <tt>null</tt>
     */
    public String getMulticastAddress() {
        return this.multicastAddress;
    }

    private void setMulticastAddress(final String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    /**
     * Returns the multicast port.
     *
     * @return a valid port number or -1
     */
    public int getMulticastPort() {
        return this.multicastPort;
    }

    private void setMulticastPort(final int multicastPort) {
        this.multicastPort = multicastPort;
    }

    /**
     * Returns the RMI registry port.
     *
     * @return a valid port number or -1
     */
    public int getRmiRegistryPort() {
        return this.rmiRegistryPort;
    }

    private void setRmiRegistryPort(final int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }

    /**
     * Returns the socket timeout.
     *
     * @return a valid timeout or 0
     */
    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    private void setSocketTimeout(final int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    /**
     * Returns the master flag.
     *
     * @return true if this node is the master node
     */
    public boolean isMasterNode() {
        return this.masterNode;
    }

    private void setMaster(final boolean masterNode) {
        this.masterNode = masterNode;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }

        final NornProperties that = (NornProperties) other;

        if (!Objects.equals(this.multicastAddress, that.multicastAddress)) {
            return false;
        }
        if (this.multicastPort != that.multicastPort) {
            return false;
        }
        if (this.socketTimeout != that.socketTimeout) {
            return false;
        }
        if (this.rmiRegistryPort != that.rmiRegistryPort) {
            return false;
        }
        if (this.masterNode != that.masterNode) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.multicastAddress);
        hash = 41 * hash + this.multicastPort;
        hash = 41 * hash + this.socketTimeout;
        hash = 41 * hash + this.rmiRegistryPort;
        hash = 41 * hash + (this.masterNode ? 1 : 0);
        return hash;
    }
}
