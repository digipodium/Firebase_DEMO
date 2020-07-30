package com.example.firebase_demo.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebase_demo.R;
import com.example.firebase_demo.models.Animal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private ProgressBar pbar;
    private FloatingActionButton fabSave;
    private EditText editName;
    private EditText editDesc;
    private FirebaseFirestore db;

    public static final String ANIMALS = "animals";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabSave = view.findViewById(R.id.fabSave);
        pbar = view.findViewById(R.id.pbar);
        editName = view.findViewById(R.id.editName);
        editDesc = view.findViewById(R.id.editDesc);
        db = FirebaseFirestore.getInstance();

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(1);
                String desc = editDesc.getText().toString();
                String name = editName.getText().toString();

                db.collection(ANIMALS).add(new Animal(name, desc)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUI(2);
                    } else {
                        String error = task.getException().getMessage();
                        Snackbar.make(fabSave, error, BaseTransientBottomBar.LENGTH_LONG).show();
                        updateUI(0);
                    }
                });

            }
        });
    }

    private void updateUI(int i) {
        if (i == 1) {
            fabSave.setEnabled(false);
            pbar.setVisibility(View.VISIBLE);
        } else if (i == 2) {
            fabSave.setEnabled(true);
            pbar.setVisibility(View.GONE);
            editDesc.setText("");
            editName.setText("");
        } else if (i == 0) {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }
}