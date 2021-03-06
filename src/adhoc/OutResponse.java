package adhoc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class OutResponse extends HttpServletResponseWrapper
{

	private StringWriter sw;

	public OutResponse(HttpServletResponse response)
	{
		super(response);
		sw = new StringWriter();
	}

	@Override
	public PrintWriter getWriter() throws IOException
	{
		return new PrintWriter(sw);
	}

	public String getContent()
	{
		return sw.toString();
	}

}