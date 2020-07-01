package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import tamara.mosis.elfak.walkhike.Activities.AddNewObjectActivity;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class AddObject_SetDetails extends Fragment implements View.OnClickListener {

    private AddNewObjectActivity myParentActivity;
    private EditText edit_share_with;
    private CheckBox cbx_public;
    private String sharedWithUsername;
    private boolean isPublic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_object__set_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        myParentActivity = (AddNewObjectActivity) getActivity();
        isPublic = myParentActivity.isPublic();

        edit_share_with = view.findViewById(R.id.edit_text_share_with);
        cbx_public = (CheckBox) view.findViewById(R.id.cbxMakePublic);

        if (!isPublic) {
            sharedWithUsername = myParentActivity.getSharedWithUsername();
            if (sharedWithUsername != null) {
                edit_share_with.setText(sharedWithUsername);
            }
            edit_share_with.setEnabled(true);
        }
        else {
            sharedWithUsername = null;
            edit_share_with.setText("");
            edit_share_with.setEnabled(false);
        }

        cbx_public.setChecked(isPublic);
        cbx_public.setOnClickListener(this);
    }

    public String getSharedWithUsername() {
        if (cbx_public.isChecked())
            sharedWithUsername = null;
        else
            sharedWithUsername = edit_share_with.getText().toString();

        return sharedWithUsername;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cbxMakePublic) {
            if (cbx_public.isChecked()) {
                sharedWithUsername = edit_share_with.getText().toString();
                edit_share_with.setEnabled(false);
                edit_share_with.setText("");
                isPublic = true;
            } else {
                edit_share_with.setEnabled(true);
                edit_share_with.setText(sharedWithUsername);
                isPublic = false;
            }
        }
    }
}
