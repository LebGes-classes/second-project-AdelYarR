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

// Класс для парсинга и сериализации данных из Excel в txt файлы и обратно.
public class ExcelParser {
    // Метод для парсинга Excel файла и сохранения данных в txt файл
    public static void parse(String filePath, String[] sheetNames) throws Exception {
        // Открытие потока для прочтения Excel файла
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);

        // Перебор каждой таблицы в Excel файле
        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);

            Iterator<Row> rowIterator = sheet.iterator();

            FileWriter writer = new FileWriter("src/files/" + sheetName + ".txt", false);

            // Итерация по строкам
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                String rowText = "";
                // Итерация по ячейкам
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    DataFormatter dataFormatter = new DataFormatter();
                    // Форматирование значение ячейки в строку
                    String cellValue = dataFormatter.formatCellValue(cell);

                    // Форматирование строки в заданном формате для txt файла
                    rowText += ("[" + cellValue + "] ");
                }

                // Запись строки в txt файл
                writer.write(rowText);
                writer.append('\n');
                writer.flush();
            }
        }

        fis.close();
    }

    // Метод для сериализации данных из txt файла в Excel файл
    public static void serialize(String filePath, String[] sheetNames) throws Exception {
        Workbook workbook = new XSSFWorkbook();

        // Стиль для сохранения ячейки с денежным значением в виде рубль-копейки
        CellStyle moneyStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        moneyStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

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
                // Поиск по регулярному выражению для получения значений из txt файла в виде [...] [...] ...
                while (matcher.find()) {
                    String value = matcher.group(1);
                    cellCounter++;
                    Cell cell = row.createCell(cellCounter);

                    // Обрабатываем числовое значение
                    if (StringUtils.isNumeric(value)) {
                        // Если ячейка содержит денежное значение, то сериализуем в тип double
                        if ((sheetName.equals("selling_point") && cellCounter == 4) ||
                                (sheetName.equals("product") && (cellCounter == 3 || cellCounter == 4))) {
                            cell.setCellStyle(moneyStyle);
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
