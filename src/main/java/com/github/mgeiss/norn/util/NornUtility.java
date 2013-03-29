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

import com.github.mgeiss.norn.NornNodeInfo;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <code>NornUtility</code> provides some utility methods like converting a <code>NornNodeInfo</code> to a byte array
 * and vise versa.
 * <p/>
 * Also it provide a method to calculate the current JVM load.
 *
 * @author Markus Geiss
 * @version 2.1.0
 */
public final class NornUtility {

    /**
     * <code>MathContext</code> used to round load t 2 decimal places.
     */
    private static final MathContext DECIMAL_ROUND_2 = new MathContext(2, RoundingMode.HALF_EVEN);

    /**
     * <code>nodeInfo2ByteArray</code> converts a <code>NornNodeInfo</code> to a byte array so it can be streamed to the
     * client.
     *
     * @param nodeInfo instance of a node information to stream
     * @return a byte array
     * @throws IOException
     */
    public static byte[] nodeInfo2ByteArray(final NornNodeInfo nodeInfo)
            throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(nodeInfo);
        oos.flush();
        oos.close();

        baos.close();
        return baos.toByteArray();
    }

    /**
     * <code>byteArray2NodeInfo</code> converts a byte array to a <code>NornNodeInfo</code>.
     *
     * @param data byte array to convert
     * @return an instance of <code>NornNodeInfo</code>
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static NornNodeInfo byteArray2NodeInfo(final byte[] data)
            throws IOException, ClassNotFoundException {
        NornNodeInfo nodeInfo;

        try (final ByteArrayInputStream bais = new ByteArrayInputStream(data);
             final ObjectInputStream ois = new ObjectInputStream(bais)) {

            nodeInfo = (NornNodeInfo) ois.readObject();
        }

        return nodeInfo;
    }

    /**
     * Returns the <code>NornNodeInfo</code> with the lowest load.
     *
     * @param nodeInfos a list of <code>NornNodeInfo</code>
     * @return the <code>NornNodeInfo</code> with the lowest load
     */
    public static NornNodeInfo getRecentNodeInfo(final List<NornNodeInfo> nodeInfos) {
        NornNodeInfo nodeInfo = null;

        if (nodeInfos != null && nodeInfos.size() > 0) {
            Collections.sort(nodeInfos, new Comparator<NornNodeInfo>() {

                @Override
                public int compare(NornNodeInfo nodeInfo1, NornNodeInfo nodeInfo2) {
                    int order = 0;

                    final double load1 = nodeInfo1.getLoad();
                    final double load2 = nodeInfo2.getLoad();

                    final boolean master1 = nodeInfo1.isMaster();
                    final boolean master2 = nodeInfo2.isMaster();

                    if (master1 && !master2) {
                        order = -1;
                    } else if (!master1 && master2) {
                        order = 1;
                    } else {
                        if (load1 < load2) {
                            order = -1;
                        } else if (load1 > load2) {
                            order = 1;
                        }
                    }

                    return order;
                }
            });

            nodeInfo = nodeInfos.get(0);
        }

        return nodeInfo;
    }

    /**
     * <code>calculateJVMLoad</code> uses a simple approach based on the load calculation used by linux to determine
     * the current JVM load.
     *
     * @return the load for this JVM
     */
    public static double calculateJVMLoad() {
        double load;

        final Runtime runtime = Runtime.getRuntime();

        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        final ThreadInfo[] allThreads = threadMXBean.dumpAllThreads(true, true);
        int threads = 0;
        for (final ThreadInfo threadInfo : allThreads) {
            if (threadInfo.getThreadState() == Thread.State.RUNNABLE) {
                threads++;
            }
        }

        final int processors = runtime.availableProcessors();

        final double processorUsage = (double) threads / (double) processors;

        final long maxMemory = runtime.maxMemory();
        final long totalMemory = runtime.totalMemory();

        final double memoryUsage = (double) totalMemory / (double) maxMemory;

        load = (memoryUsage * 3.0D) + processorUsage;

        final BigDecimal bigDecimal = new BigDecimal(load);
        load = bigDecimal.round(NornUtility.DECIMAL_ROUND_2).doubleValue();

        return load;
    }
}
