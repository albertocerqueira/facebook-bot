package facebook.bot.cassandra;

import static facebook.bot.cassandra.Constants.KEYSPACE;
import static facebook.bot.cassandra.Constants.PORT;
import static facebook.bot.cassandra.Constants.HOST;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

//simple convenience class to wrap connections, just to reduce repeat code
public class Connector {
	
	TTransport tr = new TSocket(HOST, PORT);
	
	//returns a new connection to our keyspace
	public Cassandra.Client connect() throws TTransportException, TException, InvalidRequestException {
		TFramedTransport tf = new TFramedTransport(tr);
		TProtocol proto = new TBinaryProtocol(tf);
		Cassandra.Client client = new Cassandra.Client(proto);
		tr.open();
		client.set_keyspace(KEYSPACE);
		return client;
	}
	
	public void close() {
		tr.close();
	}
}