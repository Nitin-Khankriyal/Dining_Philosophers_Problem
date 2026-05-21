# Dining Philosophers Simulation (Java Swing GUI)

An interactive, multi-threaded Java desktop application that simulates Dijkstra's classic **Dining Philosophers Problem**. The project demonstrates fundamental concurrency concepts, resource allocation, and deadlock prevention techniques in real-time using Java Swing components and `java.util.concurrent` utilities.

## Features

* **Dynamic Scale:** Configure anywhere from 2 to 10 philosophers dynamically through the UI.
* **Deadlock Prevention:** Implements an **asymmetric resource allocation** strategy (even-indexed philosophers pick up the left fork first; odd-indexed philosophers pick up the right fork first) to break the circular-wait condition.
* **Real-Time Visual Logs:** Displays thread state changes (`THINKING`, `HUNGRY`, `EATING`) and precise fork-handling steps.
* **State Snapshot Table:** Appends an organized state table layout to the logger window on every state mutation.
* **Execution Control:** Built-in `Start` and `Stop` mechanics using thread-safe `volatile` boundaries.


## The Concurrency Architecture

The application effectively models shared operating system resources using Java's foundational synchronization mechanics:

1. **Semaphores (`Semaphore[]`):** Each fork is modeled as a binary semaphore with an initial permit of `1`, guaranteeing mutual exclusion so no two adjacent philosophers can hold the same fork simultaneously.
2. **Global Print Monitor (`synchronized(lock)`):** Ensures that the user interface updates and event strings append sequentially without data race corruption or interleaved text lines.
3. **Volatile Run Boundaries (`volatile boolean running`):** Allows immediate, graceful context-loop termination across all running philosopher worker threads when the user clicks the "Stop" button.

---

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
