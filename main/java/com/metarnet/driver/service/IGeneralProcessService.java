package com.metarnet.driver.service;

import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.GeneralInfoModel;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.model.GeneralProcessEntity;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
//import com.unicom.ucloud.workflow.objects.TaskInstance;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16/0016.
 */
public interface IGeneralProcessService {

    public void saveGeneralInfo(GeneralProcessEntity generalProcessEntity, TaskInstance taskInstance, UserEntity userEntity) throws ServiceException;

    public List<GeneralInfoModel> findGeneralInfoByProcessInstID(String processInstID) throws ServiceException;

    public List<GeneralInfoModel> findGeneralInfoByProcessInstIDUp(String processInstID) throws ServiceException;

}
