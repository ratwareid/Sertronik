package com.ratwareid.sertronik.activity.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ratwareid.sertronik.R;
import com.ratwareid.sertronik.helper.UniversalKey;
import com.ratwareid.sertronik.model.Category;

import java.io.IOException;

import static com.ratwareid.sertronik.helper.UniversalKey.PICK_IMAGE_REQUEST;

public class AddNewCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageCategory;
    EditText inputNameCategory, inputCodeCategory;
    Button buttonSave;

    Uri uri;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        initWidgets();

        imageCategory.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    private void initWidgets() {
        imageCategory = findViewById(R.id.imageCategory);
        inputNameCategory = findViewById(R.id.inputNameCategory);
        inputCodeCategory = findViewById(R.id.inputCodeCategory);
        buttonSave = findViewById(R.id.buttonAddCategory);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(UniversalKey.IMAGE_CATEGORY_DATABASE_PATH);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(imageCategory)) {
            addingImageCategory();
        }else if (v.equals(buttonSave)){
            uploadCategory();
        }
    }

    private void uploadCategory() {
        if (uri != null){
            StorageReference storageReferenceImage = storageReference.child(UniversalKey.IMAGE_CATEGORY_STORAGE_PATH + System.nanoTime() + "." + GetFileExtension(uri));
            storageReferenceImage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null){
                    if (taskSnapshot.getMetadata().getReference() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String categoryName = inputNameCategory.getText().toString();
                                String categoryCode = inputCodeCategory.getText().toString();

                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "Kategori Ditambahkan!", Toast.LENGTH_LONG).show();

                                @SuppressWarnings("VisibleForTests")
                                Category category = new Category(categoryName, categoryCode, uri.toString());

                                // Getting image upload ID.
                                String imageUploadId = databaseReference.push().getKey();

                                // Adding image upload id s child element into databaseReference.
                                databaseReference.child(imageUploadId).setValue(category);
                            }
                        });
                    }
                }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addingImageCategory() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Pilih Thumbnail Kategori"), PICK_IMAGE_REQUEST);
    }
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageCategory.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
