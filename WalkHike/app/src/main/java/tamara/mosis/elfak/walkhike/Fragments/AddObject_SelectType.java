package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tamara.mosis.elfak.walkhike.Activities.AddNewObjectActivity;
import tamara.mosis.elfak.walkhike.R;

public class AddObject_SelectType extends Fragment implements View.OnClickListener {

    int type;
    AddNewObjectActivity myParentActivity;
    ImageView message;
    ImageView checkpoint;
    ImageView photo;
    ImageView emoji;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_object__select_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        message = view.findViewById(R.id.object_type_message);
        checkpoint = view.findViewById(R.id.object_type_checkpoint);
        photo = view.findViewById(R.id.object_type_photo);
        emoji = view.findViewById(R.id.object_type_emoji);

        message.setOnClickListener(this);
        checkpoint.setOnClickListener(this);
        photo.setOnClickListener(this);
        emoji.setOnClickListener(this);

        myParentActivity = (AddNewObjectActivity) getActivity();
        if (myParentActivity != null)
            this.type = myParentActivity.getObjectType();
        else
            this.type = 0;

        switch (this.type) {
            case 1: message.setBackgroundColor(getResources().getColor(R.color.colorAccent)); break;
            case 2: checkpoint.setBackgroundColor(getResources().getColor(R.color.colorAccent)); break;
            case 3: photo.setBackgroundColor(getResources().getColor(R.color.colorAccent)); break;
            case 4: emoji.setBackgroundColor(getResources().getColor(R.color.colorAccent)); break;
            default: break;
        }

        //super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.object_type_message: {
                type = 1;
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                checkpoint.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                photo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                emoji.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            }
            case R.id.object_type_checkpoint:
            {
                type = 2;
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                message.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                photo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                emoji.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            }
            case R.id.object_type_photo: {
                type = 3;
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                message.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                checkpoint.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                emoji.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            }
            case R.id.object_type_emoji: {
                type = 4;
                v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                message.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                checkpoint.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                photo.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
            }
        }

        myParentActivity.setObjectType(type);
    }
}
