package com.techstar.testplat.swagger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.alibaba.fastjson.JSONObject;
import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.service.impl.PublicVariableServiceImpl;
import com.autotest.data.utils.JsonUtil;
import com.techstar.testplat.web.utils.HttpHelper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import lombok.extern.apachecommons.CommonsLog;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
@CommonsLog
@Service
public class Swagger2 {
	@Autowired
	private PublicVariableServiceImpl variablempl;
	private List<ApiSwagger> apiSwaggerList= new ArrayList<ApiSwagger>();	
	private LocalDateTime task_time = LocalDateTime.now();	
	
	/**
	 * 获取api信息列表
	 * @return
	 */
	public List<ApiSwagger> getApiSwaggerList(){
		return this.apiSwaggerList;
	}
	
	public void setApiSwaggerList(Integer pid,String url) {
		this.apiSwaggerList.clear();	
		JSONObject swaggerInfo = getHttpResponse(url);
		if(swaggerInfo.isEmpty()||StrUtil.isEmpty(swaggerInfo.getString("paths")))return;
		swaggerInfo=inintResponse(swaggerInfo);
		String title = swaggerInfo.getJSONObject("info").getString("title");
		JSONArray tags = swaggerInfo.getJSONArray("tags");
		JSONObject paths = swaggerInfo.getJSONObject("paths");
		String basePath=swaggerInfo.getString("basePath");
		for(String key: paths.keySet()) {
			for (String method: paths.getJSONObject(key).keySet()) {
				if(method.toUpperCase().equals("HEAD"))continue;
				ApiSwagger apiSwagger = new ApiSwagger();
				apiSwagger.setApiUri(basePath+key);
				apiSwagger.setSwgTitle(title);
				apiSwagger.setProjectId(pid);
				
				apiSwagger.setApiMethod(method.toUpperCase());
				JSONObject info = paths.getJSONObject(key).getJSONObject(method);
				apiSwagger.setApiDesc(info.getString("summary"));
				apiSwagger.setModuleName(getModuleName(info));
				apiSwagger.setModuleDesc(getModuleDesc(tags, info));
				try {
					apiSwagger.setApiParameters(getApiParameters(info,swaggerInfo).toString());
					apiSwagger.setApiReponses(getApiResponse(info,swaggerInfo).toString());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("---异常接口:"+key);
				}
				apiSwagger.setApiIn(getApiIn(info));
				//apiSwagger.setApiReponses(info.getString("responses"));
				apiSwagger.setCreateTime(task_time);
				apiSwagger.setUpdateTime(task_time);
				apiSwaggerList.add(apiSwagger);
			}	
			//apiSwaggerList.add(apiSwagger);
		}
	}
	
	/**
	 * Definitions内容解析
	 * */
	private JSONObject getDefinitions(String jsonpath,JSONObject root) {
		jsonpath=custom_trim(jsonpath).concat(".properties");
		JSONObject jsonb=new JSONObject();
		String properts=JsonUtil.getStringValue(root, jsonpath);
		if(properts==null) {
			String klh=jsonpath.replace("properties", "additionalProperties");
			properts=JsonUtil.getStringValue(root, klh);
			return jsonb;
		}
			
		for (@SuppressWarnings("rawtypes") Map.Entry entry : JSONObject.parseObject(properts).entrySet()) {
			
			String key=(String) entry.getKey();
			JSONObject value=JSONObject.parseObject(entry.getValue().toString());
			String filedType=value.getString("type");			
			if(filedType!=null) {
				switch (filedType) {
				case "array":
					JSONObject item=JSONObject.parseObject(value.getString("items"));	
					JSONArray array=new JSONArray();
					if(item.getString("type")!=null) {
						if(item.getString("type").equals("string")||item.getString("type").equals("object"))
							JsonUtil.setValue(jsonb,"$."+key, String.format(filedType+"[%s]", item.getString("type")));
					}else {
						String refPath=item.getString("$ref");
						boolean pathequal=jsonpath.equals(custom_trim(refPath).concat(".properties"));
						JSONObject json=pathequal?new JSONObject():getDefinitions(refPath,root);
						//JSONObject json=getDefinitions(refPath,root);//可能存在死循环待处理
						if(!json.isEmpty())array.add(json);
						JsonUtil.setValue(jsonb,"$."+key, array);
					}
					break;
				case "string":					
					filedType=changValue(key);
					JsonUtil.setValue(jsonb,"$."+key, filedType);
					break;
				case "integer":
					int keyvalue=0;
					if(key.contains("limit"))
						keyvalue=50;
					if(key.contains("pageNo"))
						keyvalue=1;
					JsonUtil.setValue(jsonb,"$."+key, keyvalue);
					break;
				case "boolean":
					JsonUtil.setValue(jsonb,"$."+key, false);
					break;
				case "object":
					JsonUtil.setValue(jsonb,"$."+key, value.getString("type"));
					break;
				}
  			}else if(value.getString("$ref")!=null){
				String refPath=value.getString("$ref");	
				JsonUtil.setValue(jsonb,"$."+key, getDefinitions(refPath,root));				
			}
		
		}
		return jsonb;
	}

