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

import java.util.Objects;

/**
 * <code>NornConfiguration</code> is used to programmatically configure a <code>NornNode</code>.
 *
 * @author Markus Geiss
 * @version 2.1.0
 */
public final class NornConfiguration {

    /**
     * Default address for multicast.
     */
    public static final String DEFAULT_MULTICAST_ADDRESS = "234.5.6.7";

    /**
     * Default port for multicast.
     */
    public static final int DEFAULT_MULTICAST_PORT = 52000;

    /**
     * Well known port for registry.
     */
    public static final int DEFAULT_RMI_REGISTRY_PORT = 1099;

    /**
     * Default socket timeout.
     */
    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;

    /**
     * <code>Builder</code> realizes the builder pattern for a <code>NornConfiguration</code>.
     *
     * @author Markus Geiss
     * @version 2.1.0
     */
    public static final class Builder {
        private String multicastAddress = NornConfiguration.DEFAULT_MULTICAST_ADDRESS;
        private int multicastPort = NornConfiguration.DEFAULT_MULTICAST_PORT;
        private int rmiRegistryPort = NornConfiguration.DEFAULT_RMI_REGISTRY_PORT;
        private int socketTimeout = NornConfiguration.DEFAULT_SOCKET_TIMEOUT;
        private boolean master;

        /**
         * Sole constructor.
         */
        public Builder() {
            super();
        }

        /**
         * The multicast address used for UDP.
         *
         * @param multicastAddress a multicast address
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException if <code>multicastAddress</code> is not valid
         * @see com.github.mgeiss.norn.util.NornConditions#checkMulticastAddress(String)
         */
        public Builder multicastAddress(final String multicastAddress) {
            NornConditions.checkMulticastAddress(multicastAddress);

            this.multicastAddress = multicastAddress;
            return this;
        }

        /**
         * The multicast port used for UDP.
         *
         * @param multicastPort a multicast port
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException if <code>multicastPort</code> is not valid
         * @see com.github.mgeiss.norn.util.NornConditions#checkMulticastPort(int)
         */
        public Builder multicastPort(final int multicastPort) {
            NornConditions.checkMulticastPort(multicastPort);

            this.multicastPort = multicastPort;
            return this;
        }

        /**
         * The registry port used for RMI.
         *
         * @param rmiRegistryPort a rmi registry port
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException if <code>rmiRegistryPort</code> is not valid
         * @see com.github.mgeiss.norn.util.NornConditions#checkRMIRegistryPort(int)
         */
        public Builder rmiRegistryPort(final int rmiRegistryPort) {
            NornConditions.checkRMIRegistryPort(rmiRegistryPort);

            this.rmiRegistryPort = rmiRegistryPort;
            return this;
        }

        /**
         * The socket timeout used by a client, waiting for a server response.
         *
         * @param socketTimeout a socket timeout
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException if <code>socketTimeout</code> is not valid
         * @see com.github.mgeiss.norn.util.NornConditions#checkSocketTimeout(int)
         */
        public Builder socketTimeout(final int socketTimeout) {
            NornConditions.checkSocketTimeout(socketTimeout);

            this.socketTimeout = socketTimeout;
            return this;
        }

        /**
         * Indicates that a node acts as a master node in a master/slave scenario.
         *
         * @return this builder instance
         */
        public Builder master() {
            this.master = true;
            return this;
        }

        /**
         * Creates a new <code>NornConfiguration</code> instance, using this builder.
         * <p/>
         * All not configured values will have defaults:<br/>
         * <code>multicastAddress</code> is <tt>234.5.6.7</tt><br/>
         * <code>multicastPort</code> is <tt>42000</tt><br/>
         * <code>rmiRegistryPort</code> is <tt>1099</tt><br/>
         * <code>socketTimeout</code> is <tt>5 seconds</tt><br/>
         * <code>master</code> is <tt>false</tt>
         *
         * @return a new created <code>NornConfiguration</code>
         */
        public NornConfiguration build() {
            return new NornConfiguration(this);
        }
    }

    private final String multicastAddress;
    private final int multicastPort;
    private final int rmiRegistryPort;
    private final int socketTimeout;
    private final boolean master;

    /**
     * Private constructor to restrict creation.
     *
     * @param builder the <code>Builder</code> to use for creation
     */
    private NornConfiguration(Builder builder) {
        super();
        this.multicastAddress = builder.multicastAddress;
        this.multicastPort = builder.multicastPort;
        this.rmiRegistryPort = builder.rmiRegistryPort;
        this.socketTimeout = builder.socketTimeout;
        this.master = builder.master;
    }

    /**
     * Returns the mulitcast address.
     *
     * @return a valid host name or IP address
     */
    public String getMulticastAddress() {
        return this.multicastAddress;
    }

    /**
     * Returns the multicast port.
     *
     * @return a valid port number
     */
    public int getMulticastPort() {
        return this.multicastPort;
    }

    /**
     * Returns the RMI registry port.
     *
     * @return a valid port number
     */
    public int getRmiRegistryPort() {
        return this.rmiRegistryPort;
    }

    /**
     * Returns the socket timeout.
     *
     * @return a valid timeout.
     */
    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    /**
     * Returns the master flag
     *
     * @return true if set, else false
     */
    public boolean isMaster() {
        return this.master;
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

        final NornConfiguration that = (NornConfiguration) other;

        if (!Objects.equals(this.multicastAddress, that.multicastAddress)) {
            return false;
        }
        if (this.multicastPort != that.multicastPort) {
            return false;
        }
        if (this.rmiRegistryPort != that.rmiRegistryPort) {
            return false;
        }
        if (this.socketTimeout != that.socketTimeout) {
            return false;
        }
        if (this.master != that.master) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.multicastAddress);
        result = 31 * result + this.multicastPort;
        result = 31 * result + this.rmiRegistryPort;
        result = 31 * result + (this.master ? 1 : 0);
        return result;
    }
}
