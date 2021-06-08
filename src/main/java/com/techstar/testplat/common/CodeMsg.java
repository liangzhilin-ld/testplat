package com.techstar.testplat.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 返回码
 * @Author: cby
 * @Date 2019/7/23
 */
@Getter
@Setter
public class CodeMsg {
    private int code;
    private String msg;
    private transient  boolean hasdetail;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500001, "服务端异常");

    //参数相关
    public static CodeMsg PARAMS_NULL = new CodeMsg(100002, "参数不能为null：%s",true);
    public static CodeMsg PARAMS_INVALID = new CodeMsg(100003, "参数错误。");
    public static CodeMsg PARAMS_INVALID_DETAIL = new CodeMsg(100004, "参数错误：%s。",true);
    public static CodeMsg PARAMS_BINDING_ERROR=new CodeMsg(100005,"参数绑定错误");

    //操作相关
    public static CodeMsg REPEATED_OPERATION=new CodeMsg(110001,"操作无效：重复的操作");
    public static CodeMsg REFRESH_OPERATION=new CodeMsg(110002,"操作无效：不能进行刷新操作");
    public static CodeMsg HAD_REFRESH_OPERATION=new CodeMsg(110003,"操作无效：限制时间内不可重复刷新");

    //权限相关
    public static CodeMsg INVALID_AUTHORIZATION=new CodeMsg(130001,"权限不足：%s。",true);
    public static CodeMsg INVALID_REALNAME_VERIFICATION=new CodeMsg(130002,"权限不足：无效的实名认证");
    public static CodeMsg NOT_LOGIN=new CodeMsg(130003,"权限不足：用户未登录");
    public static CodeMsg NO_DATA_PERMISSION=new CodeMsg(130004,"无数据权限");

    //数据相关
    public static CodeMsg DATA_NOT_FOUND=new CodeMsg(140001,"数据不存在。");
    public static CodeMsg DATA_ACCOUNT_EXISTS=new CodeMsg(140002,"该手机号码已经注册。");

    public static CodeMsg DATA_ACCOUNT_NOTEXISTS=new CodeMsg(101,"该用户无权操作!");

    public CodeMsg(int code,String msg){
        this.code=code;
        this.msg=msg;
        this.hasdetail=false;
    }
    public CodeMsg(int code,String msg,boolean hasdetail){
        this.code=code;
        this.msg=msg;
        this.hasdetail=hasdetail;
    }
    public CodeMsg formatDetail(String details){
        if (hasdetail){
            this.msg=String.format(msg,details);
        }else {
            throw new UnsupportedOperationException("该CodeMsg不支持String format。");
        }
        return this;
    }

    @Override
    public String toString(){
        return "CodeMsg [code="+code+",msg="+msg+"]";
    }
}
