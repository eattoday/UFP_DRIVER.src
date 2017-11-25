package com.metarnet.driver.service;

import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.exception.ServiceException;
//import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.core.common.workflow.TaskInstance;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
public interface IProcessService {

    public void process(JSONObject formDataMap , TaskInstance taskInstance, UserEntity userEntity) throws ServiceException;
}
