package com.metarnet.driver.service;

import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.driver.model.PublishWorkOrderEntity;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
public interface IPublishWorkOrderService extends ICommonService{

    /**
     * 获取工单列表
     * @return
     */
    List<PublishWorkOrderEntity> getPublishWorkOrderList(String tenantId) throws DAOException;//param保留
}
