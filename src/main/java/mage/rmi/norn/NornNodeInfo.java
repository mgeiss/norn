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

import java.io.Serializable;

public class NornNodeInfo implements Serializable {

    private static final long serialVersionUID = -173857924623766009L;
    
    private String multicastAddress;
    private int multicastPort;
    private String registryAddress;
    private int registryPort;
    private double load = -1.0D;

    NornNodeInfo() {
        super();
    }

    public double getLoad() {
        return load;
    }

    void setLoad(double load) {
        this.load = load;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    void setMulticastAddress(String multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }

    @Override
    public String toString() {
        return "NornNodeInfo{" + "multicastAddress=" + multicastAddress + ", multicastPort=" + multicastPort + ", registryAddress=" + registryAddress + ", registryPort=" + registryPort + ", load=" + load + '}';
    }
}
