package com;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.GroupAuthResponse;


public class Main
{
    public static void main(String[] args)
    {
        TransportClient tc = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(tc);
        GroupAuthResponse authResponse = null;
        /*try
        {
            authResponse = vk.oauth()
                    .groupAuthorizationCodeFlow(6722262,"Nknw2pIrertsbaFaJFV9", "0", "4c32285466541109955aaf036e06d54c69d41b6de0cf8e606099b69ed1be442d7a020ad82a7c1d9022e1f")
                    .execute();
        }
        catch(ApiException e)
        {
            e.printStackTrace();
        }
        catch(ClientException e)
        {
            e.printStackTrace();
        }*/
        //GroupActor groupActor = new GroupActor(172656437, authResponse.getAccessTokens().get(172656437));
        vk.messages().se
    }
}
