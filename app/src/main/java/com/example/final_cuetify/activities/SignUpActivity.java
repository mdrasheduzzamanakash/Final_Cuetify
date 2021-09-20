package com.example.final_cuetify.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.databinding.ActivitySignInBinding;
import com.example.final_cuetify.databinding.ActivitySignUpBinding;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.textSignIn.setOnClickListener(v-> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v-> {
            if(isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.lauoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        user.put(Constants.KEY_UNI_ID, binding.inputUniId.getText().toString());
        user.put(Constants.KEY_PHONE, binding.inputPhone.getText().toString());
        if(binding.radioStudent.isChecked()) {
            user.put(Constants.KEY_STATUS, "student");
        } else if(binding.radioFaculty.isChecked()) {
            user.put(Constants.KEY_STATUS, "faculty");
        } else {
            user.put(Constants.KEY_STATUS, "cr");
        }
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    preferenceManager.putString(Constants.KEY_PHONE, binding.inputPhone.getText().toString());
                    preferenceManager.putString(Constants.KEY_UNI_ID, binding.inputUniId.getText().toString());
                    if(binding.radioStudent.isChecked()) {
                        preferenceManager.putString(Constants.KEY_STATUS, "student");
                    } else if(binding.radioFaculty.isChecked()) {
                        preferenceManager.putString(Constants.KEY_STATUS, "faculty");
                    } else {
                        preferenceManager.putString(Constants.KEY_STATUS, "cr");
                    }

                    // cleaning part
                    binding.inputName.setText("");
                    binding.inputEmail.setText("");
                    binding.inputPassword.setText("");
                    binding.inputUniId.setText("");
                    binding.inputPhone.setText("");
                    binding.inputConfirmPassword.setText("");

                    // activity shifting
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(exception -> {
                        loading(false);
                        showToast(exception.getMessage());
                });
    }


    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);binding.imageProfile.setImageBitmap(bitmap);
                            binding.addTextImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private Boolean isValidSignUpDetails() {
        if(encodedImage == null) {
            showToast("Select Profile Image");
            return false;
        } else if(binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        } else if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid Email Input");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Please Provide Password");
            return false;
        } else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Please Confirm your Password");
            return false;
        } else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password & Confirm password must be same");
            return false;
        } else if(binding.inputPhone.getText().toString().isEmpty()){
            showToast("Please provide phone number");
            return false;
        } else if(binding.inputUniId.getText().toString().isEmpty()) {
            showToast("please provide your university id");
            return false;
        } else {
            return true;
        }
    }
    private void loading(Boolean isLoading){
        if(isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBarSave.setVisibility(View.VISIBLE);
        } else {
            binding.progressBarSave.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}