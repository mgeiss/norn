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

import com.github.mgeiss.norn.util.NornConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NornTest {

    private static NornNode serverNornNode;
    private static DeepThought deepThought = new DeepThoughtImpl();
    private static final StringBuilder TELEMETRY = new StringBuilder("TELEMETRY >>>\n");

    private static final NornConfiguration nornConfiguration = new NornConfiguration.Builder()
            .multicastAddress("225.6.7.8")
            .multicastPort(43000)
            .rmiRegistryPort(2345)
            .socketTimeout(70)
            .master()
            .build();


    public NornTest() {
        super();
    }

    @BeforeClass
    public static void startNornNode() {
        System.setProperty("java.security.policy", "src/test/java/policy/norntest.policy");
        try {
            long time = System.currentTimeMillis();
            NornTest.serverNornNode = LocateNorn.createNode(NornTest.nornConfiguration);
            NornTest.TELEMETRY.append("Server node started in " + (System.currentTimeMillis() - time) + " ms!\n");

            time = System.currentTimeMillis();
            NornTest.serverNornNode.rebind(DeepThought.class.getSimpleName(),
                    UnicastRemoteObject.exportObject(NornTest.deepThought, 0));
            NornTest.TELEMETRY.append("Remote object bound in " + (System.currentTimeMillis() - time) + " ms!\n");
        } catch (RemoteException rex) {
            fail("RemoteException: " + rex.getMessage());
        }
    }

    @Test
    public void shouldLookupDeepThoughtAndAnswerTheUltimateQuestion() {
        try {
            long time = System.currentTimeMillis();
            final NornNode nornNode = LocateNorn.getNode(NornTest.nornConfiguration);
            NornTest.TELEMETRY.append("Node located in " + (System.currentTimeMillis() - time) + " ms!\n");

            time = System.currentTimeMillis();
            final DeepThought deepThoughtRef = (DeepThought) nornNode.lookup(DeepThought.class.getSimpleName());
            NornTest.TELEMETRY.append("Remote object looked up in " + (System.currentTimeMillis() - time) + " ms!\n");

            time = System.currentTimeMillis();
            final String answer = deepThoughtRef.answerToTheUltimateQuestion();
            NornTest.TELEMETRY.append("Answer to the ultimate question received in "
                    + (System.currentTimeMillis() - time) + " ms!\n");

            assertEquals("42", answer);
        } catch (NotBoundException nbex) {
            fail("NotBoundException: " + nbex.getMessage());
        } catch (RemoteException rex) {
            fail("RemoteException: " + rex.getMessage());
        }
    }

    @AfterClass
    public static void stopNornNode() {
        if (NornTest.serverNornNode != null) {
            long time = System.currentTimeMillis();
            NornTest.serverNornNode.stop();
            NornTest.TELEMETRY.append("Server node stopped in " + (System.currentTimeMillis() - time) + " ms!\n");
        }
        NornTest.TELEMETRY.append("<<< TELEMETRY\n");
        System.out.println(NornTest.TELEMETRY);
    }
}
