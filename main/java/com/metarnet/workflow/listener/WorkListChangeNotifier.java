package com.metarnet.workflow.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.eos.data.datacontext.DataContextManager;
import com.eos.foundation.data.DataObjectUtil;
import com.eos.foundation.database.DatabaseUtil;
import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.api.IBPSServiceClient;
import com.eos.workflow.data.WFUserObject;
import com.eos.workflow.data.WFWorkItem;
import com.eos.workflow.omservice.WIParticipantInfo;
import com.primeton.ucloud.workflow.util.CalendarUtil;
import com.primeton.workflow.api.IWorkListChangeNotifier;
import com.primeton.workflow.api.WFServiceException;
import com.primeton.workflow.spi.data.WFWorkItemParticipant;
import com.unicom.ucloud.workflow.exceptions.WFException;
import commonj.sdo.DataObject;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2017/3/18/0018.
 */
public class WorkListChangeNotifier implements IWorkListChangeNotifier {

    @Override
    public void notifyInsert(WFUserObject wfUserObject, Map map) {

        int uuid = new Random().nextInt(10000);

        System.out.println("[IWorkListChangeNotifier-"+uuid+"] into notifyInsert......");

        if (wfUserObject == null)
            return;
        if ((wfUserObject instanceof WFWorkItemParticipant))
            return;

        WFWorkItem workItem = (WFWorkItem)wfUserObject;

        String sender = DataContextManager.current().getMUODataContext().getUserObject().getUserId();
        String preAssigne = sender;
        int processstate;
        int rootprocessstate;

        try {
            IBPSServiceClient client = BPSServiceClientFactory.getDefaultClient();
            Long WorkItemID = Long.valueOf(workItem.getWorkItemID());

            List participants = client.getWorkItemManager().queryWorkItemParticipantInfo(WorkItemID.longValue());

            JSONArray jsArray = new JSONArray();
            for (int i = 0; i < participants.size(); i++) {
                if (((WIParticipantInfo)participants.get(i)).isCurrParticipant()) {
                    JSONObject jsObj = new JSONObject();
                    jsObj.put("id", ((WIParticipantInfo)participants.get(i)).getId());
                    jsObj.put("typeCode", ((WIParticipantInfo)participants.get(i)).getTypeCode());
                    jsArray.add(jsObj);
                }
            }

            preAssigne = preAssigne + "&" + jsArray.toString();

            processstate = client.getProcessInstManager().getProcessInstState(workItem.getProcessInstID());
            rootprocessstate = client.getProcessInstManager().getProcessInstState(workItem.getRootProcInstID());
        } catch (WFServiceException e1) {
            throw new RuntimeException(e1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String preState = workItem.getCurrentState() + "";

        DataSourceHelper.prepare("default");

        DataObject workitembizs = DataObjectUtil.createDataObject("com.primeton.ucloud.listener.worknotifyinfo.wfbusiinfo");
        Timestamp currentdate = new Timestamp(System.currentTimeMillis());
        Long widl = Long.valueOf(workItem.getWorkItemID());
        BigDecimal wid = new BigDecimal(widl.longValue());
        Long acidl = Long.valueOf(workItem.getActivityInstID());
        BigDecimal acid = new BigDecimal(acidl.longValue());
        Long pcidl = Long.valueOf(workItem.getProcessInstID());
        BigDecimal pcid = new BigDecimal(pcidl.longValue());
        Long rpidl = Long.valueOf(workItem.getRootProcInstID());
        BigDecimal rpid = new BigDecimal(rpidl.longValue());
        workitembizs.set("workitemid", wid);
        workitembizs.set("activityinstid", acid);
        workitembizs.set("processinstid", pcid);
        workitembizs.set("rootprocinstid", rpid);

        workitembizs.set("SENDERID", sender);

        workitembizs.set("PREASSIGNE", preAssigne);
        String IsTimeOut = workItem.getIsTimeOut();
        if (IsTimeOut == null) {
            IsTimeOut = "N";
        }
        workitembizs.set("PRESTATE", preState);
        workitembizs.set("preistimeout", IsTimeOut);
        workitembizs.set("prelimitnum", workItem.getLimitNum() + "");
        workitembizs.set("CREATETIME", currentdate);
        workitembizs.set("LASTUPDATETIME", currentdate);
//        workitembizs.set("TENANT_ID", appid);
        workitembizs.set("processstate", processstate + "");
        workitembizs.set("rootprocessstate", rootprocessstate + "");

        DataObject bizinfos = DataObjectUtil.createDataObject("com.primeton.ucloud.listener.worknotifyinfo.wfbizinfo");
        Long RootProcInstID = Long.valueOf(workItem.getRootProcInstID());
        BigDecimal RootProcInstID2B = new BigDecimal(RootProcInstID.longValue());
        bizinfos.set("processInstID", RootProcInstID2B);
        DataObject[] datas = DatabaseUtil.queryEntitiesByTemplate("default", bizinfos);
        if ((datas != null) && (datas.length > 0)) {
            for (DataObject d : datas) {
                if (d.getString("vcColumn2") != null) {
                    workitembizs.set("JOBID", d.getString("vcColumn2"));
                }
                if (d.getString("vcColumn8") != null) {
                    workitembizs.set("JOBCODE", d.getString("vcColumn8"));
                }
                if (d.getString("vcColumn1") != null) {
                    workitembizs.set("JOBTITLE", d.getString("vcColumn1"));
                }
                if (d.getString("vcColumn3") != null) {
                    workitembizs.set("JOBTYPE", d.getString("vcColumn3"));
                }
                if (d.getString("vcColumn4") != null) {
                    workitembizs.set("SHARD", d.getString("vcColumn4"));
                }
                if (d.getString("vcColumn5") != null) {
                    workitembizs.set("BUSINESSID", d.getString("vcColumn5"));
                }
                if (d.getString("vcColumn6") != null) {
                    workitembizs.set("PRODUCTCODE", d.getString("vcColumn6"));
                }
                if (d.getString("vcColumn7") != null) {
                    workitembizs.set("MAJORCODE", d.getString("vcColumn7"));
                }
                if (d.getString("vcColumn9") != null) {
                    workitembizs.set("ROOTVCCOLUMN1", d.getString("vcColumn9"));
                }
                if (d.getString("vcColumn10") != null) {
                    workitembizs.set("ROOTVCCOLUMN2", d.getString("vcColumn10"));
                }
                if (d.getString("nmColumn1") != null) {
                    Long rc1l = Long.valueOf(Long.parseLong(d.get("nmColumn1").toString()));
                    BigDecimal rc1 = new BigDecimal(rc1l.longValue());
                    workitembizs.set("ROOTNMCOLUMN1", rc1);
                }
                if (d.getString("nmColumn2") != null) {
                    Long rc2l = Long.valueOf(Long.parseLong(d.get("nmColumn2").toString()));
                    BigDecimal rc2 = new BigDecimal(rc2l.longValue());
                    workitembizs.set("ROOTNMCOLUMN2", rc2);
                }
                try
                {
                    if (d.get("dtColumn1") != null) {
                        workitembizs.set("JOBSTARTTIME", new Date(CalendarUtil.getTime(d.get("dtColumn1").toString())));
                    }
                    if (d.get("dtColumn2") != null)
                        workitembizs.set("JOBENDTIME", new Date(CalendarUtil.getTime(d.get("dtColumn2").toString())));
                }
                catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try
        {
            List keys = new ArrayList();
            keys.add("reBacktime");
            keys.add("strColumn1");
            keys.add("strColumn2");
            keys.add("strColumn3");
            keys.add("strColumn4");
            keys.add("strColumn5");
            keys.add("strColumn6");
            keys.add("strColumn7");
            keys.add("datColumn1");
            keys.add("datColumn2");
            keys.add("numColumn1");
            keys.add("numColumn2");
            Map RelativeDatas = getRelativeData(Long.toString(workItem.getProcessInstID()), keys);

            if ((RelativeDatas != null) && (RelativeDatas.get("reBacktime") != null))
            {
                String re = RelativeDatas.get("reBacktime").toString();
                Long rel = Long.valueOf(Long.parseLong(re));
                Date reBacktime = new Date(rel.longValue());

                workitembizs.set("REBACKTIME", reBacktime);
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn1") != null)) {
                workitembizs.set("STRCOLUMN1", RelativeDatas.get("strColumn1").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn2") != null)) {
                workitembizs.set("STRCOLUMN2", RelativeDatas.get("strColumn2").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn3") != null)) {
                workitembizs.set("STRCOLUMN3", RelativeDatas.get("strColumn3").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn4") != null)) {
                workitembizs.set("STRCOLUMN4", RelativeDatas.get("strColumn4").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn5") != null)) {
                workitembizs.set("STRCOLUMN5", RelativeDatas.get("strColumn5").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn6") != null)) {
                workitembizs.set("STRCOLUMN6", RelativeDatas.get("strColumn6").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("strColumn7") != null)) {
                workitembizs.set("STRCOLUMN7", RelativeDatas.get("strColumn7").toString());
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("datColumn1") != null)) {
                Object odc1 = RelativeDatas.get("datColumn1");
                if ((odc1 instanceof Date))
                {
                    Date d1 = (Date)RelativeDatas.get("datColumn1");

                    Long dc1l = Long.valueOf(d1.getTime());

                    Date DatColumn1 = new Date(dc1l.longValue());

                    workitembizs.set("DATCOLUMN1", DatColumn1);
                } else if (((odc1 instanceof String)) &&
                        (!((String)odc1).isEmpty())) {
                    String dc1 = RelativeDatas.get("datColumn1").toString();
                    Long dc1l = Long.valueOf(Long.parseLong(dc1));
                    Date DatColumn1 = new Date(dc1l.longValue());

                    workitembizs.set("DATCOLUMN1", DatColumn1);
                }

            }

            if ((RelativeDatas != null) && (RelativeDatas.get("datColumn2") != null)) {
                Object odc2 = RelativeDatas.get("datColumn2");
                if ((odc2 instanceof Date)) {
                    Date d2 = (Date)RelativeDatas.get("datColumn2");
                    Long dc2l = Long.valueOf(d2.getTime());
                    Date DatColumn2 = new Date(dc2l.longValue());
                    workitembizs.set("DATCOLUMN2", DatColumn2);
                } else if (((odc2 instanceof String)) &&
                        (!((String)odc2).isEmpty())) {
                    String dc2 = RelativeDatas.get("datColumn2").toString();
                    Long dc2l = Long.valueOf(Long.parseLong(dc2));
                    Date DatColumn2 = new Date(dc2l.longValue());
                    workitembizs.set("DATCOLUMN2", DatColumn2);
                }

            }

            if ((RelativeDatas != null) && (RelativeDatas.get("numColumn1") != null)) {
                String nc1s = RelativeDatas.get("numColumn1").toString();
                if (!nc1s.equals("0")) {
                    Long nc1l = Long.valueOf(Long.parseLong(nc1s));
                    BigDecimal nc1 = new BigDecimal(nc1l.longValue());
                    workitembizs.set("NUMCOLUMN1", nc1);
                }
            }

            if ((RelativeDatas != null) && (RelativeDatas.get("numColumn2") != null)) {
                String nc2s = RelativeDatas.get("numColumn2").toString();
                if (!nc2s.equals("0")) {
                    Long nc2l = Long.valueOf(Long.parseLong(nc2s));
                    BigDecimal nc2 = new BigDecimal(nc2l.longValue());
                    workitembizs.set("NUMCOLUMN2", nc2);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DatabaseUtil.insertEntity("default", workitembizs);

        System.out.println("[IWorkListChangeNotifier-"+uuid+"] insert WFBusiinfo success,workItemID=" + workItem.getWorkItemID() + ",Participant=" + workItem.getParticipant());

        DataObject workItems = DataObjectUtil.createDataObject("com.eos.workflow.data.WFWorkItem");
        workItems.set("workItemID", Long.valueOf(workItem.getWorkItemID()));

        workItems.set("statesList", "SenderID[" + sender + "]");

        DatabaseUtil.updateEntity("default", workItems);

        System.out.println("[IWorkListChangeNotifier-"+uuid+"] insert WFWorkItem success,workItemID=" + workItem.getWorkItemID() + ",statesList=SenderID[" + sender + "]");

        DataObject participants = DataObjectUtil.createDataObject("com.primeton.ucloud.listener.worknotifyinfo.wfwiparticipant");
        Long wid2 = Long.valueOf(workItem.getWorkItemID());
        BigDecimal WorkItemID2 = new BigDecimal(wid2.longValue());
        participants.set("workItemID", WorkItemID2);
        DataObject[] datas3 = DatabaseUtil.queryEntitiesByTemplate("default", participants);

        if (datas3 != null) {
            for (int i = 0; i < datas3.length; i++) {
                Long pid = Long.valueOf(Long.parseLong(datas3[i].get("WIParticID").toString()));
                BigDecimal ParticID = new BigDecimal(pid.longValue());
                participants.set("WIParticID", ParticID);

                participants.set("statesList", "SenderID[" + sender + "]");

                DatabaseUtil.updateEntity("default", participants);
            }
        }

        System.out.println("[IWorkListChangeNotifier-"+uuid+"] outof notifyInsert......");

    }

    @Override
    public void notifyUpdate(WFUserObject wfUserObject, Map map) {

        int uuid = new Random().nextInt(10000);
        System.out.println("[IWorkListChangeNotifier-"+uuid+"] into notifyUpdate......");

        if (wfUserObject == null)
            return;
        if ((wfUserObject instanceof WFWorkItemParticipant)) {
            return;
        }

        WFWorkItem workItem = (WFWorkItem)wfUserObject;

        DataSourceHelper.prepare("default");
        DataObject workitembizs = DataObjectUtil.createDataObject("com.primeton.ucloud.listener.worknotifyinfo.wfbusiinfo");
        Long wid3 = Long.valueOf(workItem.getWorkItemID());
        BigDecimal WorkItemID3 = new BigDecimal(wid3.longValue());
        workitembizs.set("workitemid", WorkItemID3);

        String preAssigne = DataContextManager.current().getMUODataContext().getUserObject().getUserId();
        String preState = workItem.getCurrentState() + "";
        try
        {
            IBPSServiceClient client = BPSServiceClientFactory.getDefaultClient();
            Long WorkItemID = Long.valueOf(workItem.getWorkItemID());

            List participants = client.getWorkItemManager().queryWorkItemParticipantInfo(WorkItemID.longValue());

            JSONArray jsArray = new JSONArray();
            for (int i = 0; i < participants.size(); i++) {
                if (((WIParticipantInfo)participants.get(i)).isCurrParticipant()) {
                    JSONObject jsObj = new JSONObject();
                    jsObj.put("id", ((WIParticipantInfo)participants.get(i)).getId());
                    jsObj.put("typeCode", ((WIParticipantInfo)participants.get(i)).getTypeCode());
                    jsArray.add(jsObj);
                }

            }

            preAssigne = preAssigne + "&" + jsArray.toString();
        }
        catch (WFServiceException e1) {
            throw new RuntimeException(e1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        workitembizs.set("PREASSIGNE", preAssigne);
        workitembizs.set("PRESTATE", preState);
        workitembizs.set("preistimeout", workItem.getIsTimeOut());
        workitembizs.set("prelimitnum", workItem.getLimitNum() + "");
        DatabaseUtil.updateEntity("default", workitembizs);

        System.out.println("[IWorkListChangeNotifier-"+uuid+"] update wfbusiinfo success,workItemID=" + workItem.getWorkItemID() + ",Participant=" + workItem.getParticipant());
        System.out.println("[IWorkListChangeNotifier-"+uuid+"] outof notifyUpdate......");

    }

    @Override
    public void notifyRemove(WFUserObject wfUserObject, Map map) {

    }


    public static Map<String, Object> getRelativeData(String processInstID, List<String> keys) throws WFException {
        try
        {
            IBPSServiceClient client = BPSServiceClientFactory.getDefaultClient();
            if (keys == null) {
                throw new WFException("getRelativeData方法中关键字不能为空");
            }
            if (processInstID == null) {
                throw new WFException("getRelativeData方法中流程实例ID不能为空");
            }
            String[] array = new String[keys.size()];

            List values = client.getRelativeDataManager().getRelativeDataBatch(Long.parseLong(processInstID), (String[])keys.toArray(array));
            Map map = new HashMap();
            for (int i = 0; i < keys.size(); i++)
                if (values.get(i) != null)
                {
                    if ((values.get(i) instanceof List)) {
                        List list = (List)values.get(i);
                        for (int j = 0; j < list.size(); j++)
                        {
                            Object item = list.get(j);
                            if (item == null);
                        }
                        map.put(keys.get(i), list);
                    }
                    else if ((values.get(i) instanceof Object[])) {
                        Object[] objs = (Object[])values.get(i);
                        for (int j = 0; j < objs.length; j++)
                        {
                            if (objs[j] != null);
                        }

                        map.put(keys.get(i), objs);
                    } else {
                        map.put(keys.get(i), values.get(i));
                    }
                }
            return map;
        } catch (WFServiceException e) {
            throw new WFException(e);
        }
    }
}
