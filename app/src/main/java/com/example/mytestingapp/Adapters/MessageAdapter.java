package com.example.mytestingapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytestingapp.Classes.Chats;
import com.example.mytestingapp.Classes.Provider;
import com.example.mytestingapp.Classes.Seeker;
import com.example.mytestingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {


    Context context;
    List<Chats> chatslist;
    String receiverId;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference1;
    int flag = 0;
    ImageView imageView;


    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static final int MESSAGE_RIGHT = 0;
    public static final int MESSAGE_LEFT = 1;



    public MessageAdapter(Context context, List<Chats> chatslist, String receiverId)
    {
        this.context = context;
        this.chatslist = chatslist;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        if (viewType == MESSAGE_RIGHT)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MyViewHolder(view);


        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        Chats chats  = chatslist.get(position);

        holder.messagetext.setText(chats.getMessage());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Providers").child(receiverId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {

                    flag = 1;

                    Provider provider = snapshot.getValue(Provider.class);

                    storageReference = FirebaseStorage.getInstance().getReference().child("images/"+receiverId);
                    final Bitmap[] bitmap = new Bitmap[1];
                    try{
                        File localfile = File.createTempFile( provider.getId(),".jpg");
                        storageReference.getFile(localfile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                        holder.imageView.setImageBitmap(bitmap[0]);
                                        provider.setImageBitmap(bitmap[0]);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                                holder.imageView.setImageBitmap(bitmap[0]);
                                provider.setImageBitmap(bitmap[0]);
                            }
                        });

                    }
                    catch (Exception e){
                        bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                        holder.imageView.setImageBitmap(bitmap[0]);
                        provider.setImageBitmap(bitmap[0]);
                    }

                    holder.imageView.setImageBitmap(provider.getImageBitmap());



                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if (flag == 0)
        {

            reference1 = FirebaseDatabase.getInstance().getReference("Seekers").child(receiverId);


            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists())
                    {

                        Seeker seeker = snapshot.getValue(Seeker.class);

                        storageReference = FirebaseStorage.getInstance().getReference().child("images/"+receiverId);
                        final Bitmap[] bitmap = new Bitmap[1];
                        try{
                            File localfile = File.createTempFile(seeker.getId(),".jpg");
                            storageReference.getFile(localfile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            bitmap[0] = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                            holder.imageView.setImageBitmap(bitmap[0]);
                                            seeker.setImageBitmap(bitmap[0]);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    bitmap[0] = BitmapFactory.decodeFile("defaultProfilePic.jpeg");
                                    holder.imageView.setImageBitmap(bitmap[0]);
                                    seeker.setImageBitmap(bitmap[0]);
                                }
                            });

                        }
                        catch (Exception e){
                            bitmap[0] = BitmapFactory.decodeFile("app/defaultProfilePic.jpeg");
                            holder.imageView.setImageBitmap(bitmap[0]);
                            seeker.setImageBitmap(bitmap[0]);
                        }

                        holder.imageView.setImageBitmap(seeker.getImageBitmap());



                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }





    }

    @Override
    public int getItemCount()
    {
        return chatslist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView messagetext;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            messagetext = itemView.findViewById(R.id.show_message);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;

        if (chatslist.get(position).getSender().equals(user.getUid()))
        {
            return MESSAGE_RIGHT;
        }
        else
        {
            return MESSAGE_LEFT;

        }

    }
}
