/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.io.IOException;
import java.io.Writer;

public class WriterCloser implements ICloser {

    private final Writer writer;

    WriterCloser(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

}
