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
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class Main
{
    private String accessToken;
    private int idGroup = 2;
    public PDDocument doc;
    public String name;
    public Random r;
    public TransportClient tc;
    public VkApiClient vk;
    public GroupActor groupActor;

    private void updatePDF(URL url) throws IOException
    {
        InputStream is = url.openStream();
        int len = 0;
        byte[] buf = new byte[1024];
        byte[] ans = new byte[1000000];
        int i = 0;
        while((len = is.read(buf)) != -1)
        {
            for(int j = 0; j < len; j++)
            {
                ans[i++] = buf[j];
            }
        }

        doc = PDDocument.load(ans);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 1200000)
    public String awake() throws IOException
    {
        URL u = new URL("https://thawing-sands-87522.herokuapp.com/awake");
        u.openStream();
        return "Awaked";
    }

    @PostConstruct
    public void main() throws IOException, ClientException, ApiException
    {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("props.properties");
        properties.load(fileInputStream);
        accessToken = properties.getProperty("accessToken");
        fileInputStream.close();
        System.out.println("PostConstruct");
        r = new Random();
        tc = HttpTransportClient.getInstance();
        vk = new VkApiClient(tc);
        groupActor = new GroupActor(172656437,accessToken);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        vk.messages().send(groupActor, 13466081).randomId(r.nextInt()).message("Start succesfull!! " + df.format(new Date())).execute();

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
        name = urlPDF1;
        bf1.close();
    }

    public File getImage(PDDocument doc) throws IOException
    {
        PDFRenderer pdfRenderer = new PDFRenderer(doc);
        BufferedImage im = pdfRenderer.renderImageWithDPI(4, 200, ImageType.RGB);
        im =im.getSubimage(105, 53, 1519, 698);
        File out = new File("file.jpg");
        ImageIO.write(im, "jpg",out);
        doc.close();
        return out;
    }

    @Scheduled(cron = "0 10 13 * * MON-SUT")
    public void force() throws IOException, ClientException, ApiException
    {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("props.properties");
        properties.load(fileInputStream);
        accessToken = properties.getProperty("accessToken");
        fileInputStream.close();
        r = new Random();
        tc = HttpTransportClient.getInstance();
        vk = new VkApiClient(tc);
        groupActor = new GroupActor(172656437,accessToken);
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
        name = urlPDF;
        URL url = new URL(urlPDF);
        System.out.println(url);

        updatePDF(url);

        File out = getImage(doc);

        PhotoUpload serverResponse = vk.photos().getMessagesUploadServer(groupActor).execute();
        MessageUploadResponse messageUploadResponse = vk.upload().photoMessage(serverResponse.getUploadUrl(),out).execute();
        List<Photo> photoList = vk.photos().saveMessagesPhoto(groupActor, messageUploadResponse.getPhoto())//.saveWallPhoto(groupActor, uploadResponse.getPhoto())
                .server(messageUploadResponse.getServer())
                .hash(messageUploadResponse.getHash())
                .execute();

        Photo photo = photoList.get(0);
        String attachId = "photo" + photo.getOwnerId() + "_" + photo.getId();
        vk.messages().send(groupActor).attachment(attachId).message("Resending").randomId(r.nextInt()).chatId(idGroup).execute();

    }

    @Scheduled(initialDelay = 1000, fixedDelay = 600000)
    public void loop() throws ClientException, ApiException, IOException, URISyntaxException, InterruptedException
    {
        System.out.println("Scheduled");
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
        if(!name.equals(urlPDF))
        {
            //System.out.println(name.equals(urlPDF));
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
            vk.messages().send(groupActor).attachment(attachId).randomId(r.nextInt()).chatId(idGroup).execute();

        }

    }
}
