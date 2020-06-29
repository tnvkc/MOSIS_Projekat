package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tamara.mosis.elfak.walkhike.Activities.AddNewObjectActivity;
import tamara.mosis.elfak.walkhike.R;

public class AddObject_InsertPhoto extends Fragment {

    int type;
    AddNewObjectActivity myParentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_object__insert_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        myParentActivity = (AddNewObjectActivity) getActivity();

        type = myParentActivity.getObjectType(); //ovde uvek =3
    }

}
