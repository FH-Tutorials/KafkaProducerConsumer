package at.ac.fhsalzburg.bde.app;

import java.io.IOException;
import java.time.Duration;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class IOTConsumer {
	private final static String CLIENTID = "IOTConsumer";
	
	public static class KafkaConsumerRunner implements Runnable {
		private final AtomicBoolean closed = new AtomicBoolean(false);
		private final KafkaConsumer<Long, String> consumer;
		

		public KafkaConsumerRunner(KafkaConsumer<Long, String> consumer) {
			this.consumer = consumer;
		}

		public void run() {
			try {				
				System.out.println("|subscribed to " + IOTProducer.TOPIC);
				System.out.println("|waiting for messages...");
				while (!closed.get()) {					
					ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(1000));
					System.out.println("||after poll (enter to exit); records: " + records.count());
					for (ConsumerRecord<Long, String> record : records)
						System.out.printf(
								"offset = %d, key = %s, value = %s\n", 
								record.offset(),
								record.key(),
								record.value());
				}
			} catch (WakeupException e) {
				System.out.println(e);
				// Ignore exception if closing
				if (!closed.get())
					throw e;
			} finally {
				consumer.close();
			}
		}

		// Shutdown hook which can be called from a separate thread
		public void shutdown() {
			closed.set(true);
			consumer.wakeup();
		}
	}

	public static KafkaConsumer<Long, String> createConsumer() {
		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IOTProducer.BOOTSTRAP_SERVERS);		
		props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENTID);
		props.put("group.id", "IOTSample");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		
		//new consumer
		KafkaConsumer<Long, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(IOTProducer.TOPIC));
		return consumer;
	}
	public static void main(String[] args) throws InterruptedException {
		
		// start consuming thread
		KafkaConsumerRunner r = new KafkaConsumerRunner(createConsumer());
		Thread t = new Thread(r);		
		t.start();
		
		System.out.println("press enter to exit");		
		try {
			System.in.read();
		} catch (IOException e) {
			// print error - just in case 
			e.printStackTrace();
		}
		r.shutdown();
		System.out.println("shutting down ...");
		t.join();
		System.out.println("done.");
	}
}
