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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText NombreR, CorreoR, contrasenaR, ConfirmarContrasenaR;
    Button RegistrarR;
    TextView TengounaCuentaTxt;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    String nombre = "", correo = "", password = "", Confirmarpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        NombreR = findViewById(R.id.NombreR);
        CorreoR = findViewById(R.id.CorreoR);
        contrasenaR = findViewById(R.id.contrasenaR);
        ConfirmarContrasenaR = findViewById(R.id.ConfirmarContrasenaR);
        RegistrarR = findViewById(R.id.RegistrarR);
        TengounaCuentaTxt = findViewById(R.id.TengounaCuentaTxt);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        RegistrarR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();

            }
        });
        TengounaCuentaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }

    private void validarDatos(){
        nombre = NombreR.getText().toString();
        correo = CorreoR.getText().toString();
        password = contrasenaR.getText().toString();
        Confirmarpassword = ConfirmarContrasenaR.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Confirmarpassword)) {
            Toast.makeText(this, "Confirme contraseña", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(Confirmarpassword)){
            Toast.makeText(this, "La contraseña no es igual", Toast.LENGTH_SHORT).show();
        }
        else{
            crearcuenta();
        }
    }
    private void crearcuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();


        //crear usuario en Firebase

        firebaseAuth.createUserWithEmailAndPassword(correo, password).
                addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                guardarinformacion();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void guardarinformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();

        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(Datos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Registro.this, menuprincipal.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}