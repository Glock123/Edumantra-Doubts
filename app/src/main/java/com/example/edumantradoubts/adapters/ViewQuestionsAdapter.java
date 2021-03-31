package com.example.edumantradoubts.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Answer;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.questionsandanswers.ViewQuestionsFragment;
import com.example.edumantradoubts.questionsandanswers.ViewQuestionsComplete;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

import static com.example.edumantradoubts.adapters.ViewAnswersAdapter.ANSWERS;
import static com.example.edumantradoubts.gettingimages.UploadImages.QUESTIONS;
import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.MATHS;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.SCIENCE;

public class ViewQuestionsAdapter extends RecyclerView.Adapter<ViewQuestionsAdapter.ViewHolder> {
    List<Question> questions;
    Context context;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;

    public ViewQuestionsAdapter(List<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public ViewQuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view_questions, parent, false);
        return new ViewQuestionsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewQuestionsAdapter.ViewHolder holder, int position) {

        // TODO: Make activities for viewing questions and it's answers : DONE

        SessionManager sessionManager = new SessionManager(context);

        // As teachers cannot delete the question
        if(sessionManager.getUserType().equals(TEACHERS)) {
            holder.buttonDelete.setVisibility(View.INVISIBLE);
        }

        Question currQuestion = questions.get(position);

        holder.cardLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ViewQuestionsComplete.class);
                in.putExtra("QUESTION", currQuestion);
                context.startActivity(in);
            }
        });

        holder.chapterNo.setText("Chapter " + currQuestion.getChapterNumber());
        if(currQuestion.getQuestionDescription().length() < 112) {
            holder.questionDescription.setText(currQuestion.getQuestionDescription());
        }
        else {
            holder.questionDescription.setText(currQuestion.getQuestionDescription().substring(0, 111) + "...");
        }

        holder.numberOfAttachedImages.setText("[ " + currQuestion.getQuestionImages().size() + " attached images]");

        if(currQuestion.getStandard().equals("10"))
            holder.standard.setText("Class X");
        else
            holder.standard.setText("Class IX");

        String ans="";
        if(currQuestion.getAnswers().size() > 0) {
            ans = currQuestion.getAnswers().size() + " Answers";
        }
        else {
            ans = "No Answers";
        }
        ans += "\n" + currQuestion.getDate() + "/" + currQuestion.getMonth() + "/" + currQuestion.getYear();
        holder.numberOfAnswers.setText(ans);


        if(currQuestion.getSubject().equals(MATHS)) {
            holder.cardLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.math_color));
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.math_color));
        }
        else if(currQuestion.getSubject().equals(SCIENCE)) {
            holder.cardLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.science_color));
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.science_color));
        }
        else {
            holder.cardLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.sst_color));
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.sst_color));

        }


        holder.authorInfo.setText(currQuestion.getStudentName() + "\n" + currQuestion.getStudentId());

        PopupMenu popup = new PopupMenu(context, holder.buttonDelete);
        popup.inflate(R.menu.delete_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.delete:
                        String currQuestionStudentId = ViewQuestionsFragment.questions.get(position).getStudentId();
                        if(!currQuestionStudentId.equals(sessionManager.getStudentDetails().getStudentId())) {
                            Toast.makeText(context, "Only the student who asked this question can delete it...", Toast.LENGTH_LONG).show();
                        }
                        else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("Want to delete this question?");
                            alertDialog.setMessage("Once deleted, cannot be recovered");
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * The author of question gives permission to delete it, hence we have to delete
                                     * a. Images of question
                                     * b. Documents of answers
                                     * c. Images of answers
                                     * d. Document of the question
                                     */

                                    deleteAnswersAndImages(currQuestion);

                                    // Deleting question from main database
                                    db.collection(QUESTIONS).document(currQuestion.getQuestionId())
                                            .delete();

                                    // Deleting all it's answer and images from main database
                                    deleteAnswersAndImages(currQuestion);

                                    // Updating the recyclerView
                                    ViewQuestionsFragment.questions.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, ViewQuestionsFragment.questions.size());

                                }
                            });
                            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
                }

                return true;
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });
    }

    void deleteAnswersAndImages(Question question) {
        /**
         * A. All question Images
         * B. All answer documents
         * C. All images from individual answer document
         *
         * STORE QUESTION AND ANSWER IMAGES IN SAME DIRECTORY
         */

        // Deleting the question images
        if(question.getQuestionImages().size()>0) {
            deleteImagesGivenPath(question.getQuestionImages(), 0);
        }

        // Deleting the answer documents and images
        if(question.getAnswers().size()>0) {
            deleteAnswers(question.getAnswers(), 0);
        }

    }

    void deleteAnswers(List<String> answerPaths, int currPos) {
        if(currPos==answerPaths.size()) return;
        String docId = answerPaths.get(currPos);
        db.collection(ANSWERS)
                .document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Answer ans = Objects.requireNonNull(task.getResult()).toObject(Answer.class);
                            assert ans != null;
                            if(ans.getAnswerImages().size()>0)
                                deleteImagesGivenPath(ans.getAnswerImages(), 0);
                        }
                        db.collection("Answers").document(docId).delete();
                        deleteAnswers(answerPaths, currPos+1);
                    }
                });
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

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buttonDelete, chapterNo, questionDescription, numberOfAttachedImages, numberOfAnswers, authorInfo, standard;
        LinearLayout cardLinearLayout;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonDelete = itemView.findViewById(R.id.text_button_delete);
            chapterNo = itemView.findViewById(R.id.chapter_no);
            questionDescription = itemView.findViewById(R.id.question_desc);
            numberOfAttachedImages = itemView.findViewById(R.id.number_of_attached_images);
            numberOfAnswers = itemView.findViewById(R.id.number_of_answers);
            authorInfo = itemView.findViewById(R.id.author_info);
            card = itemView.findViewById(R.id.card_view_questions);
            cardLinearLayout = itemView.findViewById(R.id.card_view_questions_linear_layout);
            standard = itemView.findViewById(R.id.standard_no);
        }


    }
}
