package com.cmci.user.service;

import com.cmci.user.constants.UserExcelConstants;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service(value="com.cmci.user.service..UserExcelService")
public class UserExcelService {

    public String exportPptFile(MultipartFile pptFile) {
        String rStr = "";
        String uuid = UUID.randomUUID().toString().replace("-","");

        File tmpFolder = new File(UserExcelConstants.PPT_TMP_FOLDER+File.separator+uuid);
        if(!tmpFolder.exists())
            tmpFolder.mkdir();

        File targetFile = new File(UserExcelConstants.PPT_TMP_FOLDER+File.separator+uuid+File.separator+pptFile.getOriginalFilename());
        try {
            try(InputStream is = pptFile.getInputStream()) {
                FileOutputStream fos = new FileOutputStream(targetFile);
                IOUtils.copy(is, fos);
            }

            try(XMLSlideShow ppt = new XMLSlideShow(Files.newInputStream(targetFile.toPath()))) {
                rStr = exportPptInfos(ppt);
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        File transFile = new File(UserExcelConstants.PPT_TMP_FOLDER+File.separator+uuid+File.separator+pptFile.getOriginalFilename().replace(".pptx", ".xlsx"));
        try {
            try(InputStream is = pptFile.getInputStream()) {
                FileOutputStream fos = new FileOutputStream(targetFile);
                IOUtils.copy(is, fos);
            }

            try(XMLSlideShow ppt = new XMLSlideShow(Files.newInputStream(targetFile.toPath()))) {
                XSSFWorkbook wb = exportPptToExcel(ppt);
                wb.write(Files.newOutputStream(transFile.toPath()));
                wb.close();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return rStr;
    }

    private String exportPptInfos(XMLSlideShow ppt) {
        StringBuffer sb = new StringBuffer();
        List<XSLFSlide> slides = ppt.getSlides();
        //System.out.println("Total Slide Size : " + slides.size());
        sb.append("Total Slide Size : ").append(slides.size()).append("\n");
        // 슬라이드 루프 처리 가능
        int pagePos = 1;
        for (XSLFSlide slide : slides) {
            sb.append("Page ").append(pagePos++).append("\n");
            // 데이터 추출 또는 수정 로직
            // 1. 일단 텍스트 박스 및 도형 내 텍스트
            for(XSLFShape shape : slide.getShapes()) {
                exportShape(shape, sb);
            }
        }

        return sb.toString();
    }

    private void exportShape(XSLFShape shape, StringBuffer sb) {
        if(shape!=null) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape textShape = (XSLFTextShape) shape;
                //System.out.println("Text : " + textShape.getText());
                sb.append("TXT : ").append(textShape.getText() == null ? "" : String.valueOf(textShape.getText())).append("\n");
            } else if (shape instanceof XSLFTable) {
                XSLFTable tb = (XSLFTable) shape;
                //System.out.println("Table Rows : "+tb.getRows().size());
                for (XSLFTableRow row : tb.getRows()) {
                    int tdPos = 0;
                    for (XSLFTableCell cell : row.getCells()) {
                        //System.out.print(cell.getText()+"\t");
                        if(tdPos==0)
                            sb.append("TB : ");

                        sb.append(cell.getText() == null ? "" : String.valueOf(cell.getText())).append("\t");
                        tdPos++;
                    }
                }
                //System.out.println();
                sb.append("\n");
            } else if (shape instanceof XSLFGroupShape) {
                XSLFGroupShape gs = (XSLFGroupShape) shape;
                List<XSLFShape> gsList = gs.getShapes();
                for(XSLFShape subShape : gsList) {
                    exportShape(subShape, sb);
                }
            }
        }
    }

    private XSSFWorkbook exportPptToExcel(XMLSlideShow ppt) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("PPT Slide To Excel");
        //sheet.setDefaultColumnWidth(1000);

        XSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);

        List<XSLFSlide> slides = ppt.getSlides();
        int rowNum = 0;
        int pageIdx = 1;
        for (XSLFSlide slide : slides) {

            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Page : "+pageIdx);
            row.getCell(0).setCellStyle(cs);

            for (XSLFShape shape : slide.getShapes()) {
                rowNum = transShape(shape, sheet, cs, rowNum++);
            }
            pageIdx++;
        }

        sheet.autoSizeColumn(0);
        return wb;
    }

    private int transShape(XSLFShape shape, XSSFSheet sheet, XSSFCellStyle cs, int rowNum) {
        if (shape instanceof XSLFTextShape) {
            XSLFTextShape textShape = (XSLFTextShape) shape;
            //String text = textShape.getText();
            String text = textShape.getText() == null ? "" : String.valueOf(textShape.getText().trim());

            if(!"".equals(text)) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(text);
                row.getCell(0).setCellStyle(cs);
            }
        }
        else if(shape instanceof XSLFTable) {
            XSLFTable tbShape = (XSLFTable) shape;
            for(XSLFTableRow tr : tbShape.getRows()) {
                XSSFRow row = sheet.createRow(rowNum++);
                int tcIdx = 0;
                for(XSLFTableCell tc : tr.getCells()) {
                    String text = tc.getText() == null ? "" : String.valueOf(tc.getText().trim());
                    row.createCell(tcIdx++).setCellValue(tc.getText());
                }
            }
        }
        else if (shape instanceof XSLFPictureShape) {
            /*
            XSLFPictureData picData = ((XSLFPictureShape) shape).getPictureData();
            XSSFWorkbook wb = sheet.getWorkbook();
            int picType = 0;
            if(picData.getType().name().equals("PNG")) picType = XSSFWorkbook.PICTURE_TYPE_PNG;
            else if(picData.getType().name().equals("JPEG")) picType = XSSFWorkbook.PICTURE_TYPE_JPEG;
            else if(picData.getType().name().equals("EMF")) picType = XSSFWorkbook.PICTURE_TYPE_EMF;

            int picIdx = wb.addPicture(picData.getData(), picType);
            XSSFCreationHelper helper = wb.getCreationHelper();
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = helper.createClientAnchor();

            anchor.setCol1(0);
            anchor.setRow1(rowNum++);
            drawing.createPicture(anchor, picIdx).resize();
            */
            /*
            XSLFPictureData picData = ((XSLFPictureShape) shape).getPictureData();
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Image : "+picData.getFileName());
            row.getCell(0).setCellStyle(cs);
            */
        }
        else if (shape instanceof  XSLFConnectorShape) {
            /*
            XSLFConnectorShape conn = (XSLFConnectorShape) shape;
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Connector : "+conn.getShapeName());
            row.getCell(0).setCellStyle(cs);
            */
        }
        else if (shape instanceof XSLFGroupShape) {
            XSLFGroupShape gs = (XSLFGroupShape) shape;
            List<XSLFShape> gsList = gs.getShapes();
            for(XSLFShape subShape : gsList) {
                rowNum = transShape(subShape, sheet, cs, rowNum++);
            }
        }
        else {
            /*
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("지원하지 않는 Shape : "+shape.getShapeName());
            */
        }
        return rowNum;
    }
}
