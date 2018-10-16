package com;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.GroupAuthResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.photos.responses.WallUploadResponse;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    public static PDDocument doc;

    private static void updatePDF(URL url) throws IOException
    {
        InputStream is = url.openStream();
        int len = 0;
        //ArrayList<byte[]> li= new ArrayList<byte[]>();
        byte[] buf = new byte[1024];
        byte[] ans = new byte[1000000];
        int i = 0;
        while((len = is.read(buf)) != -1)
        {
            //System.out.println(len);
            for(int j = 0; j < len; j++)
            {
                ans[i++] = buf[j];
            }
        }

        doc = PDDocument.load(ans);
    }


    public static void main(String[] args) throws ClientException, ApiException, IOException, URISyntaxException, InterruptedException
    {

        Random r = new Random();
        TransportClient tc = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(tc);
        GroupActor groupActor = new GroupActor(172656437,"e1682d74ee13b3c7e64cc9e9dad8ae15aa2c2785ad47a0cae7f3c2084b571c3f6afd124a452143b00d378");
        vk.messages().send(groupActor, 13466081).randomId(r.nextInt()).message("Start succesfull!!").execute();

        URL siteURL1 = null;
        siteURL1 = new URL("http://www.mrk-bsuir.by/ru");
        BufferedReader bf1 = new BufferedReader(new InputStreamReader(siteURL1.openStream()));
        StringBuilder sb1 = new StringBuilder();
        String line1 = null;
        while((line1 = bf1.readLine()) != null)
        {
            sb1.append(line1);
        }
        bf1.close();
        int pos1 = sb1.indexOf("Объявления");
        int end1 = sb1.indexOf(".pdf", pos1);
        String urlPDF1 = sb1.substring(pos1+76, end1+4);
        String name = urlPDF1;
        bf1.close();
        while(true)
        {
            Thread.sleep(600000);
            URL siteURL = null;
            siteURL = new URL("http://www.mrk-bsuir.by/ru");
            BufferedReader bf = new BufferedReader(new InputStreamReader(siteURL.openStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = bf.readLine()) != null)
            {
                sb.append(line);
            }
            bf.close();
            int pos = sb.indexOf("Объявления");
            int end = sb.indexOf(".pdf", pos);
            String urlPDF = sb.substring(pos+76, end+4);
            if(name != urlPDF)
            {
                name = urlPDF;
                URL url = new URL(urlPDF);
                System.out.println(url);

                updatePDF(url);

                PDFRenderer pdfRenderer = new PDFRenderer(doc);
                BufferedImage im = pdfRenderer.renderImageWithDPI(4, 200, ImageType.RGB);
                im =im.getSubimage(105, 53, 1519, 698);
                File out = new File("file.jpg");
                ImageIO.write(im, "jpg",out);

                doc.close();

                PhotoUpload serverResponse = vk.photos().getMessagesUploadServer(groupActor).execute();
                MessageUploadResponse messageUploadResponse = vk.upload().photoMessage(serverResponse.getUploadUrl(),out).execute();
                List<Photo> photoList = vk.photos().saveMessagesPhoto(groupActor, messageUploadResponse.getPhoto())//.saveWallPhoto(groupActor, uploadResponse.getPhoto())
                        .server(messageUploadResponse.getServer())
                        .hash(messageUploadResponse.getHash())
                        .execute();

                Photo photo = photoList.get(0);
                String attachId = "photo" + photo.getOwnerId() + "_" + photo.getId();
                vk.messages().send(groupActor).attachment(attachId).randomId(r.nextInt()).chatId(1).execute();
                //Привет для Коли vk.messages().send(groupActor, 152839977).attachment(attachId).message("ТЫ БЫЛ НЕ ПРАВ").randomId(r.nextInt()).execute();

            }


        }

    }
}
