package com.example.edumantradoubts.gettingimages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.example.edumantradoubts.student.StudentNavActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.senab.photoview.PhotoViewAttacher;

public class UploadImages extends AppCompatActivity {

    private ImageButton upload;
    private Button delete, uploadToFirebase, publishQuestion;
    private ImageView imageToUpload;

    public static final int GALLERY_REQUEST_CODE = 105;

    public static ArrayList<String> images;
    private String questionDocumentId;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Question currentQuestion;

    public Uri filePath;
    private Context context;

    DisplaySelectedImagesFragment displaySelectedImagesFragment = new DisplaySelectedImagesFragment();

    public static String QUESTIONS = "Questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);
        getSupportActionBar().hide();

        context = this;

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        upload = findViewById(R.id.upload);
        imageToUpload = findViewById(R.id.selected_image);
        delete = findViewById(R.id.delete);
        uploadToFirebase = findViewById(R.id.upload_to_web);
        publishQuestion = findViewById(R.id.publish_question);

        // Getting the Question object
        SessionManager mManager = new SessionManager(this);
        currentQuestion = mManager.getStoredQuestion();
        mManager.destroyQuestion();

        uploadToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToWeb();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image from..."), GALLERY_REQUEST_CODE);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = null;
                imageToUpload.setImageResource(R.mipmap.ic_launcher);
            }
        });

        publishQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion.setQuestionImages(images);
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH)+1; // As month is 0 based
                int year = calendar.get(Calendar.YEAR);

                currentQuestion.setDate(String.valueOf(date));
                currentQuestion.setMonth(String.valueOf(month));
                currentQuestion.setYear(String.valueOf(year));

                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Uploading Question...");
                progressDialog.show();


                CollectionReference collectionReference = db.collection(QUESTIONS);
                collectionReference.document(currentQuestion.getQuestionId())
                        .set(currentQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Question added to collection "Questions"
                                progressDialog.dismiss();
                                Toast.makeText(UploadImages.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(UploadImages.this, StudentNavActivity.class);
                                startActivity(in);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UploadImages.this, "Some error occurred:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });



        images = new ArrayList<>();

        questionDocumentId = currentQuestion.getQuestionId();
        //Toast.makeText(this, questionDocumentId, Toast.LENGTH_SHORT).show();

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageToUpload);
        pAttacher.update();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_view_selected_images, displaySelectedImagesFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK) {
            filePath = data.getData();
            imageToUpload.setImageURI(filePath);

        }
    }

    private void uploadToWeb() {
        if(filePath==null) {
            Toast.makeText(this, "No Image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = currentQuestion.getSubject() + "/" + currentQuestion.getStudentId() + "/" + questionDocumentId + "/" + System.currentTimeMillis();

        StorageReference ref = storageReference.child(imagePath);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...Please wait");
        progressDialog.show();

        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadImages.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        images.add(imagePath);

                        // TODO: refresh the fragment containing the uploaded images : DONE
                        refresh();
                        Log.e("SIZEOFIMAGES", String.valueOf(images.size()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadImages.this, "Image not uploaded, try again...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void refresh() {
        getSupportFragmentManager().beginTransaction().detach(displaySelectedImagesFragment).attach(displaySelectedImagesFragment).commit();
    }


}