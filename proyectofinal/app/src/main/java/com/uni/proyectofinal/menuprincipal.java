package com.uni.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class menuprincipal extends AppCompatActivity {
    Button cerrarseion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);

        cerrarseion = findViewById(R.id.cerrarsesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        cerrarseion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saliraplicacion();
            }
        });
    }

    private void saliraplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(menuprincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_LONG).show();
    }
}