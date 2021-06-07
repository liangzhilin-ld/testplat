package com.techstar.testplat.web.utils;

import java.util.HashMap;
import java.util.Map;

import com.techstar.testplat.config.TestPlatProperties;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

public class HttpHelper {
	String uri;
	String env;
	int timeout=10000;
	TestPlatProperties myProperties=SpringContextUtil.getBean(TestPlatProperties.class);
	Map<String, String> myHeader  = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{  
	      put(Header.ACCEPT.toString(), "application/json, text/plain, */*");  
	      put(Header.ACCEPT_ENCODING.toString(), "gzip, deflate");   
	      put(Header.ACCEPT_LANGUAGE.toString(), "zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7"); 
	      put(Header.CONNECTION.toString(), "keep-alive"); 
	      put(Header.CONTENT_TYPE.toString(), "application/json"); 
	      put(Header.USER_AGENT.toString(), "Hutool http"); 
		}
	}; 
	
	public HttpHelper(String uri,String env) {
		this.uri=uri;
		this.env=env;
	}
	public HttpHelper(String uri) {
		this.uri=uri;
	}
	public HttpHelper() {
	}
	public  String get(String url) {
		String result=HttpRequest.get(url)
				.addHeaders(myHeader)
        	    .timeout(this.timeout)//超时，毫秒
        	    .execute().body();
    	return result;
	}
	public  String post(String url,String body) {
		String result=HttpRequest.post(url)
				.addHeaders(myHeader)
        	    .timeout(this.timeout)//超时，毫秒
        	    .body(JSONUtil.parse(body))
        	    .execute().body();
    	return result;
	}
    public <T>  String get(T trig) {
    	String result=HttpRequest.get(myProperties.getJmeterAgentUrl()+this.uri)
				.addHeaders(myHeader)
//        		.header(Header.ACCEPT, "application/json, text/plain, */*")
//        		.header(Header.ACCEPT_ENCODING, "gzip, deflate")
//        		.header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7")
//        		.header(Header.CONNECTION, "keep-alive")
//        		.header(Header.CONTENT_TYPE, "application/json")
//        		.header(Header.USER_AGENT, "Hutool http")
        	    .timeout(this.timeout)//超时，毫秒
        	    .execute().body();
    	return result;
    }
    public <T> String post(T trig) {
    	String url=myProperties.getJmeterAgentUrl()+this.uri;
    	if(env!=null&&env.length()>0)url=url+"?env="+env;
    	String result=HttpRequest.post(url)
    			.addHeaders(myHeader)
        	    .timeout(this.timeout)//超时，毫秒
        	    .body(JSONUtil.parse(trig))
        	    .execute().body();
    	return result;
    }
}
