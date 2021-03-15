package com.techstar.testplat.swagger;

import com.autotest.data.mode.ApiSwagger;
import com.autotest.data.service.impl.ApiSwaggerServiceImpl;
import com.autotest.data.utils.JsonDiff;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.techstar.testplat.common.PageBaseInfo;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengyl
 * @since 2020-09-04
 */
@Service
@Primary
@Transactional
public class ApiSwaggerService{
	private @Autowired SwaggerProcess swaggerProcess;
	private @Autowired ApiSwaggerServiceImpl swaggerService;
	
	public Boolean insertData() {
		Boolean isSuccess=false;
		swaggerProcess.setApiSwaggerList();
		List<ApiSwagger> apiSwaggerList = swaggerProcess.getApiSwaggerList();
//		swaggerService.remove(queryWrapper);
		for (int i=0;i<apiSwaggerList.size();i++) {
			ApiSwagger actual=apiSwaggerList.get(i);
			QueryWrapper<ApiSwagger> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(ApiSwagger::getApiUri, actual.getApiUri());
			ApiSwagger db=swaggerService.getOne(queryWrapper);
			if(db==null) {
				actual.setId(swaggerService.getBaseMapper().selectMaxId()+1);
				swaggerService.save(apiSwaggerList.get(i));
			}else {
				Boolean change=actual.getApiParameters().equals(db.getApiParameters())
						&&actual.getApiMethod().equals(db.getApiMethod())
						&&actual.getApiIn().equals(db.getApiIn())
						&&actual.getApiReponses().equals(db.getApiReponses());
				if(!change) {
					JsonDiff.compareJson(JSONObject.fromObject(actual.getApiParameters()), JSONObject.fromObject(db.getApiParameters()),null);
					UpdateWrapper<ApiSwagger> updateWrapper = new UpdateWrapper<>();
					updateWrapper.lambda().set(ApiSwagger::getApiParameters, actual.getApiParameters())
						.set(ApiSwagger::getApiReponses, actual.getApiReponses())
						.set(ApiSwagger::getApiMethod, actual.getApiMethod())
						.set(ApiSwagger::getApiIn, actual.getApiIn())
						.eq(ApiSwagger::getApiUri, actual.getApiUri());
					swaggerService.update(actual,updateWrapper);
				}
				
			}
		}
		return isSuccess;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageBaseInfo selestAll(long current, long pageSize) {
		IPage<ApiSwagger> page = new Page<>(current, pageSize);
		page=swaggerService.page(page);
		return new PageBaseInfo(page.getRecords(),current,page.getTotal(),pageSize);
	}
}
