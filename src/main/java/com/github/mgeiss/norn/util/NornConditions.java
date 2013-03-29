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

import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * <code>NornConditions</code> provides simple static methods to be called at the start of your own methods to verify
 * correct arguments.
 * <p/>
 * @author Markus Geiss
 * @version 2.1.0
 */
public final class NornConditions {

    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    /**
     * Sole constructor.
     */
    private NornConditions() {
        super();
    }

    /**
     * Ensures that <code>multicastAddress</code> is not null and consists of a valid IPv4 address in the range from
     * 224.0.0.0 through 239.255.255.255.
     *
     * @param multicastAddress a multicast address
     * @throws java.lang.IllegalArgumentException if <code>multicastAddress</code> is null or does not consists a IPv4
     * address within the expected range
     */
    public static void checkMulticastAddress(String multicastAddress) {

        boolean checkFailed = false;
        if (multicastAddress == null) {
            checkFailed = true;
        }

        if (!checkFailed) {
            final StringTokenizer tokenizer = new StringTokenizer(multicastAddress, ".");
            if (tokenizer.countTokens() != 4) {
                checkFailed = true;
            }

            final int firstOctet = Integer.valueOf(tokenizer.nextToken());
            if (firstOctet < 224 || firstOctet > 239) {
                checkFailed = true;
            }

            int nextOctet;
            while (tokenizer.hasMoreTokens()) {
                nextOctet = Integer.valueOf(tokenizer.nextToken());
                if (nextOctet < 0 || nextOctet > 255) {
                    checkFailed = true;
                }
            }
        }

        if (checkFailed) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.multicast.address"));
        }
    }

    /**
     * Ensures that <code>multicastPort</code> consists of a valid dynamic port in the range from 49152 through 65535.
     *
     * @param multicastPort a multicast port
     * @throws java.lang.IllegalArgumentException if <code>multicastPort</code> does not consists a port within the
     * expected range
     */
    public static void checkMulticastPort(int multicastPort) {
        if (multicastPort < 49152 || multicastPort > 65535) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.multicast.port"));
        }
    }

    /**
     * Ensures that <code>rmiRegistryPort</code> is either 1099 or consits of a valid dynamic port in the range from
     * 41952 through 65535.
     *
     * @param rmiRegistryPort a RMI registry port
     * @throws java.lang.IllegalArgumentException if <code>rmiRegistryPort</code> is not 1099 or does not consists a
     * port within the expected range
     */
    public static void checkRMIRegistryPort(int rmiRegistryPort) {
        if (rmiRegistryPort != 1099
                && (rmiRegistryPort < 49152 || rmiRegistryPort > 65535)) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.rmi.registry.port"));
        }
    }

    /**
     * Ensures that <code>socketTimeout</code> consists of a valid number in the range from 0 to 2147483647.
     *
     * @param socketTimeout a socket timeout
     * @throws java.lang.IllegalArgumentException if <code>socketTimeout</code> does not consists of a number within
     * the expected range
     */
    public static void checkSocketTimeout(int socketTimeout) {
        if (socketTimeout < 0 || socketTimeout > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.socket.timeout"));
        }
    }
}
