package com.metarnet.workflow.service.impl;

import com.metarnet.core.common.dao.IBaseDAO;
import com.metarnet.core.common.exception.DAOException;
import com.metarnet.core.common.exception.ServiceException;
import com.metarnet.core.common.model.Pager;
import com.metarnet.workflow.service.IWorkflowService;
import com.unicom.ucloud.workflow.objects.TaskInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/11/16/0016.
 */
@Service()
public class WorkflowServiceImpl implements IWorkflowService {

    Logger logger = LogManager.getLogger("WorkflowServiceImpl");

    @Autowired
    private IBaseDAO baseDAO;

    @Override
    public Pager getMyCompletedTasks(String accountId , Pager pager) throws ServiceException {
//        String sql = "select * from wfcomplete_view t where t.assistant = ? or t.participantid = ?";
        StringBuffer sqlBuffer = new StringBuffer("select ");

        sqlBuffer.append("jobid,");
        sqlBuffer.append("jobtitle,");
        sqlBuffer.append("jobcode,");
        sqlBuffer.append("rootprocessinstid,");
        sqlBuffer.append("activitydefid,");
        sqlBuffer.append("activityinstid,");
        sqlBuffer.append("activityinstname,");
        sqlBuffer.append("appid,");
        sqlBuffer.append("businessid,");
        sqlBuffer.append("to_char(completiondate,'yyyy-MM-dd HH24:mi:ss') as completiondate,");
        sqlBuffer.append("to_char(createdate,'yyyy-MM-dd HH24:mi:ss') as createdate,");
        sqlBuffer.append("currentstate,");
        sqlBuffer.append("to_char(datcolumn1,'yyyy-MM-dd HH24:mi:ss') as datcolumn1,");
        sqlBuffer.append("to_char(datcolumn2,'yyyy-MM-dd HH24:mi:ss') as datcolumn2,");
        sqlBuffer.append("to_char(enddate,'yyyy-MM-dd HH24:mi:ss') as enddate,");
        sqlBuffer.append("formurl,");
        sqlBuffer.append("numcolumn1,");
        sqlBuffer.append("numcolumn2,");
        sqlBuffer.append("processinstid,");
        sqlBuffer.append("processmodelcnname,");
        sqlBuffer.append("processmodelid,");
        sqlBuffer.append("processmodelname,");
        sqlBuffer.append("taskinstid,");
        sqlBuffer.append("strcolumn1,");
        sqlBuffer.append("strcolumn2,");
        sqlBuffer.append("strcolumn3,");
        sqlBuffer.append("strcolumn4,");
        sqlBuffer.append("strcolumn5,");
        sqlBuffer.append("strcolumn6,");
        sqlBuffer.append("strcolumn7");

        sqlBuffer.append(" from wfcomplete_view t where (t.assistant = ? or t.participantid = ?)");

        Pager page = new Pager();
        List params = new ArrayList();

        params.add(accountId);
        params.add(accountId);

        Map<String , Object> fastQueryParameters = pager.getFastQueryParameters();

        if(fastQueryParameters != null){
            String symbol ;
            String property ;

            for(String key : fastQueryParameters.keySet()){

                if(key.indexOf("_format") != -1){
                    continue;
                }

                int _index;
                if((_index = key.indexOf("_")) != -1){

                    Object value = fastQueryParameters.get(key);
                    if("".equals(value)){
                        continue;
                    }

                    symbol = key.substring(0 , _index);
                    property = key.substring(_index + 1 , key.length());

                    sqlBuffer.append(" and " + property);
                    if("lk".equals(symbol)){
                        sqlBuffer.append(" like ?");
                        params.add("%" + value + "%");
                    } else if("eq".equals(symbol)){
                        sqlBuffer.append(" = ?");
                        params.add(value);
                    } else if("le".equals(symbol)){
                        sqlBuffer.append(" < ?");
                        Object dateformat = null;
                        if((dateformat = fastQueryParameters.get(key + "_format")) != null){
                            SimpleDateFormat sdf = new SimpleDateFormat(dateformat.toString());
                            try {
                                Date date = sdf.parse(value.toString());
                                params.add(new java.sql.Date(date.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            params.add(value);
                        }

                    } else if("ge".equals(symbol)){
                        sqlBuffer.append(" > ?");
                        Object dateformat = null;
                        if((dateformat = fastQueryParameters.get(key + "_format")) != null){
                            SimpleDateFormat sdf = new SimpleDateFormat(dateformat.toString());
                            try {
                                Date date = sdf.parse(value.toString());
                                params.add(new java.sql.Date(date.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            params.add(value);
                        }
                    } else if("in".equals(symbol)){

                        sqlBuffer.append(" in (");

                        String[] array = value.toString().split(",");
                        for(int i = 0 ; i < array.length ; i ++ ){
                            sqlBuffer.append("?");
                            if(i != array.length - 1){
                                sqlBuffer.append(",");
                            }

                            params.add(array[i]);
                        }
                        sqlBuffer.append(")");
                    }
                }
            }
        }



        try {
            logger.info(sqlBuffer.toString());
            page = baseDAO.findNativeSQLOra(sqlBuffer.toString() , params.toArray() , pager);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        return page;
    }

    @Override
    public String updateBusiInfoByRoot(TaskInstance taskInstance) throws ServiceException {
        StringBuffer sqlBuffer = new StringBuffer("update wfbusiinfo set ");

        if(taskInstance.getStrColumn1() != null && !"".equals(taskInstance.getStrColumn1())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn1(), "UTF-8");
                sqlBuffer.append("strcolumn1 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn2() != null && !"".equals(taskInstance.getStrColumn2())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn2(), "UTF-8");
                sqlBuffer.append("strcolumn2 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn3() != null && !"".equals(taskInstance.getStrColumn3())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn3(), "UTF-8");
                sqlBuffer.append("strcolumn3 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn4() != null && !"".equals(taskInstance.getStrColumn4())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn4(), "UTF-8");
                sqlBuffer.append("strcolumn4 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn5() != null && !"".equals(taskInstance.getStrColumn5())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn5(), "UTF-8");
                sqlBuffer.append("strcolumn5 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn6() != null && !"".equals(taskInstance.getStrColumn6())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn6(), "UTF-8");
                sqlBuffer.append("strcolumn6 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getStrColumn7() != null && !"".equals(taskInstance.getStrColumn7())){
            try {
                String value = URLDecoder.decode(taskInstance.getStrColumn7(), "UTF-8");
                sqlBuffer.append("strcolumn7 = '");
                sqlBuffer.append(value);
                sqlBuffer.append("',");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if(taskInstance.getNumColumn1() != 0){
            sqlBuffer.append("numcolumn1 = '");
            sqlBuffer.append(taskInstance.getNumColumn1());
            sqlBuffer.append("',");
        }

        if(taskInstance.getNumColumn2() != 0){
            sqlBuffer.append("numcolumn2 = '");
            sqlBuffer.append(taskInstance.getNumColumn2());
            sqlBuffer.append("',");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(taskInstance.getDatColumn1() != null){
            sqlBuffer.append("datcolumn1 = ");

            Date date = taskInstance.getDatColumn1();
            String value = sdf.format(date);
            value = "to_date('"+value+"' , 'yyyy-MM-dd HH24:mi:ss')";

            sqlBuffer.append(value);
            sqlBuffer.append(",");
        }

        if(taskInstance.getDatColumn2() != null){
            sqlBuffer.append("datcolumn2 = ");

            Date date = taskInstance.getDatColumn2();
            String value = sdf.format(date);
            value = "to_date('"+value+"' , 'yyyy-MM-dd HH24:mi:ss')";

            sqlBuffer.append(value);
            sqlBuffer.append(",");
        }


        String sql = sqlBuffer.toString();

        if(sql.endsWith(",")){
            sql = sql.substring(0 , sql.length() - 1);
        }

        sql += " where rootProcInstId = " + taskInstance.getRootProcessInstId();

        try {
            logger.info(sql);
            baseDAO.executeSql(sql);
            logger.info("update Busiinfo table success......rootProcInstId=" + taskInstance.getRootProcessInstId());
        } catch (DAOException e) {
            logger.info("更新业务扩展信息报错\n");
            logger.info(e.getMessage());
        }

        return null;
    }

}
