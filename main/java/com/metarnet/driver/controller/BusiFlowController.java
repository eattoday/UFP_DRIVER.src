package com.metarnet.driver.controller;

import com.alibaba.fastjson.JSONObject;
import com.eos.workflow.data.WFActivityDefine;
import com.eos.workflow.data.WFProcessDefine;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.utils.HttpClientUtil;
import com.metarnet.core.common.workflow.ActivityDef;
import com.metarnet.driver.model.BusiFlowEntity;
import com.metarnet.driver.model.CommonEntity;
import com.metarnet.driver.service.IBusiFlowService;
import com.metarnet.driver.service.IBusiFlowService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class BusiFlowController extends CommonController {

    Logger logger = LogManager.getLogger("ComponentForm");

    @Resource
    private IBusiFlowService busiFlowService;

    @RequestMapping(value = "/busiFlowInit.do")
    @ResponseBody
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response , String tenantId) throws UIException {

        return new ModelAndView(new InternalResourceView("/base/page/publishLaunch.jsp"));

    }

    @RequestMapping(value = "/busiFlowSave.do")
    @ResponseBody
    public void save(HttpServletRequest request, HttpServletResponse response , BusiFlowEntity busiFlowEntity) throws UIException {

        BusiFlowEntity tmpFlow = new BusiFlowEntity();
        tmpFlow.setName(busiFlowEntity.getName());

        if("".equals(busiFlowEntity.getSpecialtyIDs()) || busiFlowEntity.getSpecialtyIDs() == null){
            busiFlowEntity.setSpecialtyIDs("2");
            busiFlowEntity.setSpecialtyNames("全专业");
        }

        JSONObject jsonObject = new JSONObject();

        List<CommonEntity> list = null;

        Boolean isExsit = false;

        try {
            list = busiFlowService.query(tmpFlow);
            if(list != null && list.size() > 0){
                if(!list.get(0).getId().equals(busiFlowEntity.getId())){
                    isExsit = true;
                }
            }
        } catch (ServiceException e) {
            logger.info("检查重复实体报错\n" + e.getLocalizedMessage());
        }

        if(!isExsit){
            UserEntity userEntity = getUserEntity(request);

            try {
                busiFlowService.save(busiFlowEntity, userEntity);

                //调用PMOS系统保存流程信息与节点信息
                //临时解决方案
                busiFlowService.savePmosInfo(busiFlowEntity , userEntity);


                jsonObject.put("success" , true);
            } catch (ServiceException e) {
                jsonObject.put("success", false);
            }
        } else {
            jsonObject.put("success", false);
            jsonObject.put("msg" , "名称已存在");
        }

        endHandle(request , response , jsonObject , null);

    }

    @RequestMapping(value = "/busiFlowFindById.do")
    @ResponseBody
    public void findById(HttpServletRequest request, HttpServletResponse response , BusiFlowEntity busiFlowEntity) throws UIException {

        super.findById(request , response , busiFlowEntity);

    }

    @RequestMapping(value = "/busiFlowQuery.do")
    @ResponseBody
    public void query(HttpServletRequest request, HttpServletResponse response , BusiFlowEntity busiFlowEntity , String dtGridPager) throws UIException {

        super.query(request , response , busiFlowEntity , dtGridPager);

    }

    @RequestMapping(value = "/busiFlowDelete.do")
    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response , BusiFlowEntity busiFlowEntity) throws UIException {

        super.delete(request , response , busiFlowEntity);

    }

}
