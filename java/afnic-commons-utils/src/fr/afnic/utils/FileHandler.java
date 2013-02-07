package fr.afnic.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileHandler {

    public static ByteBuffer readDataFile(String fileNameAndPath) throws IOException {
        ByteBuffer byteBuffer;

        FileChannel inputChannel = new FileInputStream(fileNameAndPath).getChannel();
        int size = (int) inputChannel.size();
        byteBuffer = ByteBuffer.allocate(size);
        inputChannel.read(byteBuffer);
        byteBuffer.rewind();
        inputChannel.close();

        return byteBuffer;
    }

    public static String readStringFile(String fileNameAndPath) throws IOException {
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = readDataFile(fileNameAndPath);
        CharBuffer charBuffer = charset.decode(byteBuffer);

        return charBuffer.toString();
    }

}
