# Kafka Producer Consumer Example

Illustrates a simple example how to produce and consume messages.

## Pre-Install

 * Kafka
 * maven
 * Java >=8 (best: Openjdk)
 * IDE (e.g. Eclipse, ...)

## Before running the start scripts

Before executing you need to add kafka and zookeeper to your OS hosts-file. Here is a list of hosts-file locations according to your operating systems

* **Mac OS:** /private/etc/hosts
* **Linux:** /etc/hosts
* **Windows:** C:\\Windows\\System32\\drivers\\etc\\hosts

The format may vary a little but usually a new host with its hostname is defined using it's **ip** and the desired **hostname**. Thus your host file needs to have the following entries:

```
127.0.0.1  kafka
127.0.0.1  zookeeper
```

## Use the Applications

To build the project execute:

```bash
mvn clean install
```

To run a consumer:

```bash
mvn exec:java@consumer
```

To run the producer:

```bash
mvn exec:java@producer
```

## Helpers

 * [Consumer API Reference](https://kafka.apache.org/21/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html)
 * [Producer API Reference](https://kafka.apache.org/21/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html)
 * [Overall API Reference](https://kafka.apache.org/21/javadoc/)
 
