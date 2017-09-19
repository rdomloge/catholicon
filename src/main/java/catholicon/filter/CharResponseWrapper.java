package catholicon.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class CharResponseWrapper extends HttpServletResponseWrapper
{
    private ByteArrayPrintWriter output;
    private boolean usingWriter;

    public CharResponseWrapper(HttpServletResponse response)
    {
        super(response);
        usingWriter = false;
        output = new ByteArrayPrintWriter();
    }

    public byte[] getByteArray()
    {
        return output.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        // will error out, if in use
        if (usingWriter) {
            super.getOutputStream();
        }
        usingWriter = true;
        return output.getStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        // will error out, if in use
        if (usingWriter) {
            super.getWriter();
        }
        usingWriter = true;
        return output.getWriter();
    }

    public String toString()
    {
        return output.toString();
    }
}