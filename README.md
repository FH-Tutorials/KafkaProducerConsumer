# Kafka Producer Consumer Example

Illustrates a simple example how to produce and consume messages.

## Pre-Install

 * Kafka
 * maven
 * Java >=8 (best: Openjdk)
 * IDE (e.g. Eclipse, ...)

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
 