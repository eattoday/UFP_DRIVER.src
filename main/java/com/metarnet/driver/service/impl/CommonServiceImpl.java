package com.metarnet.driver.service.impl;

import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.driver.service.ICommonService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23/0023.
 */
@Service(value = "commonServiceImpl")
public class CommonServiceImpl implements ICommonService{

    Logger logger = LogManager.getLogger("CommonService");

    @Resource
    private IBaseDAO baseDAO;

    @Override
    public Long getSequenceNextValue(Class entityClass) throws ServiceException {
        try {
            return baseDAO.getSequenceNextValue(entityClass);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Object object, UserEntity userEntity) throws ServiceException {
        try {
            baseDAO.saveOrUpdate(object , userEntity);
        } catch (DAOException e) {
            logger.error("saveOrUpdate ERROR,\n" + e.getLocalizedMessage());
        }
    }

    @Override
    public Serializable findById(Serializable object) throws ServiceException {
        try {
            return baseDAO.findById(object);
        } catch (DAOException e) {
            logger.error("findById ERROR,\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public List query(Object object) throws ServiceException {
        try {
            return baseDAO.findByExample(object);
        } catch (DAOException e) {
            logger.error("findByExample ERROR,\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Pager queryByPager(Object object, Pager pager) throws ServiceException {
        try {
            return baseDAO.getPagerByHql(object.getClass().getName(), pager);
        } catch (DAOException e) {
            logger.error("getPagerByHql ERROR,\n" + e.getLocalizedMessage());
        }

        return null;
    }

    @Override
    public void delete(Object object) throws ServiceException {
        try {
            baseDAO.delete(object);
        } catch (DAOException e) {
            logger.error("delete ERROR,\n" + e.getLocalizedMessage());
        }
    }
}
