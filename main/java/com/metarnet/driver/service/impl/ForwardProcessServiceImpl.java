package com.metarnet.driver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.workflow.Participant;
import com.metarnet.core.common.workflow.TaskFilter;
import com.metarnet.core.common.workflow.TaskInstance;
import com.metarnet.driver.service.IProcessService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
//import com.unicom.ucloud.workflow.filters.TaskFilter;
//import com.unicom.ucloud.workflow.objects.Participant;
//import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
@Service(value = "FORWARD")
public class ForwardProcessServiceImpl implements IProcessService {

    @Override
    public void process(JSONObject formDataMap , TaskInstance taskInstance, UserEntity userEntity) throws ServiceException {

        String tenantId = formDataMap.getString("tenantId");

        /**
         * 个性化定制的转办功能，先提交流程，然后转办参与者
         */
        try {
            WorkflowAdapter.submitTask(userEntity.getUserName() + "," + tenantId , taskInstance , null);
        } catch (AdapterException e) {
            e.printStackTrace();
        }

        String taskInstID = "";

        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setProcessInstID(taskInstance.getProcessInstID());
        try {
            List<TaskInstance> list = WorkflowAdapter.getMyWaitingTasks(taskFilter , userEntity.getUserName());
            if(list != null && list.size() > 0){
                taskInstID = list.get(0).getTaskInstID();
            }
        } catch (AdapterException e) {
            e.printStackTrace();
        }

        List<Participant> participants = new ArrayList<Participant>();
        String MAIN_TRANSFER = formDataMap.getString("MAIN_TRANSFER");
        Participant participant = new Participant();
        participant.setParticipantID(MAIN_TRANSFER);
        participant.setParticipantType("1");
        participants.add(participant);
        try {
            WorkflowAdapter.forwardTask(userEntity.getUserName() + "," + tenantId, taskInstID, participants);
        } catch (AdapterException e) {
            e.printStackTrace();
        }
    }
}
