package com.metarnet.driver.service;

import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.Pager;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
public interface ICommonService {

    public Long getSequenceNextValue(Class entityClass) throws ServiceException;

    /**
     * 保存实体，自动赋值用户信息
     * @param object
     * @param userEntity
     * @throws ServiceException
     */
    public abstract void save(Object object, UserEntity userEntity) throws ServiceException;

    /**
     * 通过主键查询实体
     * @param object
     * @return
     * @throws ServiceException
     */
    public abstract Serializable findById(Serializable object) throws ServiceException;

    /**
     * 查询实体
     * @param object
     * @return
     * @throws ServiceException
     */
    public abstract List query(Object object) throws ServiceException;

    /**
     * 针对jquery.dtGrid插件的Pager查询实体
     * @param object
     * @param pager
     * @return
     * @throws ServiceException
     */
    public abstract Pager queryByPager(Object object , Pager pager) throws ServiceException;

    /**
     * 删除实体
     * @param object
     * @throws ServiceException
     */
    public abstract void delete(Object object) throws ServiceException;

}
