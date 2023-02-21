# Chunking_with_Kafka

How chuning Algorithm works: 

Data In Json  -> Data in String -> Array of bytes
Initialization of a chunker function
new Chunker(Id , byte[ ] , size);
size : Size of data can vary from 10 bytes to 2^32 bytes
Id : is used to distinguish between types of data at dechunking, say  accountData will have id = 12 , transactionData will have id = 13 …

 .hasNext()
Add Metadata + divided array in bytes + updates Remaining size of bytes array
METADATA - (13 byte)
ID -> ( mentioned above) (4 byte)
Size -> Size per chunk (4 byte)
Option -> remainingSize in that id is less than “size” ? 1 : 0 (1 byte)
Serial -> increase its value with 1 for next chunks. Required to sort at the other end. (4 byte)	

Data to size “size” bytes
remaningSize - = (size);
Sends newly formed byte array (data + meta) to consumer.

How does dechunking work ?
Initialize a listener whenever it receives a message it follows 2 to 5.
Receives the byte array + metadata
If ID is not present in treemap then initialize a collection(key of map = id) . OtherWise add message to the collection. Same message gets grouped together.
If it is end of the message(from option) , sort the message on serial.
Convert byte array to string and send it to logger and clear map key when message ends.

How is Kafka receiving data ?

Kafka module has 3 Classes :
KafkaConsumerService -> Receives chunked messages and the compile them
KafkaProducerService -> Sends message to KafkaConsumerService
KafkaProducerController -> maps api calls to perform an action.
    	
	Data Flow :
KafkaProducerController is called to give some data.
KafkaProducerController fetches the data from DB and passes it to the chunker function. 
The Chunker function segments the data and adds metadata and calls KafkaProducerService each time a chunk is prepared.
KafkaProducerService sends data to KafkaConsumerService.
KafkaConsumerService has a consumer listening to a topic and consumes the data
Data is continuously getting send to dechunker and when whole data is received merge function is called
Logger will have the whole unchunked data.




Work to be Done :
Acknowledgement to the producer side when whole message is received.
Any way around treeMap at the consumer side.
Adding kafka (.hasNext( ) - > kafka Queue/ Producers)

-----------------------------------------------------------------------------------------------
Zookeeper StartUP
~/kafka_2.13-3.4.0/bin/zookeeper-server-start.sh ~/kafka_2.13-3.4.0/config/zookeeper.properties

Kafka StartUP
~/kafka_2.13-3.4.0/bin/kafka-server-start.sh ~/kafka_2.13-3.4.0/config/server.properties

Consuming from a topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test


