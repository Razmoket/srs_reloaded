/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.io.IOException;
import java.io.Reader;

public class ReaderCloser implements ICloser {

    private final Reader reader;

    ReaderCloser(final Reader reader) {
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

}
