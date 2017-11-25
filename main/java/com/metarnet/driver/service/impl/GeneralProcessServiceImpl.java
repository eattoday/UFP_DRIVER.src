package com.metarnet.driver.service.impl;

import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.GeneralInfoModel;
import com.metarnet.core.common.service.IWorkflowBaseService;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.model.GeneralProcessEntity;
import com.metarnet.driver.service.IGeneralProcessService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
//import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16/0016.
 */
@Service()
public class GeneralProcessServiceImpl implements IGeneralProcessService {

    @Autowired
    private IBaseDAO baseDAO;

    @Resource
    private IWorkflowBaseService workflowBaseService;

    @Override
    public void saveGeneralInfo(GeneralProcessEntity generalProcessEntity, TaskInstance taskInstance, UserEntity userEntity) throws ServiceException {

        workflowBaseService.setGeneralInfo(generalProcessEntity, taskInstance, userEntity);
        generalProcessEntity.setRootProInstId(taskInstance.getRootProcessInstId());
        generalProcessEntity.setProcessInstId(taskInstance.getProcessInstID());
        generalProcessEntity.setTaskInstId(taskInstance.getTaskInstID());
        generalProcessEntity.setActivityInstName(taskInstance.getActivityInstName());
        try {
            baseDAO.saveOrUpdate(generalProcessEntity, userEntity);
        } catch (DAOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<GeneralInfoModel> findGeneralInfoByProcessInstID(String processInstID) throws ServiceException {
        try {
            return baseDAO.find("from GeneralInfoModel where processInstId=? order by objectId" , new Object[]{processInstID});
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<GeneralInfoModel> findGeneralInfoByProcessInstIDUp(String processInstID) throws ServiceException {
        List<GeneralInfoModel> currLevelList = findGeneralInfoByProcessInstID(processInstID);
        if(currLevelList != null && currLevelList.size() > 0){
            GeneralInfoModel first = currLevelList.get(0);
            if( !"0".equals(first.getParentProInstId()) && first.getParentProInstId() != null && !"".equals(first.getParentProInstId())){
                List<GeneralInfoModel> parLevelList = findGeneralInfoByProcessInstIDUp(first.getParentProInstId());
                currLevelList.addAll(parLevelList);
            }
        }
        return currLevelList;
    }
}
