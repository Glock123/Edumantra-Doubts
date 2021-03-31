package com.example.edumantradoubts.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImagesFromStorageAdapter extends RecyclerView.Adapter<ImagesFromStorageAdapter.ViewHolder>{

    String[] paths;
    Context context;
    FirebaseStorage storage;
    StorageReference storageReference;

    public ImagesFromStorageAdapter(String[] paths, Context context) {
        this.paths = paths;
        this.context = context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_images_from_storage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int[] pushed = {0};
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        holder.image.setLayoutParams(lp);
        /**
         * TODO: Implement showing and hiding of image on click of bar: DONE
         */

        holder.showHideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushed[0]++;
                if(pushed[0]%2==1) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    holder.image.setLayoutParams(lp);
                    holder.arrow.setImageResource(R.drawable.ic_arrow_up);
                }
                else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    holder.image.setLayoutParams(lp);
                    holder.arrow.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        String path = paths[position];
        StorageReference filePath = storageReference.child(path);
        try {
            String[] token = path.split("/");
            final File localFile =File.createTempFile(token[2], "") ;
            filePath.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            localFile.deleteOnExit();
                            holder.image.setImageBitmap(bitmap);
                            PhotoViewAttacher pAttacher;
                            pAttacher = new PhotoViewAttacher(holder.image);
                            pAttacher.update();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Cannot load images, check internet", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        holder.positionOfImage.setText("Image " + (position+1) + "/" + paths.length);

    }

    @Override
    public int getItemCount() {
        return paths.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout showHideImage;
        ImageView arrow, image;
        TextView positionOfImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showHideImage = itemView.findViewById(R.id.show_hide_image);
            arrow = itemView.findViewById(R.id.arrow);
            image = itemView.findViewById(R.id.image_from_storage);
            positionOfImage = itemView.findViewById(R.id.position_of_image);
        }
    }
}
