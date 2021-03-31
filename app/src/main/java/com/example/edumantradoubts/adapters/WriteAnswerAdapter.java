package com.example.edumantradoubts.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.gettingimages.UploadImages;
import com.example.edumantradoubts.questionsandanswers.WriteAnswerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class WriteAnswerAdapter extends RecyclerView.Adapter<WriteAnswerAdapter.ViewHolder> {

    Context context;
    ArrayList<Uri> ansImagePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    public WriteAnswerAdapter(ArrayList<Uri> ansImagePath, Context context) {
        this.context = context;
        this.ansImagePath = ansImagePath;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public WriteAnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_write_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WriteAnswerAdapter.ViewHolder holder, int position) {
        final int[] pushed = {0};
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        holder.hide.setLayoutParams(lp);
        /**
         * TODO: Implement showing and hiding of image on click of bar: DONE
         */

        holder.showHideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushed[0]++;
                if(pushed[0]%2==1) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    holder.hide.setLayoutParams(lp);
                    holder.arrow.setImageResource(R.drawable.ic_arrow_up);
                }
                else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    holder.hide.setLayoutParams(lp);
                    holder.arrow.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

//        String path = ansImagePath.get(position);
//        StorageReference filePath = storageReference.child(path);
//        try {
//            String[] token = path.split("/");
//            final File localFile =File.createTempFile(token[3], "") ;
//            filePath.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            PhotoViewAttacher pAttacher;
//                            pAttacher = new PhotoViewAttacher(holder.image);
//                            pAttacher.update();
//                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                            localFile.deleteOnExit();
//                            holder.image.setImageBitmap(bitmap);
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, "Cannot load images, check internet", Toast.LENGTH_LONG).show();
//                        }
//                    });
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        Uri uri = ansImagePath.get(position);
        holder.image.setImageURI(uri);

        holder.positionOfImage.setText("Image " + (position+1) + "/" + ansImagePath.size());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteAnswerFragment.ansImagePaths.remove(position);
                WriteAnswerFragment.ansImagesUri.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, WriteAnswerFragment.ansImagesUri.size());
//                ProgressDialog progressDialog = new ProgressDialog(context);
//                progressDialog.setTitle("Deleting...Please wait");
//                progressDialog.show();
//
//                storageReference.child(path)
//                        .delete()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                progressDialog.dismiss();
//                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                WriteAnswerFragment.ansImagePaths.remove(position);
//                                notifyItemRemoved(position);
//                                notifyItemRangeChanged(position, WriteAnswerFragment.ansImagePaths.size());
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return ansImagePath.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout showHideImage, hide;
        ImageView arrow, image;
        TextView positionOfImage;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showHideImage = itemView.findViewById(R.id.answer_show_hide_image);
            arrow = itemView.findViewById(R.id.answer_arrow);
            image = itemView.findViewById(R.id.answer_image);
            positionOfImage = itemView.findViewById(R.id.ans_position_of_image);
            delete = itemView.findViewById(R.id.answer_delete_image);
            hide = itemView.findViewById(R.id.hide_rest);
            if(hide==null) Log.e("HIDE", "NULL");
            else Log.e("HIDE", "NOT NULL");
        }
    }
}
