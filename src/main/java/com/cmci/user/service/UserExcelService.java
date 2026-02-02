package com.cmci.user.service;

import com.cmci.user.constants.UserExcelConstants;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
                if(shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    //System.out.println("Text : " + textShape.getText());
                    sb.append(textShape.getText() == null ? "" : String.valueOf(textShape.getText())).append("\n");
                }
                else if(shape instanceof XSLFTable) {
                    XSLFTable tb = (XSLFTable) shape;
                    //System.out.println("Table Rows : "+tb.getRows().size());
                    for(XSLFTableRow row : tb.getRows()) {
                        for(XSLFTableCell cell : row.getCells()) {
                            //System.out.print(cell.getText()+"\t");
                            sb.append(cell.getText() == null ? "" : String.valueOf(cell.getText())).append("\t");
                        }
                    }
                    //System.out.println();
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }

    private XSSFWorkbook exportPptToExcel(XMLSlideShow ppt) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("PPT Slide To Excel");
        List<XSLFSlide> slides = ppt.getSlides();
        int rowNum = 0;
        for (XSLFSlide slide : slides) {
            for (XSLFShape shape : slide.getShapes()) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    String text = textShape.getText();

                    // 3. 엑셀에 데이터 쓰기
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(text);
                }
                else if(shape instanceof XSLFTable) {
                    XSLFTable tbShape = (XSLFTable) shape;
                    for(XSLFTableRow tr : tbShape.getRows()) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        int tcIdx = 0;
                        for(XSLFTableCell tc : tr.getCells()) {
                            row.createCell(tcIdx++).setCellValue(tc.getText());
                        }
                    }
                }
            }
        }

        return wb;
    }
}
