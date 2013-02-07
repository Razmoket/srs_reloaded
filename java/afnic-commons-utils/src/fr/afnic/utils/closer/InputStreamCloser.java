/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils.closer;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamCloser implements ICloser {

    private final InputStream inputStream;

    InputStreamCloser(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }

}
