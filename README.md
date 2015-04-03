# Raw Sockets
This assignment works with the low-level operations of the Internet protocol stack. In real world the operating system's networking stack hides all the underlying complexity of creating and managing IP and TCP headers.This program takes a URL and downloads the associated file.This program uses SOCK_RAW/IPPROTO_RAW socket, which are responsible for building the IP and TCP headers in each packet. In essence, this program is rebuilding the operating system's TCP/IP stack within the application.

RockSaw is a simple API for performing network I/O with IPv4 and IPv6 raw sockets in Java.

Note : 
On most operating systems, you must have root access or administrative privileges to use raw sockets.Access to raw sockets requires root privileges on the operating system. Recall that raw sockets are promiscuous, i.e. they can observe all packets that arrive at a machine. It would be a security vulnerability if any program could open raw sockets, because that would enable you to spy on the network traffic of all other users using a shared machine

Always run in Virtual/Sandboxed environment.
