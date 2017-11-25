package com.metarnet.driver.dao;

import com.metarnet.core.common.exception.DAOException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Administrator on 2016/11/1/0001.
 */
public interface IProDao {

    /**
     * 功能: 通过调用HibernateTemplate的save方法实现 实现流程： 1. 参入参数调用HibernateTemplate的save方法实现
     *
     * @param entities 实体对象
     * @return Serializable 对象
     * @throws com.metarnet.core.common.exception.DAOException 抛出数据访问异常
     */
    public void saveOrUpdateAll(Collection entities)throws DAOException;

    /**
     * 通用的调用原生SQL
     *
     * @param sql sql语句
     * @return List
     * @throws java.sql.SQLException
     * @throws IllegalStateException
     * @throws org.hibernate.HibernateException
     * @throws org.springframework.dao.DataAccessResourceFailureException
     */
    public int executeSql(String sql) throws DAOException;
}
