package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.poi.ss.usermodel.*;
import service.BulkEthanksQueueListener;


public class ExcelReadUtil {

    static{
        for(int i=0; i<3; i++)
        {
            BulkEthanksQueueListener listener = new BulkEthanksQueueListener();
            listener.setListenerNumber(i);
            listener.start();

        }
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
         channel.queueDeclare(BulkEthanksQueueListener.BULK_ETHANKS_QUEUE, false, false, false, null);
         channel.basicPublish("", BulkEthanksQueueListener.BULK_ETHANKS_QUEUE, null, uniqueId.getBytes());
        System.out.println("basic publish completed");
        channel.close();
        System.out.println("clannel closed");
        connection.close();
        System.out.println("connection closed");
        return true;
    }

}
