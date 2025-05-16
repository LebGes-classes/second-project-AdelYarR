package excel_parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelParser {
    public static void parse(String filePath, String[] sheetNames) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();

            FileWriter writer = new FileWriter("src/files/" + sheetName + ".txt", false);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                String rowText = "";
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    DataFormatter dataFormatter = new DataFormatter();
                    String cellValue = dataFormatter.formatCellValue(cell);

                    rowText += ("[" + cellValue + "] ");
                }

                writer.write(rowText);
                writer.append('\n');
                writer.flush();
            }
        }

        fis.close();
    }

    public static void serialize(String filePath, String[] sheetNames) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.createSheet(sheetName);
            File file = new File("src/files/" + sheetName + ".txt");
            Scanner sc = new Scanner(file);
            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            int rowCounter = -1;
            while (sc.hasNext()) {
                rowCounter++;
                Row row = sheet.createRow(rowCounter);

                String input = sc.nextLine().trim();
                Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(input);

                int cellCounter = -1;
                ArrayList<String> cells = new ArrayList<>();
                while (matcher.find()) {
                    String value = matcher.group(1);
                    cellCounter++;
                    Cell cell = row.createCell(cellCounter);

                    if (StringUtils.isNumeric(value)) {
                        if ((sheetName.equals("selling_point") && cellCounter == 4) ||
                                (sheetName.equals("product") && (cellCounter == 3 || cellCounter == 4))) {
                            cell.setCellValue(Double.parseDouble(value));
                        } else {
                            cell.setCellValue(Long.parseLong(value));
                        }
                    } else {
                        cell.setCellValue(value);
                    }
                }
                rows.add(cells);
            }

            sc.close();
        }

        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
    }
}
