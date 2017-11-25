package com.metarnet.workflow.service.impl;

import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.data.WFDeployeeOption;
import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.driver.service.IFlowNodeSettingService;
import com.metarnet.workflow.service.IBizFlowMgrService;
import com.metarnet.workflow.service.IBizUnitDeployMgrService;
import com.primeton.bps.component.manager.api.BPSMgrServiceClientFactory;
import com.primeton.bps.component.manager.api.IBPSMgrServiceClient;
import com.primeton.bps.data.WFBizProcessDef;
import com.primeton.workflow.api.WFServiceException;
import com.primeton.workflow.bizresource.manager.api.IWFBizUnitDeployManager;
import com.primeton.workflow.bizresource.manager.model.WFBizResponse;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2017/2/16/0016.
 */
@Service
public class BizUnitDeployMgrServiceImpl implements IBizUnitDeployMgrService {

    Logger logger = LogManager.getLogger("BizUnitDeployMgrServiceImpl");

    @Resource
    private IFlowNodeSettingService flowNodeSettingService;

    @Resource
    IBizFlowMgrService bizFlowMgrService;


    @Override
    public Boolean deploy(String submitType, String deploy, String versionDesc, String selectVersion, WFBizProcessDef processDef, UserEntity userEntity) throws ServiceException {
        BPSServiceClientFactory.getLoginManager().setCurrentUser(userEntity.getUserName(), userEntity.getUserName());
        IWFBizUnitDeployManager wfBizUnitDeployManager = null;
        try {
            wfBizUnitDeployManager = BPSMgrServiceClientFactory.getDefaultClient().getWFBizUnitDeployManager();
        } catch (WFServiceException e) {
            e.printStackTrace();
        }

        List resList = new ArrayList();

        WFDeployeeOption deployeeOption = new WFDeployeeOption();
        if ("1".equals(deploy))
            deployeeOption.setIsPublish(true);
        else {
            deployeeOption.setIsPublish(false);
        }
        if ("2".equals(submitType))
            deployeeOption.setMode("old-version");
        else if ("3".equals(submitType))
            deployeeOption.setMode("new-version");
        else {
            deployeeOption.setMode("default");
        }
        deployeeOption.setVersionDesc(versionDesc);
        deployeeOption.setPackageID("BPS_DEFAULT");
        deployeeOption.setPackageName("BPS_DEFAULT");

        if ((selectVersion == null) || ("1".equals(submitType))) {
            selectVersion = "";
        }
        WFBizResponse response = wfBizUnitDeployManager.putinBizProcess(Long.valueOf(processDef.getProcessDefID()), selectVersion, deployeeOption, resList);
        if (!response.isSuccess()) {
            return false;
        }

        if (response.isSuccess()) {
            WFBizProcessDef publishedProcess = null;
            try {
                publishedProcess = queryPublishedProcess(processDef.getProcessDefName(), BPSMgrServiceClientFactory.getDefaultClient());
            } catch (WFServiceException e) {
                e.printStackTrace();
            }

            //设置extend4为空，表示不存在定时启用
            WFBizProcessDef wfBizProcessDef = new WFBizProcessDef();
            wfBizProcessDef.setProcessDefCHName(processDef.getProcessDefCHName());
            wfBizProcessDef.setProcessDefID(processDef.getProcessDefID());
            wfBizProcessDef.setExtend4("");
            try {
                bizFlowMgrService.updateProcessDefTmp(wfBizProcessDef);
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            try {
                flowNodeSettingService.savePublishSetting(publishedProcess.getProcessDefName(), publishedProcess.getVersionSign(), userEntity, String.valueOf(publishedProcess.getProcessDefID()));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private WFBizProcessDef queryPublishedProcess(String processDefName, IBPSMgrServiceClient mgrClient)
    {
        IWFBizUnitDeployManager wfBizUnitDeployManager = mgrClient.getWFBizUnitDeployManager();
        WFBizResponse response = wfBizUnitDeployManager.getBizProcessesByProcName(processDefName);
        List<WFBizProcessDef> procList = (List)response.getResult();
        if (procList != null) {
            for (WFBizProcessDef def : procList) {
                if (3 == def.getCurrentState()) {
                    return def;
                }
            }
        }
        return new WFBizProcessDef();
    }
}

