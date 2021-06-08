package com.techstar.testplat.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.testplat.config.TestPlatProperties;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.apachecommons.CommonsLog;

/**
 * 用户token验证
 * @author liangzhilin
 *
 */
//@Component
@CommonsLog
public class AuthInterceptor implements HandlerInterceptor{
	
	private String URI="/api/platform/user/context";
	private @Autowired TestPlatProperties authHost;

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String ff=request.getRequestURI();
        String host=authHost.getLoginHost();
        String result=HttpRequest.post(host.concat(URI))
        		.header(Header.ACCEPT, "application/json, text/plain, */*")
        		.header(Header.ACCEPT_ENCODING, "gzip, deflate")
        		.header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,en;q=0.8,fr;q=0.7")
        		.header(Header.CONNECTION, "keep-alive")
        		.header(Header.CONTENT_TYPE, "application/json")
        		.header(Header.HOST, host)
        		.header(Header.USER_AGENT, "Hutool http")
        		.header("Authorization", token)
        	    .timeout(20000)//超时，毫秒
        	    .execute().body();
        //....处理逻辑
        if(!JSONUtil.isJson(result)||JSONUtil.parseObj(result).containsKey("code")){
        	outPut(response,result);
        	return false;
        }
       // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }
	
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    
    private void outPut(HttpServletResponse response,String msg){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
          writer = response.getWriter();
//          JSONObject outputMSg = new JSONObject();
//          outputMSg.put("code", 500);
//          outputMSg.put("content", "");
//          outputMSg.put("msg", "没有权限");
          writer.print(msg);
        } catch (IOException e){
          log.error("拦截器输出流异常"+e);
        } finally {
          if(writer != null){
            writer.close();
          }
        }
      }
}

