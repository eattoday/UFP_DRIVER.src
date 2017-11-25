package com.metarnet.workflow.service;

import com.metarnet.core.common.exception.ServiceException;
import com.primeton.bps.data.WFBizProcessDef;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
public interface IBizFlowMgrService {

    public void updateProcessDefTmp(WFBizProcessDef processDef) throws ServiceException;
    public void updateProcessDef(WFBizProcessDef processDef) throws ServiceException;

    public Map getProcessDefById(String processDefId) throws ServiceException;
}