	private JSONObject getApiResponse(JSONObject method,JSONObject root) {
		JSONObject res=method.getJSONObject("responses").getJSONObject("200");
		JSONObject schema=res.getJSONObject("schema");	
		//获取响应体结构
		if(schema!=null) {
			if(schema.getString("$ref")!=null) {
				res=getDefinitions(schema.getString("$ref"), root);
				return res;
			}
			if(schema.getString("items")!=null) {
				String path=schema.getJSONObject("items").getString("$ref");
				if(path!=null)
					res=getDefinitions(path, root);
			}
		}
		return res;
	}
	
	private JSONObject getApiParameters(JSONObject method,JSONObject root) {
		JSONArray paras=method.getJSONArray("parameters");
		JSONObject jsonb=new JSONObject();
		if(paras!=null&&paras.size()>1) {
			for (int i = 1; i < paras.size(); i++) {				
				JSONObject jsonss =JSONObject.parseObject(paras.getString(i));
				String format=jsonss.getString("in");
				if(format!=null&&format.equals("body")) {
					JSONObject schema=JSONObject.parseObject(jsonss.getString("schema"));
					String type=schema.getString("type");
					if(type==null&&schema.getString("$ref")!=null) {
						jsonb=getDefinitions(schema.getString("$ref"),root);
						break;
					}
					if(type.equals("array")) {	
						jsonb=getDefinitions(schema.getJSONObject("items").getString("$ref"),root);
						break;
					}
					if(type.equals("string")) {
						if(JsonUtil.isJSON(jsonss.getString("description")))
							jsonb=JSONObject.parseObject(jsonss.getString("description"));
						break;
					}
				}else if(format!=null&&format.equals("query")){
					
					String key="$."+jsonss.getString("name");//$.step[1].type
					String type=jsonss.getString("type");//可能是array
					if(type.equals("array"))
						type=String.format(type+"[%s]", JsonUtil.getStringValue(jsonss, "$.items.type"));
					JsonUtil.setValue(jsonb,key, type);
				}else {
					//in": "formData"
					String key="$."+jsonss.getString("name");//$.step[1].type
					String type=jsonss.getString("type");
					if(type==null)type=JsonUtil.getStringValue(jsonss, "$.schema.type");
					JsonUtil.setValue(jsonb,key, type);
				}
			}
		}
		return jsonb;
		
	}
	//获取请求内容数据格式类型
	private String getApiIn(JSONObject json) {
		String contentType="query";
		JSONArray paras=json.getJSONArray("parameters");
		if(paras!=null&&paras.size()>1) {
			JSONObject jsonss=JSONObject.parseObject(paras.getString(paras.size()-1));			
			String cType=jsonss.getString("in");
			if(cType!=null)contentType=cType;
		}		
		return contentType;
	}
	/**
	 * 原始swagger json字符串特殊字符处理
	 */
	private JSONObject inintResponse(JSONObject jsob) {
		String key="definitions";
		Set<Entry<String, Object>> entrys=jsob.getJSONObject(key).entrySet();
		JSONObject newjson = JSONObject.parseObject(jsob.toJSONString());
		newjson.remove(key);
		for (Entry<String, Object> entry : entrys) {
			String oldkey=entry.getKey();
			Object value=entry.getValue();
			oldkey=changeCharStr(oldkey,"«/»,");
			JsonUtil.setValue(newjson, "$.definitions."+oldkey, value);
		}
		return newjson;
	}
	

	
	private String getModuleDesc(JSONArray tags,JSONObject info) {
		JSONArray tagList = info.getJSONArray("tags");
		String tagTarget = tagList.getString(0);
		for (int i=0;i<tags.size();i++) {
			JSONObject tagSource = tags.getJSONObject(i);
			if (tagTarget.equals(tagSource.getString("name"))) {
				return tagSource.getString("description");
			}
		}
		return "";
	}
	
