package com.example.edumantradoubts.questionsandanswers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Answer;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.Structure.Teacher;
import com.example.edumantradoubts.adapters.WriteAnswerAdapter;
import com.example.edumantradoubts.gettingimages.UploadImages;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.grpc.Context;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.app.Activity.RESULT_OK;
import static com.example.edumantradoubts.gettingimages.UploadImages.QUESTIONS;

public class WriteAnswerFragment extends Fragment {

    private Uri filePath;
    public static ArrayList<String> ansImagePaths = new ArrayList<>();
    public static ArrayList<Uri> ansImagesUri = new ArrayList<>();
    public static final int GALLERY_REQUEST_CODE = 105;
    Question question;
    String docId, imageStorageDirectory;
    Teacher teacher;

    Button delete, add, publish;
    ImageButton gallery;
    ImageView image;
    EditText description;
    RecyclerView mRecyclerView;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_answer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gallery = view.findViewById(R.id.ib_button_gallery);
        image = view.findViewById(R.id.iv_answer_image_from_gallery);
        delete = view.findViewById(R.id.answer_delete);
        add = view.findViewById(R.id.answer_add_image);
        publish = view.findViewById(R.id.answer_publish_answer);
        mRecyclerView = view.findViewById(R.id.rv_answer_images);
        description = view.findViewById(R.id.ans_desc);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Images...Please wait");

        SessionManager mManager = new SessionManager(getContext());
        teacher = mManager.getTeacherDetails();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        // Getting question
        Bundle bundle = getArguments();
        if(bundle==null) {
            Log.e("WRITE_ANS_FRAG", "NO QUESTION");
            return;
        }
        question = (Question) bundle.getParcelable("QUESTION");

        docId = teacher.getTeacherId() + "-" + question.getSubject() + "-" + System.currentTimeMillis();
        imageStorageDirectory = question.getSubject() + "/" + teacher.getTeacherId() + "/" + question.getQuestionId();

        mRecyclerView = view.findViewById(R.id.rv_answer_images);
        WriteAnswerAdapter mAdapter = new WriteAnswerAdapter(ansImagesUri, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(image);
        pAttacher.update();

        gallery.setOnClickListener(new View.OnClickListener() {
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
                image.setImageResource(R.mipmap.ic_launcher);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath==null) {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                String imagePath = imageStorageDirectory + "/" + System.currentTimeMillis();
                image.setImageResource(R.mipmap.ic_launcher);

                ansImagesUri.add(filePath);
                filePath = null;
                ansImagePaths.add(imagePath);
                mAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "Image Added successfully", Toast.LENGTH_SHORT).show();

//
//                StorageReference ref = storageReference.child(imagePath);
//
//                ProgressDialog progressDialog = new ProgressDialog(getContext());
//                progressDialog.setTitle("Uploading...Please wait");
//                progressDialog.show();
//
//                ref.putFile(filePath)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                progressDialog.dismiss();
//                                ansImagePaths.add(imagePath);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(), "Image not uploaded, try again...", Toast.LENGTH_LONG).show();
//                            }
//                        });

            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Description Field cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(calendar.get(Calendar.MONTH)+1); // As month is 0 based
                String year = String.valueOf(calendar.get(Calendar.YEAR));

                Answer answer = new Answer(teacher.getTeacherId(), teacher.getName(), question.getSubject(),
                        description.getText().toString(),  date, month, year,
                        ansImagePaths, question.getQuestionId(), docId);

                // Adding the document to answers collection
                db.collection("Answers").document(docId)
                        .set(answer)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.e("ANSWER DOCUMENT", "ADDED SUCESS");
                                }
                            }
                        });

                // Adding the docId to question document
                Map<String, Object> map = new HashMap<>();
                map.put("answers", FieldValue.arrayUnion(docId));
                db.collection("Questions").document(question.getQuestionId()).update(map);
                List<String> temp = question.getAnswers();
                temp.add(docId);
                question.setAnswers(temp);

                // Incrementing number of answers in questions
                db.collection(QUESTIONS).document(answer.getQuestionId())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    Question question = task.getResult().toObject(Question.class);
                                    int curAnsCount = question.getNumAns();
                                    Map<String, Object> replace = new HashMap<>();
                                    replace.put("numAns", curAnsCount+1);
                                    db.collection(QUESTIONS).document(answer.getQuestionId()).update(replace);
                                    question.setNumAns(question.getNumAns()+1);
                                }
                            }
                        });


                // Uploading the images haha
                uploadAllImages(0);

            }
        });

    }

    void refresh() {
        ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().
                detach(new AnswerImagesFragment()).attach(new AnswerImagesFragment()).commit();
    }

    void uploadAllImages(int pos) {
        if(pos==0) progressDialog.show();
        if(pos==ansImagePaths.size()) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Images Uploaded Successfully", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "Answer Uploaded Successfully", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getContext(), ViewQuestionsComplete.class);
            in.putExtra("QUESTION", question);
            startActivity(in);
            requireActivity().finish();
            return;
        }
        progressDialog.setMessage((pos+1) + "/" + ansImagePaths.size() + " uploaded");
        StorageReference ref = storageReference.child(ansImagePaths.get(pos));
        ref.putFile(ansImagesUri.get(pos))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadAllImages(pos+1);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Image not uploaded, try again...", Toast.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK) {
            filePath = data.getData();
            image.setImageURI(filePath);
        }
    }
}