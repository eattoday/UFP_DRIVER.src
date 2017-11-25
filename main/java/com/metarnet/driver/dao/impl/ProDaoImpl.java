package com.metarnet.driver.dao.impl;

import com.metarnet.core.common.exception.DAOException;
import com.metarnet.driver.dao.IProDao;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by Administrator on 2016/11/1/0001.
 */
@Service()
public class ProDaoImpl implements IProDao {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void saveOrUpdateAll(Collection entities) throws DAOException {
        for (Object entity : entities) {
            sessionFactory.getCurrentSession().saveOrUpdate(entity);
        }
    }

    @Override
    public int executeSql(String sql) {
        return sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
    }
}