	private String getModuleName(JSONObject info) {
		JSONArray tags = info.getJSONArray("tags");
		return tags.getString(0);
	}
	
	private JSONObject getHttpResponse(String swaggerUrl) {
		JSONObject swaggerInfo = new JSONObject();
		try {
        	String result=new HttpHelper().get(swaggerUrl);
            System.out.println(result);
            swaggerInfo =JSONObject.parseObject(result);
        } catch (Exception e) {
        	log.error("swagger url连接地址异常！");
//            e.printStackTrace();
            
        }
		return swaggerInfo;
	}

	//去除特殊字符
	public String changeCharStr(String sourceString,String ch ) {
		sourceString=sourceString.replace(" ", "");
		char[] chars = ch.toCharArray();		
		StringBuffer stringBuffer = new StringBuffer("");
		for (int i = 0; i < sourceString.length(); i++) {
			boolean noExist=true;
			for (char c : chars) {
				if(sourceString.charAt(i)== c) {
					noExist=false;
					break;
				}
			}
			if(noExist)stringBuffer.append(sourceString.charAt(i));
//			if (sourceString.charAt(i) != ch1
//					&&sourceString.charAt(i) != ch2
//					&&sourceString.charAt(i) != ',') {
//				stringBuffer.append(sourceString.charAt(i));
//			}
		}
		return stringBuffer.toString();

		}
	/**
	 * 对原始路径去除特殊字符处理，并转为jsonPath路径表达式
	 * @param str 原始路径
	 * @param c   字符转"."
	 * @return  jsonPath表达式
	 */
	private String custom_trim(String str) {
		try {
			int st = 0;
//			str=changeCharString(str,'«','»')
//					.replace("#", "$")
//					.replace(",","")
//					.replace(".", "")
//					.replace(" ", "");
			str=changeCharStr(str,"«,». ").replace("#", "$");//去除特殊字符替换#为$
			//前2个斜杠转为.号
			char[] chars = str.toCharArray();
	        int len = chars.length;        
	        for (int i=0;i<len;i++) {
				if(chars[i]=='/') {
					chars[i]='.';
					st++;
				}
				if(st==2)break;
			}
	        //删除剩余斜杠('/')
	        return String.valueOf(chars).replace("/", "");
		} catch (Exception e) {
			System.out.println("error:路径处理异常："+str);
			return "";
		}
		
	}
	private String changValue(String str) {
		if(str.toLowerCase().contains("date")||str.toLowerCase().contains("time"))
			str=variablempl.getKeyValue("${__time()}");
		if(str.toLowerCase().contains("email"))
			str=variablempl.getKeyValue("${__email()}");
		if(str.toLowerCase().contains("gender"))
			str=variablempl.getKeyValue("${__gender()}");
		if(str.toLowerCase().contains("idCard"))
			str=variablempl.getKeyValue("${__idCard()}");
		if(str.toLowerCase().contains("mobile"))
			str=variablempl.getKeyValue("${__mobile()}");
		if(str.toLowerCase().contains("name"))
			str=variablempl.getKeyValue("${__name()}");
		return str;
	}
}
