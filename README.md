# OS Memory Management Simulator

A Java-based simulation of core memory management techniques found in operating systems. Demonstrates address translation, page tables, TLB caching, and page replacement policies with a focus on clean low-level design.

## Features
- Per-process page tables
- Multilevel paging
- TLB with LRU eviction
- Page replacement strategies (LRU, FIFO, Random)
- Pluggable policy interface
- Centralized logging

## Design
Built using OOP and LLD principles:
- Interface-based extensibility
- Clean separation of concerns
- Encapsulated TLB, MemoryManager, and Process logic

## Run
1. Compile: `javac -d bin src/**/*.java`
2. Run: `java -cp bin your.package.MainClass`

## Author
Soham Khadilkar
