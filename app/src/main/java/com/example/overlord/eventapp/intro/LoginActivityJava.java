package com.example.overlord.eventapp.intro;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.example.overlord.eventapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityJava extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private String name;
    private String weddingSide;
    private String relation;
    private final boolean keyContact = false;
    private Image profilePhoto;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Button loginButton;
    private EditText userName;
    private Spinner userRelation;
    private Button uploadImage;
    private ImageView userProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.userName);
        userRelation = findViewById(R.id.userRelation);
        userProfileImage = findViewById(R.id.userProfilePhoto);
        uploadImage = findViewById(R.id.uploadProfilePhoto);
        loginButton = findViewById(R.id.buttonLogin);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = userName.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        findViewById(R.id.bride).setOnClickListener(view -> weddingSide = "Bride");
        findViewById(R.id.groom).setOnClickListener(view -> weddingSide = "Groom");


        userRelation.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRelation.setAdapter(adapter);

        uploadImage.setOnClickListener(view -> {

        });

        if(auth.getCurrentUser() != null) {
            startApp();
        } else {
            loginButton.setOnClickListener(view -> {

            });
        }
    }

    void startApp() {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        relation = adapterView.getItemAtPosition(i).toString();
    }
}
