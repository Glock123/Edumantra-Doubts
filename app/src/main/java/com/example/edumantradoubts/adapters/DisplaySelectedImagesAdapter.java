package com.example.edumantradoubts.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.gettingimages.UploadImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DisplaySelectedImagesAdapter extends RecyclerView.Adapter<DisplaySelectedImagesAdapter.ViewHolder> {

    private final Context context;
    FirebaseStorage storage;
    StorageReference storageReference;

    public DisplaySelectedImagesAdapter(Context context) {
        this.context = context;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_to_display_upload_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Getting path and reference to image
        String path = UploadImages.images.get(position);
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
                            holder.imageFromWeb.setImageBitmap(bitmap);
                            PhotoViewAttacher pAttacher;
                            pAttacher = new PhotoViewAttacher(holder.imageFromWeb);
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

        holder.toDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Deleting from the QuestionImages path
                String path = UploadImages.images.get(position);

                // Have to also delete from firebase storage
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Deleting...Please wait");
                progressDialog.show();

                storageReference.child(path)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                UploadImages.images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, UploadImages.images.size());
            }
        });

    }


    @Override
    public int getItemCount() {
        return UploadImages.images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageFromWeb;
        public Button toDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFromWeb = itemView.findViewById(R.id.image_from_web);
            toDelete = itemView.findViewById(R.id.delete_image_from_web);
        }


    }

}
