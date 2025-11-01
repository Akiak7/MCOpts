package ivorius.mcopts.commands.parameters;

import java.io.IOException;
import java.io.Reader;

/**
 * A lightweight {@link Reader} implementation that keeps track of the cursor position while reading
 * from a {@link String}. This mirrors the behaviour of {@link java.io.StringReader} but exposes the
 * cursor so callers can observe how many characters have been consumed by the parser.
 */
public class TrackingStringReader extends Reader
{
    private String string;
    private int length;
    private int next;
    private int mark;

    public TrackingStringReader(String s)
    {
        this.string = s != null ? s : "";
        this.length = this.string.length();
        this.next = 0;
        this.mark = 0;
    }

    public int getCursor()
    {
        return next;
    }

    private void ensureOpen() throws IOException
    {
        if (string == null)
            throw new IOException("Stream closed");
    }

    @Override
    public int read() throws IOException
    {
        ensureOpen();
        if (next >= length)
            return -1;
        return string.charAt(next++);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {
        ensureOpen();
        if ((off | len | (off + len) | (cbuf.length - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
        if (len == 0)
            return 0;
        if (next >= length)
            return -1;

        int n = Math.min(length - next, len);
        string.getChars(next, next + n, cbuf, off);
        next += n;
        return n;
    }

    @Override
    public long skip(long ns) throws IOException
    {
        ensureOpen();
        if (next >= length)
            return 0;
        long n = Math.min(length - next, ns);
        if (n < 0)
            return 0;
        next += (int) n;
        return n;
    }

    @Override
    public boolean ready() throws IOException
    {
        ensureOpen();
        return true;
    }

    @Override
    public boolean markSupported()
    {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException
    {
        if (readAheadLimit < 0)
            throw new IllegalArgumentException("Read-ahead limit < 0");
        ensureOpen();
        mark = next;
    }

    @Override
    public void reset() throws IOException
    {
        ensureOpen();
        next = mark;
    }

    @Override
    public void close()
    {
        string = null;
        length = 0;
        next = 0;
        mark = 0;
    }
}
