package kg.apc.perfmon.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author undera
 */
class UDPTransport extends AbstractTransport {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private final DatagramChannel channel;

    public UDPTransport(SocketAddress addr) throws IOException {
        super(addr);
        channel = DatagramChannel.open();
        channel.socket().setSoTimeout(1000);
        channel.connect(addr);
    }

    public void disconnect() {
        super.disconnect();

        try {
            channel.close();
        } catch (IOException ex) {
            log.error("Problems closing UDP connection", ex);
        }
    }

    public void writeln(String line) throws IOException {
        channel.write(ByteBuffer.wrap(line.concat("\n").getBytes()));
    }

    public String readln() {
        ByteBuffer buf = ByteBuffer.allocateDirect(4096); // FIXME: magic constants are bad

        try {
            channel.read(buf);
            buf.flip();
            return readln(buf);
        } catch (IOException e) {
            return "";
        }
    }
}
