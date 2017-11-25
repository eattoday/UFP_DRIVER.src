package com.metarnet.driver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.service.IProcessService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
//import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
@Service(value = "APPROVAL")
public class ApprovalProcessServiceImpl implements IProcessService {

    @Override
    public void process(JSONObject formDataMap , TaskInstance taskInstance, UserEntity userEntity) throws ServiceException {
        String approvalResult = formDataMap.getString("approval_result");
        String tenantId = formDataMap.getString("tenantId");
        Map relativeData = new HashMap();
        relativeData.put("approval_result" , approvalResult);
        try {
            WorkflowAdapter.setRelativeData(taskInstance.getProcessInstID(), relativeData, userEntity.getUserName());
            WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId, taskInstance, null);
        } catch (AdapterException e) {
            e.printStackTrace();
        }

    }
}
