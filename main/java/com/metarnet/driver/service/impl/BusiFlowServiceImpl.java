package com.metarnet.driver.service.impl;

import com.eos.workflow.data.WFActivityDefine;
import com.metarnet.core.common.adapter.WorkflowAdapter;
import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.utils.HttpClientUtil;
import com.metarnet.driver.Constant;
import com.metarnet.driver.model.BusiFlowEntity;
import com.metarnet.driver.model.BusiNodeEntity;
import com.metarnet.driver.service.IBusiFlowService;
import com.metarnet.driver.service.IFlowButtonService;
import com.ucloud.paas.proxy.aaaa.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23/0023.
 */
@Service
public class BusiFlowServiceImpl extends CommonServiceImpl implements IBusiFlowService {

    @Resource
    private IBaseDAO baseDAO;

    @Override
    public void savePmosInfo(BusiFlowEntity busiFlowEntity, UserEntity userEntity) throws ServiceException {
        String[] processDefCodes = busiFlowEntity.getProcessDefCodes().split(",");

//                String requestURL = "http://10.110.2.84:10000/PMOS/processController.do";
        String requestURL = Constant.PMOS_URL + "/processController.do";
        String sendData = "method=saveProcess" +
                "&processID=" + busiFlowEntity.getId() +
                "&processCode=" + busiFlowEntity.getId() +
                "&processName=" + java.net.URLEncoder.encode(busiFlowEntity.getName()) +
                "&specialtyNames=" + busiFlowEntity.getSpecialtyIDs() +
                "&globalUniqueID=" + userEntity.getAttribute1();
        HttpClientUtil.sendPostRequest(requestURL, sendData, true);

        List<BusiNodeEntity> nodeList = saveBusiNode(busiFlowEntity , userEntity , processDefCodes[0]);

        for(BusiNodeEntity busiNodeEntity : nodeList){
//                        requestURL = "http://10.110.2.84:10000/PMOS/nodeController.do";
            requestURL = Constant.PMOS_URL + "/nodeController.do";
            sendData = "method=saveNode" +
                    "&proNodeID=" + busiNodeEntity.getId() +
                    "&processName=" + java.net.URLEncoder.encode(busiNodeEntity.getProcessName() + "," + busiFlowEntity.getId()) +
                    "&proNodeName=" + java.net.URLEncoder.encode(busiNodeEntity.getProNodeName()) +
                    "&proNodeEnName=" + busiNodeEntity.getProcessEnName() +
                    "&proNodeCode=" + busiNodeEntity.getProNodeCode() +
                    "&specialtyNames=" + busiFlowEntity.getSpecialtyIDs() +
                    "&deleteFlag=" + busiNodeEntity.getDeletedFlag() +
                    "&globalUniqueID=" + userEntity.getAttribute1();
            System.out.println(sendData);
            HttpClientUtil.sendPostRequest(requestURL , sendData , true);
        }

    }

    List<BusiNodeEntity> saveBusiNode(BusiFlowEntity busiFlowEntity, UserEntity userEntity , String processCode) throws ServiceException {

        List<BusiNodeEntity> updateList = new ArrayList<BusiNodeEntity>();

        BusiNodeEntity busiNodeEntity = new BusiNodeEntity();
        busiNodeEntity.setProcessId(busiFlowEntity.getId());
        List<BusiNodeEntity> currList = query(busiNodeEntity);

        String[] processDefIDs = busiFlowEntity.getProcessDefIDs().split(",");

        for(int i = 0 ; i < processDefIDs.length ; i++) {
            String processDefID = processDefIDs[i];
            List<WFActivityDefine> activityDefs = WorkflowAdapter.queryActivitiesOfProcess(busiFlowEntity.getTenantId(), processDefID);

            for(int j = 0 ; j < activityDefs.size() ; j++) {

                WFActivityDefine wfActivityDefine = activityDefs.get(j);

                if (!"manual".equals(wfActivityDefine.getType())) {
                    continue;
                }

                String proNodeName = wfActivityDefine.getName();
                String proNodeCode = wfActivityDefine.getId();

                Boolean exsit = false;

                Iterator<BusiNodeEntity> currentIt = currList.iterator();

                //更新已存在的节点
                while(currentIt.hasNext()){
                    BusiNodeEntity currentNode = currentIt.next();
                    if(currentNode.getProNodeName().equals(proNodeName) || currentNode.getProNodeCode().equals(proNodeCode)){
                        currentNode.setProNodeName(proNodeName);
                        currentNode.setProNodeCode(proNodeCode);
                        updateList.add(currentNode);

                        currentIt.remove();

                        exsit = true;
                        break;
                    }
                }

                //新增节点
                if(!exsit){
                    BusiNodeEntity newBusiNode = new BusiNodeEntity();
                    newBusiNode.setId(getSequenceNextValue(BusiNodeEntity.class));
                    newBusiNode.setProcessId(busiFlowEntity.getId());
                    newBusiNode.setProcessName(busiFlowEntity.getName());
                    newBusiNode.setProcessCode(processCode);
                    newBusiNode.setProNodeName(proNodeName);
                    newBusiNode.setProNodeCode(proNodeCode);
                    newBusiNode.setProNodeEnName(proNodeCode);
                    newBusiNode.setDeletedFlag(false);

                    updateList.add(newBusiNode);
                }

                //currentList剩余的就是待删除的节点，此处做假删除
                for(BusiNodeEntity toDelete : currList){
                    toDelete.setDeletedFlag(true);

                    updateList.add(toDelete);
                }
            }
        }

        try {
            baseDAO.saveOrUpdateAll(updateList);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return updateList;
    }

}
