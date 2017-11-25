package com.metarnet.workflow.test;

import com.alibaba.fastjson.JSONObject;
import com.metarnet.core.common.utils.HttpClientUtil;
import com.metarnet.core.common.workflow.ProcessModelParams;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guohaotian01 on 2017/11/2.
 */
public class TestAPI {
    public static void main(String[] args) {

        try {
            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<JSONObject> forEntity =
//                    restTemplate.getForEntity("http://127.0.0.1:9087/UFP_DRIVER/workFlowController.do?method=startProcess&processModelID=sjz-3&bizModleParam={?}",
//                            JSONObject.class,"{\"jobID\":\"1111\",\"jobCode\":\"22222\",\"jobTitle\":\"3333\"}");
            String url = "http://127.0.0.1:9087/UFP_DRIVER/";
            String method = "workFlowController.do?method=startProcess";

            String processModelIDString = "&processModelID={?}";
            String bizModleParamString = "&bizModleParam={?}";
            String accountIdString="&accountId={?}";
            String participantIDString="&participantID={?}";
            String tenantIdString="&tenantId={?}";
            String processModelParamsString="&processModelParams={?}";


            String processModelID = "sjz-3";
            String accountId="root";
            String participantID="root";
            String tenantId="111";

            Map<String, Object> map = new HashMap<>();
            map.put("jobID", "1111");
            map.put("jobCode", "2222");
            map.put("jobTitle", "3333");
            String bizModleParam = JSONObject.toJSONString(map);

            ProcessModelParams modelParams =new ProcessModelParams();
            modelParams.setParameter("1111","22222");
            String processModelIDParams=JSONObject.toJSONString(modelParams);
//            System.out.println(processModelIDParams);
//            System.out.println(json);
//            String bizModleParam="{\"jobID\":\"1111\",\"jobCode\":\"22222\",\"jobTitle\":\"3333\"}";
            ResponseEntity<JSONObject> forEntity1 =
                    restTemplate.postForEntity(url + method +
                                    processModelIDString +
                                    accountIdString+
                                    participantIDString+
                                    tenantIdString+
                                    bizModleParamString+
                                    processModelParamsString,
                            null, JSONObject.class,

                            processModelID,
                            accountId,
                            participantID,
                            tenantId,
                            bizModleParam,
                            processModelIDParams

                            );
            JSONObject body = forEntity1.getBody();
            System.out.println(JSONObject.toJSONString(body));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
