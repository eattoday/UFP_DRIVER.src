package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.AdapterException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.core.common.workflow.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;import com.metarnet.core.common.workflow.*;

/**
 * Created by ght on 2017-10-27 08:58:21
 */
@Controller
public class DemoController extends BaseController {




    @RequestMapping(value = "/demoController.do", params = "method=getWaitings")
    @ResponseBody
    public void getWaitings( HttpServletRequest request, HttpServletResponse response
                             ) throws AdapterException, UIException {
        TaskFilter taskFilter=new TaskFilter();
        taskFilter.setProcessInstID("17650");
        List<TaskInstance> list = WorkflowAdapter.getMyWaitingTasks(taskFilter, "ght");
        endHandle(request, response, JSON.toJSONString(list), "");
    }




    /**
     * 获取分页信息
     */
    private TaskFilter setPageCodition(TaskFilter taskFilter, HttpServletRequest request) {
        String dtGridPager = request.getParameter("dtGridPager");
        int start;// 开始行数
        int length;// 每页大小
        String jobCode = null;
        String jobTitle = null;
        String startCreateDate = null;
        String endCreateDate = null;
        try {
            JSONObject dtGridPagerJson = JSON.parseObject(dtGridPager);
            start = Integer.valueOf((String) dtGridPagerJson.get("startRecord"));
            length = Integer.valueOf((String) dtGridPagerJson.get("pageSize"));

            JSONObject fastQueryParameters = (JSONObject) dtGridPagerJson.get("fastQueryParameters");
            try {
                jobCode = fastQueryParameters.getString("lk_jobCode");
                jobTitle = fastQueryParameters.getString("lk_jobTitle");
                //到单时间查询
                startCreateDate = fastQueryParameters.getString("ge_completionDate");
                endCreateDate = fastQueryParameters.getString("le_completionDate");
            } catch (Exception e) {

            }
            if (jobCode != null && !"".equals(jobCode)) {
                taskFilter.setJobCode(jobCode);
            }
            if (jobTitle != null && !"".equals(jobTitle)) {
                taskFilter.setJobTitle(jobTitle);
            }

        } catch (Exception e) {
            start = 0;
            length = 10;
        }

        PageCondition pageCondition = taskFilter.getPageCondition();
        if (pageCondition == null) {
            pageCondition = new PageCondition();
            pageCondition.setBegin(start);
            pageCondition.setLength(length);
            taskFilter.setPageCondition(pageCondition);
        }

        return taskFilter;
    }


}
