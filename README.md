Norn
====
UDP Multicast based java remoting using RMI under the hood.

Summary
-------
In norse mythology the Norns are female beings who rule the destiny of gods and
men. They spin the thread of fate at the foot of Yggdrasil, the tree of the
world. For more information see [Wikipedia](http://en.wikipedia.org/wiki/Norns).

The approach of this project is to provide a simple java remoting cluster, that
feels like using RMI. It uses UDP multicast to find all RMI registry and connect
to a Node using a simple load algorithm.

People
------
Norn has been written by Markus Gei&szlig; (markus.geiss@live.de).

Usage
-----
I've provided a JUnit test case, so you can see how simple it is to use norn.

Since version 2.0.0 it is possible to configure a node programmatically.

    NornConfiguration nornConfiguration = new NornConfiguration.Builder()
            .multicastAddress("225.6.7.8")
            .multicastPort(52345)
            .rmiRegistryPort(53456)
            .socketTimeout(70)
            .master()
            .build();
    
    NornNode nornNode = LocateNorn.createNode(nornConfiguration);

License
-------
Copyright 2012 - 2014 Markus Gei&szlig;

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Maven
-----
To use norn just add the following dependency to your local pom.xml:

    <dependency>
        <groupId>com.github.mgeiss</groupId>
        <artifactId>norn</artifactId>
        <version>2.1.2</version>
    </dependency>
