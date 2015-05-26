# TCP-Simulating
In this project, a reliable transport protocol over the UDP protocol has been implemented. This reliable transport protocol is called My Transport Protocol (MTP). 
MTP implements features listing below:
1. A threeway handshake for the connection establishment.
2. MTP Sender maintains a single-timer for timeout operation.
3. MTP Sender implements the simplified TCP sender and fast retransmit.
4. MTP Sender is able to deal with different maximum segment size (MSS).
5. The maximum number of un-acknowledged bytes that the MTP Sender can have is Maximum Window size (MWS).
8. MTP Sender implement a Packet Loss (PLD) Module.
9. MTP Sender uses a constant timeout.
10. MTP Receiver receives the data packet, and acknowledge it immediately.
11. Both MTP Sender and Receiver print a log file while the file transmission process.
