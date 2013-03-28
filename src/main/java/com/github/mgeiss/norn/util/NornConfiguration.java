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
package com.github.mgeiss.norn.util;

import java.util.Objects;

/**
 * <code>NornConfiguration</code> is used to programmatically create a <code>NornNode</code>.
 *
 * @author Markus Geiss
 * @version 2.0.0
 */
public final class NornConfiguration {

    /**
     * <code>Builder</code> realizes the builder pattern for a <code>NornConfiguration</code>.
     */
    public static final class Builder {
        private String multicastAddress = "234.5.6.7";
        private int multicastPort = 42000;
        private int rmiRegistryPort = 1099;
        private int socketTimeout = 5000;
        private boolean master = false;

        /**
         * Sole constructor.
         */
        public Builder() {
            super();
        }

        /**
         * The multicast address used for UDP.
         *
         * @param multicastAddress the multicast address
         * @return this builder instance
         */
        public Builder multicastAddress(final String multicastAddress) {
            if (multicastAddress == null || multicastAddress.length() == 0
                    || !NornUtility.isValidMulticastAddress(multicastAddress)) {
                throw new IllegalArgumentException("Multicast address must be given and contains a valid address " +
                        "between 224.0.0.0 and 239.255.255.255");
            }
            this.multicastAddress = multicastAddress;
            return this;
        }

        /**
         * The multicast port used for UDP.
         *
         * @param multicastPort a valid port number greater 0 and equals or lesser 65535
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException
         *          if the port number equals or is lesser 0 and greater 65535
         */
        public Builder multicastPort(final int multicastPort) {
            if (multicastPort < 1 || multicastPort > 65535) {
                throw new IllegalArgumentException("Mutilcast port must be between 1 and 65535");
            }
            this.multicastPort = multicastPort;
            return this;
        }

        /**
         * The registry port used for RMI.
         *
         * @param rmiRegistryPort the rmi registry port
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException
         *          if the port number equals or is lesser 0 and greater 65535
         */
        public Builder rmiRegistryPort(final int rmiRegistryPort) {
            if (rmiRegistryPort < 1 || rmiRegistryPort > 65535) {
                throw new IllegalArgumentException("RMI registry port must be between 1 and 65535");
            }
            this.rmiRegistryPort = rmiRegistryPort;
            return this;
        }

        /**
         * The socket timeout used by a client, waiting for a server response.
         *
         * @param socketTimeout the socket timeout
         * @return this builder instance
         * @throws java.lang.IllegalArgumentException
         *          if the timeout ist lesser than 0
         */
        public Builder socketTimeout(final int socketTimeout) {
            if (socketTimeout < 0) {
                throw new IllegalArgumentException("Socket timeout must be greater or equals 0");
            }
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

        return this.master == that.master;
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
