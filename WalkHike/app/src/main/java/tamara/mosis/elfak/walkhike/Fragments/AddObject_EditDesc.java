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

public class AddObject_EditDesc extends Fragment {

    int type;
    AddNewObjectActivity myParentActivity;
    String desc;
    EditText edit_desc;
    TextView desc_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_object__edit_desc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        myParentActivity = (AddNewObjectActivity) getActivity();
        desc = myParentActivity.getDesc();

        type = myParentActivity.getObjectType();

        desc_text = view.findViewById(R.id.add_object_desc_textView);

        if (type == 1) {
            desc_text.setText("Insert your message: ");
        } else if (type == 2) {
            desc_text.setText("Insert name for your place: ");
        } else if (type == 3) {
            desc_text.setText("Insert photo description: ");
        } else if (type == 4) {
            desc_text.setText("Insert your reaction: ");
        }

        edit_desc = view.findViewById(R.id.add_object_desc);

        if (desc != null)
            edit_desc.setText(desc);

    }

    public String getDesc() {
        desc = edit_desc.getText().toString();
        return desc;
    }

}
