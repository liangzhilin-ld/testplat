package com.techstar.testplat.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 接口返回结果封装类。
 * @Author: cby
 * @Date 2019/7/18
 */
@ApiModel("返回结果")
@Setter
@Getter
@NoArgsConstructor
public class Result<T> implements Serializable{
	
	private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "返回码")
	private int code;
    @ApiModelProperty(value = "返回信息")
    private String msg="success";
    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(CodeMsg codeMsg){
        this.code=codeMsg.getCode();
        this.msg=codeMsg.getMsg();
    }

    public Result(CodeMsg codeMsg, T data){
        this.code=codeMsg.getCode();
        this.msg=codeMsg.getMsg();
        this.data=data;
    }

    @SuppressWarnings("rawtypes")
	public Result success() {
        return  new Result(CodeMsg.SUCCESS);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Result success(Object data) {
    	return  new Result(CodeMsg.SUCCESS,data);
    }

    @SuppressWarnings("rawtypes")
	public Result fail(CodeMsg codeMsg) {
    	return new Result(codeMsg);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result setSuccess(Object data) {
    	return  new Result(CodeMsg.SUCCESS,data);
    }
    @SuppressWarnings("rawtypes")
	public static Result createfail(CodeMsg codeMsg) {
		Result result=new Result();
        result.setCode(codeMsg.getCode());
        result.setMsg(codeMsg.getMsg());
        return result;
    }

    //spring 参数绑定错误处理
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Result deployBindingErr(BindingResult bindingResult){
        List<String> bindingErrorInfoList=new ArrayList<String>();
        bindingResult.getAllErrors().stream().forEach(
                objectError -> {
                    bindingErrorInfoList.add(objectError.getDefaultMessage());
                }
        );
        return new Result(CodeMsg.PARAMS_BINDING_ERROR,bindingErrorInfoList);
    }
}
