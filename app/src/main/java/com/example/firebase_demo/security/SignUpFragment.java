package com.example.firebase_demo.security;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebase_demo.MainActivity;
import com.example.firebase_demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpFragment extends Fragment {

    private TextInputEditText editEmail;
    private TextInputEditText editPass;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressBar pbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public void updateUI(int status) {
        if (status == 1) {
            pbar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else if (status == 0) {
            pbar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);

        } else if (status == 2) {
            pbar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            /*Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i); // your first fragment will be displayed from MainActivity
            getActivity().finish(); // kill the auth activity*/
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Security")
                                .setMessage("We have send you the verification mail, please check email")
                                .setNeutralButton("OK", null)
                                .create()
                                .show();
                    } else {
                        String errorMsg = task.getException().getMessage();
                        editEmail.setError(errorMsg);
                        editEmail.requestFocus();
                    }
                }
            });
            mAuth.signOut(); // logout the user out
        }
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        editEmail = v.findViewById(R.id.editEmail);
        editPass = v.findViewById(R.id.editPassword);
        btnRegister = v.findViewById(R.id.btnRegister);
        pbar = v.findViewById(R.id.pbar);

        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(view -> {
            updateUI(1);
            String email = editEmail.getText().toString();
            String password = editPass.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getActivity(), "registered", Toast.LENGTH_SHORT).show();
                    updateUI(2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(btnRegister, "ERROR: " + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                    updateUI(0);
                }
            });
        });
    }
}