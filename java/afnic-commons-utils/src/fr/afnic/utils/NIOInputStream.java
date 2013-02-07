package fr.afnic.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOInputStream extends InputStream {
    private ByteBuffer byteBuffer;
    private final FileChannel inputChannel;
    private int fileSize;
    private int fileSizeLeft;

    /** Creates an uninitialized stream that cannot be used until {@link #setByteBuffer(ByteBuffer)} is called.
     * @throws FileNotFoundException */
    public NIOInputStream(String fileNameAndPath) throws FileNotFoundException {
        this.inputChannel = new FileInputStream(fileNameAndPath).getChannel();
        this.fileSize = -1;
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.inputChannel.close();
        this.byteBuffer = null;
    }

    @Override
    public int read() throws IOException {
        if ((this.byteBuffer == null) || (this.byteBuffer.remaining() == 0)) {
            this.readDataFromFile();
        }
        if (this.byteBuffer.remaining() == 0)
            return 0;
        return this.byteBuffer.get();
    }

    private void readDataFromFile() throws IOException {
        if (this.fileSize == -1) {
            this.fileSize = (int) this.inputChannel.size();
            this.fileSizeLeft = this.fileSize;
        }
        if (this.fileSizeLeft == 0)
            return;
        this.byteBuffer = ByteBuffer.allocate(Math.min(4 * 1024, this.fileSizeLeft));
        int decrease = this.inputChannel.read(this.byteBuffer);
        this.fileSizeLeft -= decrease;
        this.byteBuffer.rewind();
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if ((this.byteBuffer == null) || (this.byteBuffer.remaining() == 0)) {
            this.readDataFromFile();
        }

        int count = Math.min(this.byteBuffer.remaining(), length);
        if (count == 0) return -1;
        this.byteBuffer.get(bytes, offset, length);

        length -= count;
        if (length > 0) {
            int back = this.read(bytes, offset + count, length);
            if (back == -1) {
                back = 0;
            }
            count += back;
        }

        return count;
    }

    @Override
    public int available() throws IOException {
        if (this.byteBuffer == null) {
            return 0;
        }
        return this.byteBuffer.remaining();
    }

}