package com.techstar.testplat.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techstar.testplat.config.TestPlatProperties;
import com.techstar.testplat.web.bean.UserAuth;
import com.techstar.testplat.web.utils.HttpHelper;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.apachecommons.CommonsLog;
import javax.annotation.PostConstruct;
@Api(tags = "用户权限")
@Validated
@RestController
@RequestMapping("login")
public class Login {
	private @Autowired TestPlatProperties pro;
	
    @ApiOperation(value = "g接口")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("g")//
	public String g() {
		String res=new HttpHelper().get(pro.getLoginHost()+"/api/auth/g");
		return res;
	}
    
    @ApiOperation(value = "image接口")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @GetMapping("image")//
	public String image() {
		String res=new HttpHelper().get(pro.getLoginHost()+"/api/auth/code/image");
		return res;
	}
    
    @ApiOperation(value = "pw接口")
    @ApiResponses({@ApiResponse(code = 200, message = "ResultMsg"),})
    @PostMapping("pw")//
	public String pw(@RequestBody UserAuth mpa) {
    	String jstr= JSONUtil.toJsonStr(mpa);
		String res=new HttpHelper().post(pro.getLoginHost()+"/api/auth/login/pw",jstr);
		return res;
	}
    
}
