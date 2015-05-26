#!/bin/bash
java mtp_receiver 6666 file.txt
java mtp_sender 127.0.0.1 6666 test2.txt 400 100 100 0.4 25
