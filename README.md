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
to a Node using a simple load algorhythm.

People
------
Norn has been written by Markus Geiss (mgeiss257@gmail.com).

Usage
-----
I provided a JUnit testcase, so you can see how simple it is to use norn.

License
-------
Copyright 2012 Markus Geiss

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
