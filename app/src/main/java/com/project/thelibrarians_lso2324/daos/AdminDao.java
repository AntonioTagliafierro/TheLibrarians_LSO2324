package com.project.thelibrarians_lso2324.daos;



import static com.project.thelibrarians_lso2324.utils.Utils.API_BASE_URL;


import android.content.Context;

import com.android.volley.toolbox.JsonObjectRequest;
import com.project.thelibrarians_lso2324.events.AuthenticationErrorEvent;
import com.project.thelibrarians_lso2324.events.LoginEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import com.project.thelibrarians_lso2324.utils.VolleyRequestHandler;



public class AdminDao implements AdminDaoInterface {

    @Override
    public TokenPayload login(String email, String password, Context context) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                API_BASE_URL + "login", obj -> {
            try {
                TokenPayload tokenPayload = new TokenPayload(obj.getString("token"));
                LoginEvent userLoginEvent = new LoginEvent(tokenPayload);

                EventBus.getDefault().post(userLoginEvent);
            } catch (Exception e) {
                EventBus.getDefault().post(new AuthenticationErrorEvent(e.getMessage()));

            }
        }, (error) -> {
            EventBus.getDefault().post(new AuthenticationErrorEvent("Failed to login"));
        }) {

            @Override
            public int getMethod() {
                return Method.POST;
            }

            @Override
            public byte[] getBody() {
                JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    body.put("password", password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return body.toString().getBytes();
            }
        };

        VolleyRequestHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return null;
    }
}
