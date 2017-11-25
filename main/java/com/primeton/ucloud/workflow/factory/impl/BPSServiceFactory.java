//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.primeton.ucloud.workflow.factory.impl;

import com.eos.data.sdo.DynamicXSDLoader;
import com.primeton.ucloud.workflow.factory.BPMServiceFactory;
import com.primeton.ucloud.workflow.impl.BPSObjectInterfaceImpl;
import com.unicom.ucloud.workflow.exceptions.WFException;
import com.unicom.ucloud.workflow.interfaces.WorkflowObjectInterface;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class BPSServiceFactory extends BPMServiceFactory {
    private static Logger logger = Logger.getLogger(BPSServiceFactory.class);

    public BPSServiceFactory() {
    }

    public WorkflowObjectInterface getWorkflowService(String accountID, String appID, String token) throws WFException {
        return new BPSObjectInterfaceImpl(accountID, appID, token);
    }

    public String getProvider() {
        return "Primeton BPS";
    }

    protected int getPriority() {
        return 100;
    }

    private static void printFile(String file) throws IOException {
        println(">>>>>>>>>>>> " + file);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream criteriaStream2 = cl.getResourceAsStream(file);
        if(criteriaStream2 == null) {
            throw new IOException("资源文件" + file + "不存在");
        } else {
            criteriaStream2.close();
            println("<<<<<<<<<<<< " + file);
        }
    }

    private static void println(String file) {
        logger.info(file);
    }

    static {
        try {
            printFile("META-INF/xsd/biz.xsd");
            printFile("META-INF/xsd/data.xsd");
            printFile("META-INF/xsd/extdataset.xsd");
            printFile("META-INF/xsd/bizdataset.xsd");
            printFile("com/eos/foundation.xsd");
            printFile("com/primeton/das/criteria.xsd");
            printFile("META-INF/das/AnyType.xsd");
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream bizStream = cl.getResourceAsStream("META-INF/xsd/biz.xsd");
        InputStream dataStream = cl.getResourceAsStream("META-INF/xsd/data.xsd");
        InputStream extdatasetStream = cl.getResourceAsStream("META-INF/xsd/extdataset.xsd");
        InputStream bizdatasetStream = cl.getResourceAsStream("META-INF/xsd/bizdataset.xsd");
        InputStream criteriaStream = cl.getResourceAsStream("com/primeton/das/criteria.xsd");
        InputStream foundationStream = cl.getResourceAsStream("com/eos/foundation.xsd");
        InputStream anyTypeStream = cl.getResourceAsStream("META-INF/das/AnyType.xsd");
        DynamicXSDLoader.load(bizStream, (String)null, true);
        DynamicXSDLoader.load(dataStream, (String)null, true);
        DynamicXSDLoader.load(extdatasetStream, (String)null, true);
        DynamicXSDLoader.load(bizdatasetStream, (String)null, true);
        DynamicXSDLoader.load(criteriaStream, (String)null, true);
        DynamicXSDLoader.load(foundationStream, (String)null, true);
        DynamicXSDLoader.load(anyTypeStream, (String)null, true);
    }
}
