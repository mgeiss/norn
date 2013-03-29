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

public final class NornConditions {

    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    private NornConditions() {
        super();
    }

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

    public static void checkMulticastPort(int multicastPort) {
        if (multicastPort < 49152 || multicastPort > 65535) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.multicast.port"));
        }
    }

    public static void checkRMIRegistryPort(int rmiRegistryPort) {
        if (rmiRegistryPort != 1099
                && (rmiRegistryPort < 49152 || rmiRegistryPort > 65535)) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.rmi.registry.port"));
        }
    }

    public static void checkSocketTimeout(int socketTimeout) {
        if (socketTimeout < 0 || socketTimeout > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(messages.getString("message.illegal.argument.socket.timeout"));
        }
    }
}
