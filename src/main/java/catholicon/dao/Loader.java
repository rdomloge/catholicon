package catholicon.dao;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import catholicon.domain.Login;
import catholicon.ex.DaoException;

public class Loader {
	
	public static String load(String url) throws DaoException {
		HttpMethod method = new GetMethod(url);
		
		try {
			HttpClient client = new HttpClient();
			client.executeMethod(method);
			byte[] responseBody = method.getResponseBody();
			int statusCode = method.getStatusCode();
			if(statusCode != 200) throw new DaoException("Server responded with a bad code: "+statusCode);
			String page = new String(responseBody);
			
			return page;
		} 
		catch (IOException e) {
			throw new DaoException(e);
		} 
		finally {
			method.releaseConnection();
		}
	}

	public static String sendLogin(String url, Login login) throws DaoException {
		HttpMethod method = new PostMethod(url);
		method.addRequestHeader("Username", login.getName());
		method.addRequestHeader("Password", login.getPassword());
		
		try {
			HttpClient client = new HttpClient();
			client.executeMethod(method);
			byte[] responseBody = method.getResponseBody();
			int statusCode = method.getStatusCode();
			if(statusCode != 200) throw new DaoException("Server responded with a bad code: "+statusCode);
			String page = new String(responseBody);
			
			return page;
		} 
		catch (IOException e) {
			throw new DaoException(e);
		} 
		finally {
			method.releaseConnection();
		}
	}
}
