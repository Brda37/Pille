package com.uni.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText Correol, contrasenaL;
    Button Btn_login;
    TextView Btn_nuevo;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String correo = "", password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Correol = findViewById(R.id.correol);
        contrasenaL = findViewById(R.id.contrasenaL);
        Btn_login = findViewById(R.id.Btn_login);
        Btn_nuevo = findViewById(R.id.Btn_nuevo);

        firebaseAuth = firebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor...");
        progressDialog.setCanceledOnTouchOutside(false);

        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });

        Btn_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
    }

    private void ValidarDatos() {

        correo = Correol.getText().toString();
        password = contrasenaL.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo invalido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
        }
        else{
            loginusuario();
        }
    }

    private void loginusuario() {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(Login.this, menuprincipal.class));
                    Toast.makeText(Login.this, "Bienvenido(a) " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Verifique el correo y contraseña son correctos", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "" +  e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}