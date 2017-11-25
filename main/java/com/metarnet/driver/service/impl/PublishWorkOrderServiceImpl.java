package com.metarnet.driver.service.impl;

import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.driver.model.PublishWorkOrderEntity;
import com.metarnet.driver.service.IPublishWorkOrderService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/11/15/0015.
 */
@Service
public class PublishWorkOrderServiceImpl extends CommonServiceImpl implements IPublishWorkOrderService {

    Logger logger = LogManager.getLogger();

    @Resource
    private IBaseDAO baseDAO;

    @Override
    public List<PublishWorkOrderEntity> getPublishWorkOrderList(String tenantId) throws DAOException {
        String sql="select name,customFormID,customFormName,processModelName,tenantId,id from PublishWorkOrderEntity";
        if(tenantId != null && !"".equals(tenantId)){
            sql +=  " where tenantId='"+tenantId+"'";
        }
        sql += " order by id";
        return baseDAO.find(sql);
    }
}
