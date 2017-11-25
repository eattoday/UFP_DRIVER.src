package com.metarnet.workflow.service;

import com.metarnet.core.common.exception.ServiceException;
import com.primeton.bps.data.WFBizProcessDef;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
public interface IBizUnitDeployMgrService {

    public Boolean deploy(String submitType, String deploy, String versionDesc, String selectVersion, WFBizProcessDef processDef , UserEntity userEntity) throws ServiceException;


}
