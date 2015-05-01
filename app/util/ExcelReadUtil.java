package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.poi.ss.usermodel.*;
import service.MessageListener;


public class ExcelReadUtil {

    static{
        MessageListener listener = new MessageListener();
        listener.start();
    }


    public void getIds(File file){
        System.out.println("in get idsss");
        InputStream inp = null;

        try{
            inp = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            Header header = sheet.getHeader();

            int rowsCount = sheet.getLastRowNum();
            System.out.println("Total Number of Rows: " + (rowsCount + 1));
            for (int i = 0; i <= rowsCount; i++) {
                Row row = sheet.getRow(i);
                int colCounts = row.getLastCellNum();
                System.out.println("Total Number of Cols: " + colCounts);
                for (int j = 0; j < colCounts; j++) {
                    Cell cell = row.getCell(j);
                    System.out.println("[" + i + "," + j + "]=" + cell.getNumericCellValue());
                    publishUniqueId(cell.getNumericCellValue()+"");

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                inp.close();
            } catch (IOException ex) {
            }
        }
    }


    public boolean publishUniqueId(String uniqueId) throws Exception
    {
        System.out.println("inside publishUniqueId  "+uniqueId);
        String uri = System.getenv("CLOUDAMQP_URL");
         if (uri == null)
            // uri = "amqp://ifyuypdy:r7OnVUkJVHdyphz5WjVnQ8g9ut2LA9yE@tiger.cloudamqp.com/ifyuypdy";
             uri = "amqp://performance:performance@rabbitmq.dev.octanner.com:5672/ntp";

        System.out.println("uri ::  "+uri);
        ConnectionFactory factory = new ConnectionFactory();
         factory.setUri(uri);
         factory.setRequestedHeartbeat(30);
         factory.setConnectionTimeout(30000);
        System.out.println("before connection creation");
         Connection connection = factory.newConnection();
        System.out.println("connection created");
         Channel channel = connection.createChannel();
        System.out.println("channel created");
         channel.queueDeclare("hello", false, false, false, null);
         channel.basicPublish("", "hello", null, uniqueId.getBytes());
        System.out.println("basic publish completed");
        channel.close();
        System.out.println("clannel closed");
        connection.close();
        System.out.println("connection closed");
        return true;
    }

}
