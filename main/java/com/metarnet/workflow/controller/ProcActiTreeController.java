package com.metarnet.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.eos.workflow.data.WFActivityDefine;
import com.eos.workflow.data.WFProcessDefine;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.controller.BaseController;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.exception.UIException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.core.common.model.TreeNodeVo;
import com.metarnet.core.common.utils.PagerPropertyUtils;
import com.metarnet.core.common.workflow.ProcessModel;
import com.metarnet.workflow.service.IWorkflowService;
import com.primeton.bps.data.WFBizCatalog;
import com.unicom.ucloud.workflow.objects.TaskInstance;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31/0031.
 */
@Controller
public class ProcActiTreeController extends BaseController {

    Logger logger = LogManager.getLogger("CatalogController");


    @RequestMapping(value = "/queryProcActiTreeNodes.do")
    @ResponseBody
    public void queryProcActiTreeNodes(HttpServletRequest request, HttpServletResponse response , String tenantId , String id , String type) throws UIException {
        String returnResult = "";

        try {
            if(id == null){
                returnResult = queryCatalogs(tenantId);
            } else if("0".equals(type)){
                returnResult = queryProcesses(tenantId , id);
            } else if("1".equals(type)){
                returnResult = queryActivities(tenantId, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        endHandle(request, response, returnResult, "");
    }

    private String queryCatalogs(String tenantId) throws UIException {
        String returnResult = "";

        try {
            List<TreeNodeVo> treeNodeVos = new ArrayList<TreeNodeVo>();
            List<WFBizCatalog> list = WorkflowAdapter.queryCatalogs(tenantId);

            for(WFBizCatalog wfBizCatalog : list){
                TreeNodeVo treeNodeVo = new TreeNodeVo();
                treeNodeVo.setId(wfBizCatalog.getCatalogUUID());
                treeNodeVo.setLabel(wfBizCatalog.getCatalogName());
                treeNodeVo.setNocheck(true);
                treeNodeVo.setType(0);
                treeNodeVo.setIsParent(true);
                treeNodeVos.add(treeNodeVo);
            }

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            returnResult = JSON.toJSONString(treeNodeVos , ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }

    private String queryProcesses(String tenantId , String catalogUUID) throws UIException {
        String returnResult = "";

        try {
            List<TreeNodeVo> treeNodeVos = new ArrayList<TreeNodeVo>();
            JSONObject processModeLists = WorkflowAdapter.getProcessModeLists(tenantId);
            net.sf.json.JSONArray data = processModeLists.getJSONArray("data");

            for(Object object : data){
                TreeNodeVo treeNodeVo = new TreeNodeVo();
                JSONObject jsonObject=(JSONObject) object;
                treeNodeVo.setId(jsonObject.getString("id") + "");
                treeNodeVo.setLabel(jsonObject.getString("name"));
                treeNodeVo.setNocheck(false);
                treeNodeVo.setType(1);
//                treeNodeVo.setIsParent(true);
                treeNodeVo.setCode(jsonObject.getString("key"));
                treeNodeVos.add(treeNodeVo);
            }

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            returnResult = JSON.toJSONString(treeNodeVos , ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }

    private String queryActivities(String tenantId , String processDefId) throws UIException {
        String returnResult = "";

        try {
            List<TreeNodeVo> treeNodeVos = new ArrayList<TreeNodeVo>();
            List<WFActivityDefine> list = WorkflowAdapter.queryActivitiesOfProcess(tenantId, processDefId);

            for(WFActivityDefine wfActivityDefine : list){
                TreeNodeVo treeNodeVo = new TreeNodeVo();
                treeNodeVo.setId(wfActivityDefine.getId());
                treeNodeVo.setLabel(wfActivityDefine.getName());
                treeNodeVo.setType(2);
                treeNodeVos.add(treeNodeVo);
            }

            SerializeConfig ser = new SerializeConfig();
            ser.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
            ser.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

            returnResult = JSON.toJSONString(treeNodeVos , ser, SerializerFeature.WriteNullListAsEmpty);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }

}
