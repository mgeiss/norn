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

import junit.framework.Assert;
import org.junit.Test;

import java.util.ResourceBundle;

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
public class NornConditionsTest {

    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public NornConditionsTest() {
        super();
    }

    @Test
    public void shouldSucceedMulticastAddressCheck() {
        try {
            NornConditions.checkMulticastAddress("224.0.0.0");
        } catch (IllegalArgumentException iaex) {
            Assert.fail();
        }
    }

    @Test
    public void shouldFailMulticastAddressCheck() {
        try {
            NornConditions.checkMulticastAddress("192.168.0.1");
            Assert.fail();
        } catch (IllegalArgumentException iaex) {
            Assert.assertEquals(messages.getString("message.illegal.argument.multicast.address"), iaex.getMessage());
        }
    }

    @Test
    public void shouldSucceedMulticastPortCheck() {
        try {
            NornConditions.checkMulticastPort(52345);
        } catch (IllegalArgumentException iaex) {
            Assert.fail();
        }
    }

    @Test
    public void shouldFailMulticastPortCheck() {
        try {
            NornConditions.checkMulticastPort(143);
            Assert.fail();
        } catch (IllegalArgumentException iaex) {
            Assert.assertEquals(messages.getString("message.illegal.argument.multicast.port"), iaex.getMessage());
        }
    }

    @Test
    public void shouldSucceedRMIRegistryPortCheckWithDefaultPort() {
        try {
            NornConditions.checkRMIRegistryPort(1099);
        } catch (IllegalArgumentException iaex) {
            Assert.fail();
        }
    }

    @Test
    public void shouldSucceedRMIRegistryPortCheck() {
        try {
            NornConditions.checkRMIRegistryPort(61234);
        } catch (IllegalArgumentException iaex) {
            Assert.fail();
        }
    }

    @Test
    public void shouldFailRMIRegistryPortCheck() {
        try {
            NornConditions.checkRMIRegistryPort(389);
            Assert.fail();
        } catch (IllegalArgumentException iaex) {
            Assert.assertEquals(messages.getString("message.illegal.argument.rmi.registry.port"), iaex.getMessage());
        }
    }

    @Test
    public void shouldSucceedSocketTimeoutCheck() {
        try {
            NornConditions.checkSocketTimeout(5000);
        } catch (IllegalArgumentException iaex) {
            Assert.fail();
        }
    }

    @Test
    public void shouldFailSocketTimeoutCheckWithNumberLesserZero() {
        try {
            NornConditions.checkSocketTimeout(-3000);
            Assert.fail();
        } catch (IllegalArgumentException iaex) {
            Assert.assertEquals(messages.getString("message.illegal.argument.socket.timeout"), iaex.getMessage());
        }
    }
}
