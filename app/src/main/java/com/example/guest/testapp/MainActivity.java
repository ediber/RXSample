package com.example.guest.testapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.AbstractCollection;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends ActionBarActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText mail;
    private EditText city;
    private EditText password1;
    private EditText password2;
    private View login;
    private ImageView check;
    private boolean isChecked;
    private TextView terms;
    private CompositeSubscription _compositeSubscription = new CompositeSubscription();
    private UserParent userParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        mail = (EditText)findViewById(R.id.mail);
        city = (EditText)findViewById(R.id.city);
        password1 = (EditText)findViewById(R.id.password1);
        password2 = (EditText)findViewById(R.id.password2);
        terms = (TextView)findViewById(R.id.terms);


        check = (ImageView)findViewById(R.id.check);
        login = findViewById(R.id.login);



        RxView.clicks(login).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(validate()){
                     userParent = new UserParent(firstName.getText().toString(), lastName.getText().toString(), mail.getText().toString(), city.getText().toString(),
                            password1.getText().toString());
//                    sendLogin(userParent);
                    startVolleyRequest();
                }
            }
        });

        RxView.clicks(check).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(isChecked){
                    check.setImageResource(R.drawable.btn_round);
                    isChecked = false;
                } else {
                    check.setImageResource(R.drawable.btn_round_selected);
                    isChecked = true;
                }
            }
        });



    }

/*    private void sendLogin(UserParent userParent) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url1 = "http://52.16.100.4/Mobile/RegistrationRequest";

        JSONObject jsonObject = null;
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userParent);
        try {
             jsonObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url1, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //...
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //...
                    }
                })
        {

        };


        queue.add(request);
    }*/

    private boolean validate() {
        boolean isLegal = true;
        if(firstName.getText().toString().equals("")){
            firstName.setError("you must enter your first name");
            isLegal = false;
        }
        if(lastName.getText().toString().equals("")){
            lastName.setError("you must enter your last name");
            isLegal = false;
        }
        if(mail.getText().toString().equals("")){
            mail.setError("you must enter your e mail");
            isLegal = false;
        }
        if(city.getText().toString().equals("")){
            city.setError("you must enter your city");
            isLegal = false;
        }
        if(password1.getText().toString().equals("")){
            password1.setError("you must enter your password");
            isLegal = false;
        }
        if(password2.getText().toString().equals("")){
            password2.setError("you must enter your password");
            isLegal = false;
        }
        if(! isChecked){
            terms.setError("Please confirm the term of use");
            isLegal = false;
        }
        return isLegal;
    }



    private void startVolleyRequest() {
        _compositeSubscription.add(newGetRouteData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        VolleyError cause = (VolleyError) e.getCause();
                        String s = new String(cause.networkResponse.data, Charset.forName("UTF-8"));


                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {

                    }
                }));
    }

    private Observable<JSONObject> newGetRouteData() {
        return Observable.defer(new Func0<Observable<JSONObject>>() {
            @Override
            public Observable<JSONObject> call() {
                try {
                    return Observable.just(getRouteData());
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("routes", e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    private JSONObject getRouteData() throws ExecutionException, InterruptedException {

        JSONObject jsonObject = null;
        Gson gson = new Gson();
        String jsonInString = gson.toJson(userParent);
        try {
            jsonObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String url = "http://52.16.100.4/Mobile/RegistrationRequest";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,jsonObject, future, future);
        Volley.newRequestQueue(this).add(req);
        return future.get();
    }


}
