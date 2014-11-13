package main.java.net.kolipass;

import main.java.net.kolipass.wworld.GameState;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kolipass on 12.12.13.
 */
public class ResultFrame extends JFrame {
    static int i = 0;
    String resultsFile = "results.xls";

    ArrayList<GameState> beans = new ArrayList<GameState>();

    public ResultFrame(GameState gameState) {
        super("Results Table");
        load(gameState.map, gameState);
        init();
    }

    private HSSFSheet newBook(HSSFWorkbook workbook, String bookName) {
        HSSFSheet sheet = workbook.createSheet(bookName);
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int i = 0;
        Cell id = row.createCell(i++);
        id.setCellValue("map");
        Cell name = row.createCell(i++);
        name.setCellValue("gameIsFinish");
        Cell lat = row.createCell(i++);
        lat.setCellValue("score");
        Cell lon = row.createCell(i++);
        lon.setCellValue("agentIsDied");
        Cell adr = row.createCell(i++);
        adr.setCellValue("wamplusIsDied");
        Cell city = row.createCell(i++);
        city.setCellValue("sulpmuwIsDied");
        Cell firmc = row.createCell(i++);
        firmc.setCellValue("hasGold");
        Cell hasFood = row.createCell(i++);
        hasFood.setCellValue("hasFood");
        Cell hasArrow = row.createCell(i++);
        hasArrow.setCellValue("hasArrow");
        return sheet;
    }

    private void load(String bookname, GameState currentGameState) {
        try {
            File book = new File(resultsFile);
            HSSFWorkbook workbook = null;
            if (!book.exists()) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = new HSSFWorkbook(new FileInputStream(book));
            }

            HSSFSheet sheet = null;
            if (workbook.getSheetIndex(bookname) == -1) {
                sheet = newBook(workbook, bookname);
            } else {
                sheet = workbook.getSheet(bookname);
            }

            int rownum = 0;
            for (Row row : sheet) {
                rownum++;
                if (rownum == 1) {
                    continue;
                }
                GameState gameState = new GameState();
                int i = 0;
                gameState.map = row.getCell(i++).getStringCellValue();
                gameState.gameIsFinish = row.getCell(i++).getBooleanCellValue();
                gameState.score = (int) row.getCell(i++).getNumericCellValue();
                gameState.agentIsDied = row.getCell(i++).getBooleanCellValue();
                gameState.wamplusIsDied = row.getCell(i++).getBooleanCellValue();
                gameState.sulpmuwIsDied = row.getCell(i++).getBooleanCellValue();
                gameState.hasGold = row.getCell(i++).getBooleanCellValue();
                gameState.hasFood = row.getCell(i++).getBooleanCellValue();
                gameState.hasArrow = row.getCell(i++).getBooleanCellValue();

                beans.add(gameState);
            }

            if (currentGameState != null) {
                Row row = sheet.createRow(++rownum);
                int i = 0;
                Cell id = row.createCell(i++);
                id.setCellValue(currentGameState.map);
                Cell name = row.createCell(i++);
                name.setCellValue(currentGameState.gameIsFinish);
                Cell lat = row.createCell(i++);
                lat.setCellValue(currentGameState.score);
                Cell lon = row.createCell(i++);
                lon.setCellValue(currentGameState.agentIsDied);
                Cell adr = row.createCell(i++);
                adr.setCellValue(currentGameState.wamplusIsDied);
                Cell city = row.createCell(i++);
                city.setCellValue(currentGameState.sulpmuwIsDied);
                Cell firmc = row.createCell(i++);
                firmc.setCellValue(currentGameState.hasGold);
                Cell hasFood = row.createCell(i++);
                hasFood.setCellValue(currentGameState.hasFood);
                Cell hasArrow = row.createCell(i++);
                hasArrow.setCellValue(currentGameState.hasArrow);
                beans.add(currentGameState);
            }


            FileOutputStream out =
                    new FileOutputStream(book);
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ResultFrame() {

        super("Results Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();

    }

    private void init() {

        TableModel model = new MyTableModel(beans);
        JTable table = new JTable(model);

        getContentPane().add(new JScrollPane(table));

        setPreferredSize(new Dimension(800, 600));
        pack();
        setVisible(true);

//        toExcel(table, new File("results.xml"));
    }

    public void toExcel(JTable table, File file) {
        try {
            TableModel model = table.getModel();
            FileWriter excel = new FileWriter(file);

            for (int i = 0; i < model.getColumnCount(); i++) {
                excel.write(model.getColumnName(i) + "\t");
            }

            excel.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    excel.write(model.getValueAt(i, j).toString() + "\t");
                }
                excel.write("\n");
            }

            excel.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
//    public void write() throws IOException, WriteException {
//        File file = new File(inputFile);
//        WorkbookSettings wbSettings = new WorkbookSettings();
//
//        wbSettings.setLocale(new Locale("en", "EN"));
//
//        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
//        workbook.createSheet("Report", 0);
//        WritableSheet excelSheet = workbook.getSheet(0);
//        createLabel(excelSheet);
//        createContent(excelSheet);
//
//        workbook.write();
//        workbook.close();
//    }
//
//    private void createLabel(WritableSheet sheet)
//            throws WriteException {
//        // Lets create a times font
//        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
//        // Define the cell format
//        times = new WritableCellFormat(times10pt);
//        // Lets automatically wrap the cells
//        times.setWrap(true);
//
//        // create create a bold font with unterlines
//        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
//                UnderlineStyle.SINGLE);
//        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
//        // Lets automatically wrap the cells
//        timesBoldUnderline.setWrap(true);
//
//        CellView cv = new CellView();
//        cv.setFormat(times);
//        cv.setFormat(timesBoldUnderline);
//        cv.setAutosize(true);
//
//        // Write a few headers
//        addCaption(sheet, 0, 0, "Header 1");
//        addCaption(sheet, 1, 0, "This is another header");
//
//
//    }

    public class MyTableModel implements TableModel {

        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

        private List<GameState> beans;

        public MyTableModel(List<GameState> beans) {
            this.beans = beans;
        }

        public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public int getColumnCount() {
            return 9;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "map";
                case 1:
                    return "gameIsFinish";
                case 2:
                    return "score";
                case 3:
                    return "agentIsDied";
                case 4:
                    return "wamplusIsDied";
                case 5:
                    return "sulpmuwIsDied";
                case 6:
                    return "hasGold";
                case 7:
                    return "hasFood";
                case 8:
                    return "hasArrow";
            }
            return "";
        }

        public int getRowCount() {
            return beans.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            GameState bean = beans.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return bean.map;
                case 1:
                    return bean.gameIsFinish;
                case 2:
                    return bean.score;
                case 3:
                    return bean.agentIsDied;
                case 4:
                    return bean.wamplusIsDied;
                case 5:
                    return bean.sulpmuwIsDied;
                case 6:
                    return bean.hasGold;
                case 7:
                    return bean.hasFood;
                case 8:
                    return bean.hasArrow;

            }
            return "";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {

        }

    }

}
