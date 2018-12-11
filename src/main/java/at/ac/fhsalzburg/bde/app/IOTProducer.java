package at.ac.fhsalzburg.bde.app;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class IOTProducer {
	static Random sensorRandom;

	private static LinkedList<UUID> sensorIDs;

	public final static String TOPIC = "iottopic";
	public final static String BOOTSTRAP_SERVERS = "localhost:9092";
	private final static String CLIENTID = "IOTProducer";

	private static String selectSensor() {
		if (sensorRandom == null) {
			sensorRandom = new Random(System.currentTimeMillis());
		}
		if (sensorIDs == null) {
			sensorIDs = new LinkedList<UUID>();
			for (int i = 0; i < 10; i++)
				sensorIDs.add(UUID.randomUUID());
		}
		return sensorIDs.get(sensorRandom.nextInt(sensorIDs.size() - 1)).toString();
	}

	private static String getSensorValue() {
		String sensorValue = selectSensor() + "," + sensorRandom.nextDouble() * sensorRandom.nextInt(10);

		return sensorValue;
	}

	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENTID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
				LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
				
		props.put("acks", "all");
		props.put("delivery.timeout.ms", 40000);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		
		return new KafkaProducer<>(props);
	}

	public static void main(String[] args) throws InterruptedException {
		int sendMessageCount = 10;

		if (args.length == 1) {
			try {
				sendMessageCount = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// System.err.println("Argument" + args[0] + " must be an
				// integer.");
			}
		}

		final Producer<Long, String> producer = createProducer();
		final long time = System.currentTimeMillis();

		System.out.println("start sending...");
		ProducerRecord<Long, String> record;
		
		for (long index = time; index < time + sendMessageCount; index++) {
			String sensorValue = getSensorValue();
			System.out.printf("offset = %d, key = %s, value = %s\n",
					index,
					TOPIC,						
					sensorValue);
			
			record = new ProducerRecord<Long, String>(TOPIC, index, sensorValue);
			
			producer.send(record);
			Thread.sleep(500L);
		}

		//System.out.println("flushing ...");
		//producer.flush();
		
		System.out.println("closing ...");
		producer.close();
		
		System.out.println("done.");
	}
}
