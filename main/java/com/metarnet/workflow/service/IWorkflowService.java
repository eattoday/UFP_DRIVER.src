package com.metarnet.workflow.service;

import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.Pager;
import com.unicom.ucloud.workflow.objects.TaskInstance;

/**
 * Created by Administrator on 2017/1/4/0004.
 */
public interface IWorkflowService {

    public Pager getMyCompletedTasks(String accountId , Pager pager) throws ServiceException;

    public String updateBusiInfoByRoot(TaskInstance taskInstance) throws ServiceException;
}
