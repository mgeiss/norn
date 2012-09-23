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

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

public class NornTest {

    private static NornNode serverNornNode;
    private static DeepThought deepThought = new DeepThoughtImpl();

    public NornTest() {
        super();
    }

    @BeforeClass
    public static void startNornNode() {
        System.setProperty("java.security.policy", "src/test/java/policy/norntest.policy");
        try {
            NornTest.serverNornNode = LocateNorn.createNode();
            NornTest.serverNornNode.rebind(DeepThought.class.getSimpleName(), UnicastRemoteObject.exportObject(NornTest.deepThought, 0));
        } catch (RemoteException rex) {
            fail("RemoteException: " + rex.getMessage());
        }
    }

    @Test
    public void lookupDeepThoughtAndAnswerTheUltimateQuestion() {
        try {
            NornNode nornNode = LocateNorn.getNode(100);
            DeepThought deepThoughtRef = (DeepThought) nornNode.lookup(DeepThought.class.getSimpleName());
            String answer = deepThoughtRef.answerToTheUltimateQuestion();
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
            NornTest.serverNornNode.stop();
        }
    }
}
