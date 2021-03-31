package com.example.edumantradoubts.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Answer;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.Structure.Teacher;
import com.example.edumantradoubts.dispimagesanswers.ImagesFromStorageFragment;
import com.example.edumantradoubts.questionsandanswers.ViewAnswersFragment;
import com.example.edumantradoubts.questionsandanswers.ViewQuestionsComplete;
import com.example.edumantradoubts.questionsandanswers.ViewQuestionsFragment;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.edumantradoubts.gettingimages.UploadImages.QUESTIONS;
import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;

public class ViewAnswersAdapter extends RecyclerView.Adapter<ViewAnswersAdapter.ViewHolder> {

    List<String> ansPath;
    Context context;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;

    public static String ANSWERS = "Answers";

    public ViewAnswersAdapter(ArrayList<String> ansPath, Context context) {
        this.ansPath = ansPath;
        this.context = context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAnswersAdapter.ViewHolder holder, int position) {
        final int[] pushed = {0};
        String docId = ansPath.get(position);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        holder.ansComplete.setLayoutParams(lp);
        db.collection(ANSWERS).document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Answer answer = task.getResult().toObject(Answer.class);
                            holder.teacherNameAndDate.setText(answer.getTeacherName() + " on " + answer.getDate() + "/" + answer.getMonth() + "/" + answer.getYear());
                            holder.ansDescription.setText(answer.getAnswerDescription());

                            holder.ansPreview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pushed[0]++;
                                    if(pushed[0]%2==1) {
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        holder.ansComplete.setLayoutParams(lp);
                                        holder.arrow.setImageResource(R.drawable.ic_arrow_up);
                                    }
                                    else {
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                        holder.ansComplete.setLayoutParams(lp);
                                        holder.arrow.setImageResource(R.drawable.ic_arrow_down);
                                    }
                                }
                            });

                            // Passing image paths to fragment

                                ImagesFromStorageFragment imageFrag = new ImagesFromStorageFragment();
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("IMAGE PATH", answer.getAnswerImages().toArray(new String[0]));
                                imageFrag.setArguments(bundle);
                                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fl_ans_images, imageFrag).commit();
                            // If above gives error try this: ((Activity)context).getFragmentManager();

                            SessionManager sessionManager = new SessionManager(context);
                            if(sessionManager.getUserType().equals(TEACHERS)) {
                                Teacher teacher = sessionManager.getTeacherDetails();
                                if(!answer.getTeacherId().equals(teacher.getTeacherId())) {
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                    holder.ansDelete.setLayoutParams(lp);
                                }
                                else {
                                    holder.ansDelete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                            alertDialog.setTitle("Want to delete your answer?");
                                            alertDialog.setMessage("Once deleted, cannot be recovered");
                                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    /**
                                                     * A. Delete from Questions document(From the array)
                                                     * B. Delete images of Answers
                                                     * C. Delete current answer document
                                                     *  Refresh the recyclerView
                                                     */

                                                    // A. Delete from Questions document
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("answers", FieldValue.arrayRemove(answer.getAnswerId()));
                                                    db.collection(QUESTIONS).document(answer.getQuestionId())
                                                            .update(map);

                                                    // B. Delete answer images
                                                    if(answer.getAnswerImages().size()>0)
                                                        deleteImagesGivenPath(answer.getAnswerImages(), 0);

                                                    // C. Delete this answer document
                                                    db.collection(ANSWERS)
                                                            .document(answer.getAnswerId())
                                                            .delete();

                                                    // D. Decrementing count of numAns in question document
                                                    db.collection(QUESTIONS).document(answer.getQuestionId())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.isSuccessful()) {
                                                                        Question question = task.getResult().toObject(Question.class);
                                                                        int curAnsCount = question.getNumAns();
                                                                        Map<String, Object> replace = new HashMap<>();
                                                                        replace.put("numAns", curAnsCount-1);
                                                                        db.collection(QUESTIONS).document(answer.getQuestionId()).update(replace);
                                                                    }
                                                                }
                                                            });

                                                    // Deleting from ViewQuestionComplete Fragment to0
                                                    List<String> temp = ViewQuestionsComplete.question.getAnswers();
                                                    temp.remove(position);
                                                    ViewQuestionsComplete.question.setAnswers(temp);

                                                    // Updating the recyclerView
                                                    ansPath.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, ansPath.size());
                                                }
                                            });
                                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    });
                                }
                            }
                            else {
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                                holder.ansDelete.setLayoutParams(lp);
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return ansPath.size();
    }

    void deleteImagesGivenPath(List<String> imagePaths, int currPos) {
        if(currPos==imagePaths.size()) return;
        String path = imagePaths.get(currPos);
        storageReference.child(path)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        deleteImagesGivenPath(imagePaths, currPos+1);
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ansPreview, ansComplete;
        TextView teacherNameAndDate, ansDescription;
        ImageView arrow;
        Button ansDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ansPreview = itemView.findViewById(R.id.ans_preview);
            teacherNameAndDate = itemView.findViewById(R.id.teacher_name_date);
            ansDescription = itemView.findViewById(R.id.ans_description);
            arrow = itemView.findViewById(R.id.arrow_view_answers);
            ansComplete = itemView.findViewById(R.id.ans_complete);
            ansDelete = itemView.findViewById(R.id.ans_delete);
        }
    }

}
