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

import java.io.Serializable;

/**
 * <code>NornNodeInfo</code> holds all information from a remote host. This
 * includes information about the registry address, registry port and the load.
 *
 * @author Markus Geiss
 * @version 1.1.0
 */
public final class NornNodeInfo
        implements Serializable {

    private static final long serialVersionUID = 8112182598139555673L;
    private String multicastAddress;
    private int multicastPort;
    private String registryAddress;
    private int registryPort;
    private double load = -1.0D;
    private boolean master;

    /**
     * Trusted package private constructor
     */
    NornNodeInfo() {
        super();
    }

    /**
     * Returns the load of the remote host.
     *
     * @return load of the remote host
     */
    public double getLoad() {
        return load;
    }

    /**
     * Package private setter for the load of the remote host.
     *
     * @param load current load of this node
     */
    void setLoad(final double load) {
        this.load = load;
    }

    /**
     * Returns the multicast address.
     *
     * @return multicast address
     */
    public String getMulticastAddress() {
        return multicastAddress;
    }

    /**
     * Package private setter for the multicast address.
     *
     * @param multicastAddress address of the multicast group
     */
    void setMulticastAddress(final String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    /**
     * Returns the port on which the multicast accepts requests.
     *
     * @return port on which the multicast accepts requests
     */
    public int getMulticastPort() {
        return multicastPort;
    }

    /**
     * Package private setter for the multicast port.
     *
     * @param multicastPort port on which the multicast will accept requests
     */
    void setMulticastPort(final int multicastPort) {
        this.multicastPort = multicastPort;
    }

    /**
     * Returns the registry address.
     *
     * @return address of the remote registry
     */
    public String getRegistryAddress() {
        return registryAddress;
    }

    /**
     * Package private setter for the registry address.
     *
     * @param registryAddress address of the remote registry
     */
    void setRegistryAddress(final String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * Returns the port on which the remote registry accepts requests.
     *
     * @return port on which the remote registry accepts requests
     */
    public int getRegistryPort() {
        return registryPort;
    }

    /**
     * Package private setter for the remote registry port.
     *
     * @param registryPort the port on which the remote registry will accept
     *                     requests
     */
    void setRegistryPort(final int registryPort) {
        this.registryPort = registryPort;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(final boolean master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return "NornNodeInfo{"
                + "multicastAddress=" + this.multicastAddress
                + ", multicastPort=" + this.multicastPort
                + ", registryAddress=" + this.registryAddress
                + ", registryPort="+ this.registryPort
                + ", load=" + this.load
                + ", master="+ this.master
                + '}';
    }
}
