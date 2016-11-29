package catholicon.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import catholicon.domain.Login;
import catholicon.ex.DaoException;

public class Loader {
	
	private static HttpContext ctx;
	
	static {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie1 = new BasicClientCookie("BDBLUID", "24");
		cookie1.setDomain("bdbl.org.uk");
		cookie1.setPath("/");
		BasicClientCookie cookie2 = new BasicClientCookie("testcookie", "true");
		cookie2.setDomain("bdbl.org.uk");
		cookie2.setPath("/");
		cookieStore.addCookie(cookie1);
		cookieStore.addCookie(cookie2);
		
		ctx = HttpClientContext.create();
		ctx.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	}
	
	private static String streamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(is);
		char[] cbuf = new char[128];
		int read = -1;
		try {
			while( (read = isr.read(cbuf)) != -1) {
				sb.append(cbuf, 0, read);
			}
		} 
		finally {
			isr.close();
			is.close();
		}
		return sb.toString();
	}
	
	public static String load(String url) throws DaoException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		
		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse resp) throws ClientProtocolException, IOException {
//				int statusCode = resp.getStatusLine().getStatusCode();
//				if(statusCode != 200) throw new DaoException("Server responded with a bad code: "+statusCode);
				return streamToString(resp.getEntity().getContent());
			}
		};

		try {
			return client.execute(get, handler, ctx);
		} 
		catch (IOException e) {
			throw new DaoException(e);
		} 
		finally {
			get.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}

	/*
		Username:RDomloge
		Password:Badmuthafucka05
		BrowserName:Google Chrome
		BrowserVersion:46.0.2490.76
		OperatingSystem:Linux 
		BrowserEngine:Web Kit
		BrowserEngineVersion:537.36
	 */
	public static String sendLogin(String url, Login login) throws DaoException {
		CloseableHttpClient client = HttpClients.createDefault();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("Username", login.getName()));
		formparams.add(new BasicNameValuePair("Password", login.getPassword()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		
		ResponseHandler<String> handler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse resp) throws ClientProtocolException, IOException {
				return streamToString(resp.getEntity().getContent());
			}};
		
		try {
			return client.execute(post, handler, ctx);
		} 
		catch (IOException e) {
			throw new DaoException(e);
		} 
		finally {
			post.releaseConnection();
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}
}
