//package com.metarnet.workflow.utils;
//
//import org.apache.poi.hdgf.HDGFDiagram;
//import org.apache.poi.hdgf.extractor.VisioTextExtractor;
//import org.apache.poi.hdgf.pointers.Pointer;
//import org.apache.poi.hdgf.streams.*;
//import org.apache.poi.poifs.filesystem.DocumentEntry;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//
//import java.io.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Date;
//
///**
// * Created by Administrator on 2017/5/15/0015.
// */
//public class VisioUtils {
//
//    public static void main(String args[]){
//        String defFilename = "D:\\工作\\新疆电信\\BPMNvisio\\start.vsd";
////        String defFilename = "D:\\工作\\新疆电信\\BPMNvisio\\规1.3预算管理流程.vsd";
//        try {
////            VisioTextExtractor extractor = new VisioTextExtractor(new FileInputStream(defFilename));
////            VisioTextExtractor extractor = new VisioTextExtractor( new HDGFDiagram( new POIFSFileSystem( new FileInputStream(defFilename) ) ) );
////            String[] text = extractor.getAllText();
////            String textS = extractor.getText();
//
//            HDGFDiagram hdgfDiagram = new HDGFDiagram( new POIFSFileSystem( new FileInputStream(defFilename) ) );
//            readProperties(hdgfDiagram);
////            hdgfDiagram.getDocumentSize();
////            hdgfDiagram.getTopLevelStreams();
////            byte[] contents = hdgfDiagram.getTrailerStream()._getStore()._getContents();
////            String content = new String(contents);
//
////            Diagram diagram = new Diagram(defFilename);
////            Object shenqing = diagram.getPages().get(0).getShapes().get(3);
////
////            for(Object shape : diagram.getPages().get(0).getShapes()){
////                System.out.println(((Shape)shape).getText().getValue().getText());
////            }
//
////            readProperties(shenqing , "");
//
//
//            //Display Visio version and document modification time at different stages
////            System.out.println("Visio Instance Version : " + diagram.getVersion());
////            System.out.println("Full Build Number Created : " + diagram.getDocumentProps().getBuildNumberCreated());
////            System.out.println("Full Build Number Edited : " + diagram.getDocumentProps().getBuildNumberEdited());
////            System.out.println("Date Created : " + diagram.getDocumentProps().getTimeCreated());
////            System.out.println("Date Last Edited : " + diagram.getDocumentProps().getTimeEdited());
////            System.out.println("Date Last Printed : " + diagram.getDocumentProps().getTimePrinted());
////            System.out.println("Date Last Saved : " + diagram.getDocumentProps().getTimeSaved());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    static void readProperties(Object model , String parent) {
//        Method[] methods = model.getClass().getDeclaredMethods();
//        for(Method method : methods){
//            String methodName = method.getName();
//            if(methodName.startsWith("get")){
//                try {
//                    Object value = method.invoke(model);
//
//                    String valueType = value.getClass().getName();
//                    if(valueType.equals("java.lang.String") ||
//                            valueType.equals("java.lang.Integer") ||
//                            valueType.equals("java.lang.Long") ||
//                            valueType.equals("java.lang.Boolean") ||
//                            valueType.equals("com.aspose.diagram.DateTime") ||
//                            valueType.equals("com.aspose.diagram.Color") ||
//                            valueType.equals("com.aspose.diagram.ColorValue") ||
//                            valueType.equals("com.aspose.diagram.StyleSheet") ||
//                            valueType.equals("com.aspose.diagram.UnitFormulaErr") ||
//                            valueType.equals("com.aspose.diagram.BoolValue") ||
//                            valueType.equals("com.aspose.diagram.BevelType") ||
//                            valueType.equals("com.aspose.diagram.BevelLightingType") ||
//                            valueType.equals("com.aspose.diagram.TextBlock") ||
//                            valueType.equals("com.aspose.diagram.TextDirection") ||
//                            valueType.equals("com.aspose.diagram.VerticalAlign") ||
//                            valueType.equals("com.aspose.diagram.PageSheet")){
//                        System.out.println(parent + "." + model.getClass().getSimpleName() + "." + methodName + "() = " + value);
//                    } else if(valueType.equals("com.aspose.diagram.DoubleValue")){
//                        System.out.println(parent + "." + model.getClass().getSimpleName() + "." + methodName + "() = " + ((com.aspose.diagram.DoubleValue)value).getValue());
//                    }  else if(valueType.equals("com.aspose.diagram.Text")){
//                        System.out.println(parent + "." + model.getClass().getSimpleName() + "." + methodName + "() = " + ((com.aspose.diagram.Text) value).getValue().getText());
//                    }  else if(valueType.equals("com.aspose.diagram.Protection") ||
//                            valueType.equals("com.aspose.diagram.Image") ||
//                            valueType.equals("com.aspose.diagram.Misc") ||
//                            valueType.equals("com.aspose.diagram.Help") ||
//                            valueType.equals("com.aspose.diagram.Group") ||
//                            valueType.equals("com.aspose.diagram.Fill") ||
//                            valueType.equals("com.aspose.diagram.Event") ||
//                            valueType.equals("com.aspose.diagram.ForeignData") ||
//                            valueType.equals("com.aspose.diagram.Foreign") ||
//                            valueType.equals("com.aspose.diagram.Align") ||
//                            valueType.equals("com.aspose.diagram.Diagram") ||
//                            valueType.equals("com.aspose.diagram.ThreeDFormat") ||
//                            valueType.equals("com.aspose.diagram.TextXForm") ||
//                            valueType.equals("com.aspose.diagram.Shape")){
//                        continue;
//                    } else {
//                        readProperties(value , parent + "." + model.getClass().getSimpleName());
//                        System.out.println(parent + "." + model.getClass().getSimpleName() + "." + methodName + "() = " + value + ",valueType = " + value.getClass().getName());
//                    }
//                } catch (Exception e) {
////                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    static void readProperties(HDGFDiagram hdgfDiagram){
//        TrailerStream trailer = hdgfDiagram.getTrailerStream();
//        for(int i=0; i<trailer.getPointedToStreams().length; i++) {
//            Stream stream = trailer.getPointedToStreams()[i];
//            Pointer ptr = stream.getPointer();
//
//            System.err.print("Looking at pointer " + i);
//            System.err.print("\tType is " + ptr.getType() + "\t\t");
//            System.err.print("\tOffset is " + ptr.getOffset() + "\t\t");
//            System.err.print("\tAddress is " + ptr.getAddress() + "\t");
//            System.err.print("\tLength is " + ptr.getLength() + "\t\t");
//            System.err.print("\tFormat is " + ptr.getFormat() + "\t\t");
//            System.err.print("\tCompressed is " + ptr.destinationCompressed() + "\t\t");
//            System.err.println("\tStream is " + stream.getClass());
//
//            if(stream instanceof PointerContainingStream) {
//                PointerContainingStream pcs = (PointerContainingStream)stream;
//
//                if(pcs.getPointedToStreams() != null && pcs.getPointedToStreams().length > 0) {
//                    System.err.println("\tContains " + pcs.getPointedToStreams().length + " other pointers/streams");
//                    for(int j=0; j<pcs.getPointedToStreams().length; j++) {
//                        Stream ss = pcs.getPointedToStreams()[j];
//                        Pointer sptr = ss.getPointer();
//                        System.err.print("\t\t" + j + " - Type is " + sptr.getType() + "\t\t");
//                        System.err.print("\t\t" + j + " - Address is " + sptr.getAddress() + "\t\t");
//                        System.err.print("\t\t" + j + " - Offset is " + sptr.getOffset() + "\t\t");
//                        System.err.print("\t\t" + j + " - Length is " + sptr.getLength() + "\t\t");
//                        System.err.println("\t\t" + j + " - Format is " + sptr.getFormat() + "\t\t");
//                    }
//                }
//            }
//
//            if(stream instanceof StringsStream) {
//                System.err.println("\t\t**strings**");
//                StringsStream ss = (StringsStream)stream;
//                System.err.println("\t\t" + ss._getContentsLength());
//                try {
//                    System.err.println("\t\t" + new String(ss._getStore()._getContents() , "UTF8"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//}
