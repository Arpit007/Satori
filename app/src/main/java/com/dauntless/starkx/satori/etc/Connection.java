package com.dauntless.starkx.satori.etc;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Home Laptop on 06-Oct-17.
 */

public class Connection
{
	public static final String BaseUrl = "";
	static final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
	static OkHttpClient client = new OkHttpClient();

	public static void Post(final String url, final JSONObject object, final ConnectionResponse connectionResponse)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					RequestBody body = RequestBody.create(jsonType, object.toString());
					Request request = new Request.Builder()
							.url(url)
							.post(body)
							.build();
					Response response = client.newCall(request).execute();
					connectionResponse.JsonResponse(new JSONObject(response.body().string()), true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					connectionResponse.JsonResponse(null, false);
				}
			}
		}.start();
	}

	public static void Get(final String url, final JSONObject object, final ConnectionResponse connectionResponse)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					String Url = url;
					StringBuilder Query = new StringBuilder("");
					Iterator<String> keys = object.keys();
					while (keys.hasNext())
					{
						if (!Query.toString().isEmpty())
						{
							Query.append("&");
						}
						String Key = keys.next();
						Query.append(Key + "=" + object.getString(Key));
					}

					if (!Query.toString().isEmpty())
					{
						Url += "?" + Query;
					}

					Request request = new Request.Builder()
							.url(Url)
							.build();

					Response response = client.newCall(request).execute();
					connectionResponse.JsonResponse(new JSONObject(response.body().string()), true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					connectionResponse.JsonResponse(null, false);
				}
			}
		}.start();
	}

	public static interface ConnectionResponse
	{
		void JsonResponse(JSONObject object, boolean Success);
	}
}