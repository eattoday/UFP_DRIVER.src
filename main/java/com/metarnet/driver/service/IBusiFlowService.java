package com.metarnet.driver.service;


import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.driver.model.BusiFlowEntity;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
public interface IBusiFlowService extends ICommonService{

    public abstract void savePmosInfo(BusiFlowEntity process, UserEntity userEntity) throws ServiceException;
}
