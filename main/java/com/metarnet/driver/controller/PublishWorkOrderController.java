package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSONObject;
import com.eos.workflow.data.WFProcessDefine;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.driver.model.PublishWorkOrderEntity;
import com.metarnet.driver.service.IPublishWorkOrderService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10/0010.
 */
@Controller
public class PublishWorkOrderController extends CommonController{

    @Resource
    private IPublishWorkOrderService publishWorkOrderService;


    @RequestMapping(value = "/toLaunch.do")
    @ResponseBody
    public ModelAndView toLaunch(HttpServletRequest request, HttpServletResponse response , Long id) throws UIException {
        PublishWorkOrderEntity launchWorkOrderEntity = new PublishWorkOrderEntity();
        launchWorkOrderEntity.setId(id);
        try {
            launchWorkOrderEntity = (PublishWorkOrderEntity) publishWorkOrderService.findById(launchWorkOrderEntity);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return new ModelAndView(new InternalResourceView("/base/page/launch.jsp")).addObject("launchWorkOrderEntity" , launchWorkOrderEntity);
    }

    @RequestMapping(value = "/publishhWorkOrderController.do", params = "method=init")
    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException {
        PublishWorkOrderEntity launchWorkOrderEntity = new PublishWorkOrderEntity();

        List<WFProcessDefine> list = WorkflowAdapter.queryProcessDefByBizCatalogUUID(tenantId);

        return new ModelAndView(new InternalResourceView("/base/page/publishLaunch.jsp")).addObject("list" , list);
    }

    @RequestMapping(value = "/publishhWorkOrderController.do" , params = "method=save")
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , PublishWorkOrderEntity launchWorkOrderEntity) throws UIException {

        String formName = launchWorkOrderEntity.getCustomFormName();
        int split = formName.indexOf("：");
        launchWorkOrderEntity.setCustomFormName(formName.substring(split + 1 , formName.length()));
        super.save(request , response , launchWorkOrderEntity);

    }

    @RequestMapping(value = "/publishhWorkOrderController.do" , params = "method=getList")
    @ResponseBody
    public void getList(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException
    {
        JSONObject jsonObject = new JSONObject();
        try {

            List list=publishWorkOrderService.getPublishWorkOrderList(tenantId);

            jsonObject.put("datas" , list);
        } catch (Exception e) {
            jsonObject.put("datas" , null);
            e.printStackTrace();
        }
        endHandle(request, response, jsonObject, "");
    }



}
