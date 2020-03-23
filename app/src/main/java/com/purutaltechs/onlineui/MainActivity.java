package com.purutaltechs.onlineui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseFirestore db;
    LinearLayout layout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        layout = findViewById(R.id.container);
        context = this;
        db.collection("ui").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    String viewClass = snapshot.getString("view");
                    List<HashMap<String, String>> methodNames = (List<HashMap<String, String>>) snapshot.get("methods");

                    try {
                        if (viewClass != null) {
                            Class view = Class.forName(viewClass);

                            Constructor constructor = view.getConstructor(Context.class);
                            View baseView = (View) constructor.newInstance(getBaseContext());

                            for (HashMap<String, String> map : methodNames) {
                                Method method = view.getMethod(map.get("name"), Class.forName(map.get("paramTypes")));
                                String value = map.get("paramValues");
                                if (!value.isEmpty())
                                    method.invoke(baseView, value);
                                else
                                    method.invoke(baseView, ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
                            }
                            layout.addView(baseView);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
