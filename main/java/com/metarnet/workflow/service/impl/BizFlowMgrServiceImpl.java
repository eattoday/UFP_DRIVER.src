package com.metarnet.workflow.service.impl;

import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.workflow.service.IBizFlowMgrService;
import com.primeton.bps.data.WFBizProcessDef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/16/0016.
 */
@Service
public class BizFlowMgrServiceImpl implements IBizFlowMgrService {

    Logger logger = LogManager.getLogger("BizFlowMgrServiceImpl");

    @Resource
    private IBaseDAO baseDAO;


    @Override
    public void updateProcessDefTmp(WFBizProcessDef processDef) throws ServiceException {

        StringBuffer sqlBuffer = new StringBuffer("update wfprocessdefinetemp set ");

        if(processDef.getProcessDefCHName() != null && !"".equals(processDef.getProcessDefCHName())){
            sqlBuffer.append("processchname = '");
            sqlBuffer.append(processDef.getProcessDefCHName());
            sqlBuffer.append("',");
        }

        if(processDef.getProcessDefName() != null && !"".equals(processDef.getProcessDefName())){
            sqlBuffer.append("processdefname = '");
            sqlBuffer.append(processDef.getProcessDefName());
            sqlBuffer.append("',");
        }

        if(processDef.getVersionSign() != null && !"".equals(processDef.getVersionSign())){
            sqlBuffer.append("versionsign = '");
            sqlBuffer.append(processDef.getVersionSign());
            sqlBuffer.append("',");
        }

        if(processDef.getDescription() != null && !"".equals(processDef.getDescription())){
            sqlBuffer.append("description = '");
            sqlBuffer.append(processDef.getDescription());
            sqlBuffer.append("',");
        }

        if(processDef.getExtend2() != null){
            sqlBuffer.append("extend2 = '");
            sqlBuffer.append(processDef.getExtend2());
            sqlBuffer.append("',");
        }

        if(processDef.getExtend3() != null){
            sqlBuffer.append("extend3 = '");
            sqlBuffer.append(processDef.getExtend3());
            sqlBuffer.append("',");
        }

        if(processDef.getExtend4() != null){
            sqlBuffer.append("extend4 = '");
            sqlBuffer.append(processDef.getExtend4());
            sqlBuffer.append("',");
        }

        String sql = sqlBuffer.toString();

        if(sql.endsWith(",")){
            sql = sql.substring(0 , sql.length() - 1);
        }

        sql += " where tempprocessdefid = " + processDef.getProcessDefID();

        try {
            logger.info(sql);
            baseDAO.executeSql(sql);
        } catch (DAOException e) {
            logger.info("更新流程定义信息报错\n");
            logger.info(e.getMessage());
        }
    }

    @Override
    public void updateProcessDef(WFBizProcessDef processDef) throws ServiceException {

        StringBuffer sqlBuffer = new StringBuffer("update wfprocessdefine set ");

        if(processDef.getVersionSign() != null && !"".equals(processDef.getVersionSign())){
            sqlBuffer.append("versionsign = '");
            sqlBuffer.append(processDef.getVersionSign());
            sqlBuffer.append("',");
        }

        String sql = sqlBuffer.toString();

        if(sql.endsWith(",")){
            sql = sql.substring(0 , sql.length() - 1);
        }

        sql += " where processdefname = '" + processDef.getProcessDefName() + "'";

        try {
            logger.info(sql);
            baseDAO.executeSql(sql);
        } catch (DAOException e) {
            logger.info("更新流程定义信息报错\n");
            logger.info(e.getMessage());
        }
    }

    @Override
    public Map getProcessDefById(String processDefId) throws ServiceException {

        Map map = new HashMap();

        String sql = "select extend2 , extend3 , extend4 from wfprocessdefinetemp where tempprocessdefid = ?";
        try {
            logger.info(sql);
            List list = baseDAO.findNativeSQL(sql , new Object[]{processDefId});
            if(list != null && list.size() > 0){
                map = (Map) list.get(0);
            }
        } catch (DAOException e) {
            logger.info("更新流程定义信息报错\n");
            logger.info(e.getMessage());
        }

        return map;
    }
}
