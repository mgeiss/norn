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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NornUtil {
    
    public static final String NORN_LOGGER = "norn.logger";

    public static byte[] nodeInfo2ByteArray(NornNodeInfo nodeInfo) throws IOException {
        byte[] data = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(nodeInfo);
        oos.flush();
        oos.close();

        baos.close();
        data = baos.toByteArray();

        return data;
    }

    public static NornNodeInfo byteArray2NodeInfo(byte[] data) throws IOException, ClassNotFoundException {
        NornNodeInfo nodeInfo = null;

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);

        nodeInfo = (NornNodeInfo) ois.readObject();

        ois.close();
        bais.close();

        return nodeInfo;
    }

    public static NornNodeInfo getRecentNodeInfo(List<NornNodeInfo> nodeInfos) {
        NornNodeInfo nodeInfo = null;

        if (nodeInfos != null && nodeInfos.size() > 0) {
            Collections.sort(nodeInfos, new Comparator<NornNodeInfo>() {

                @Override
                public int compare(NornNodeInfo nodeInfo1, NornNodeInfo nodeInfo2) {
                    int order = 0;

                    double load1 = nodeInfo1.getLoad();
                    double load2 = nodeInfo2.getLoad();

                    if (load1 < load2) {
                        order = -1;
                    } else if (load1 > load2) {
                        order = 1;
                    }

                    return order;
                }
            });

            nodeInfo = nodeInfos.get(0);
        }

        return nodeInfo;
    }

    public static double calculateJVMLoad() {
        double load = -1.0D;
        
        int processors = Runtime.getRuntime().availableProcessors();
        int threads = ManagementFactory.getThreadMXBean().getThreadCount();

        load = threads / processors;
        
        return load;
    }
}
