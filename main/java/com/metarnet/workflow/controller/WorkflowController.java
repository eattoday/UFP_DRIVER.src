package com.metarnet.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.workflow.service.IWorkflowService;
import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Controller
public class WorkflowController extends BaseController {

    Logger logger = LogManager.getLogger("WorkflowController");

    @Resource
    private IWorkflowService workflowService;

    @RequestMapping(value = "/getMyCompletedTasks.do")
    @ResponseBody
    public void getMyCompletedTasks(HttpServletRequest request, HttpServletResponse response , String accountId , String pager) throws UIException {
        String returnResult = "";

        try {
            pager = URLDecoder.decode(pager , "UTF-8");
            Pager page = PagerPropertyUtils.copy(pager);
            page = workflowService.getMyCompletedTasks(accountId , page);
            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            returnResult = JSON.toJSONString(page , ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        endHandle(request, response, returnResult, "");
    }

    @RequestMapping(value = "/upBusiInfoByRoot.do")
    @ResponseBody
    public void updateBusiInfoByRoot(HttpServletRequest request, HttpServletResponse response , TaskInstance taskInstance) throws UIException {
        String returnResult = "true";

        try {
            workflowService.updateBusiInfoByRoot(taskInstance);
        } catch (ServiceException e) {
            returnResult = "false";
            e.printStackTrace();
        }

        endHandle(request, response, returnResult, "");
    }
}
