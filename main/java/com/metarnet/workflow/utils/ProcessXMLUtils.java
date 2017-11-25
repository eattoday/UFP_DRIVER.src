package com.metarnet.workflow.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Administrator on 2017/5/21/0021.
 */
public class ProcessXMLUtils {

    public static void geneProcessXML(){
        Document document = DocumentHelper.createDocument();

        Element root = document.addElement("workflowProcess productVersion=\"6.1\" schemaVersion=\"6.1\"");     //创建根节点
        Element processHeader = document.addElement("processHeader");
        Element activities = document.addElement("activities");
        Element startActivity = document.addElement("activity");
        startActivity.addEntity("activityId" , "startActivity");
        startActivity.addEntity("activityName" , "开始");
        startActivity.addEntity("description" , "");
        startActivity.addEntity("splitType" , "XOR");
        startActivity.addEntity("joinType" , "XOR");
        startActivity.addEntity("priority" , "60");
        startActivity.addEntity("activityType" , "start");
        startActivity.addEntity("splitTransaction" , "false");
        startActivity.addEntity("isStartActivity" , "true");

        Element activateRule = document.addElement("activateRule");
        activateRule.addEntity("activateRuleType" , "directRunning");

        Element applicationInfo = document.addElement("applicationInfo");
        applicationInfo.addEntity("actionType" , "service-component");

        Element application = document.addElement("application");
        application.addEntity("actionType" , "service-component");
        application.addEntity("applicationUri" , "");
        application.addEntity("transactionType" , "suspend");
        application.addEntity("exceptionStrategy" , "ignore");
        application.addEntity("invokePattern" , "synchronous");
        application.addEntity("parameters" , "");

        applicationInfo.add(application);
        activateRule.add(applicationInfo);
        startActivity.add(activateRule);

        Element nodeGraphInfo = document.addElement("nodeGraphInfo");
        nodeGraphInfo.addEntity("color" , "16777215");
        nodeGraphInfo.addEntity("height" , "35");
        nodeGraphInfo.addEntity("width" , "35");
        nodeGraphInfo.addEntity("x" , "100");
        nodeGraphInfo.addEntity("y" , "150");
        nodeGraphInfo.addEntity("lookAndFeel" , "classic");
        nodeGraphInfo.addEntity("fontName" , "System");
        nodeGraphInfo.addEntity("fontSize" , "12");
        nodeGraphInfo.addEntity("fontWidth" , "550");
        nodeGraphInfo.addEntity("isItalic" , "0");
        nodeGraphInfo.addEntity("isUnderline" , "0");
        nodeGraphInfo.addEntity("textHeight" , "60");


        startActivity.add(nodeGraphInfo);
        activities.add(startActivity);




        root.add(processHeader);
        root.add(activities);
    }
}
