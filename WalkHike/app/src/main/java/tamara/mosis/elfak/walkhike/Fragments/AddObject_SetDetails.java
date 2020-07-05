package tamara.mosis.elfak.walkhike.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import tamara.mosis.elfak.walkhike.Activities.AddNewObjectActivity;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.FriendshipData;
import tamara.mosis.elfak.walkhike.modeldata.User;

public class AddObject_SetDetails extends Fragment implements View.OnClickListener {

    private AddNewObjectActivity myParentActivity;
    private Spinner share_with_select_user;
    private CheckBox cbx_public;
    private String sharedWithUsername;
    private boolean isPublic;
    private ArrayList<String> usernames;

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

        cbx_public = (CheckBox) view.findViewById(R.id.cbxMakePublic);

        share_with_select_user = view.findViewById(R.id.share_with_select_user);
        String email = myParentActivity.getLoggedUserEmail();
        ArrayList<User> friends = FriendshipData.getInstance().GetUserFriends(email);

        usernames = new ArrayList<>();
        usernames.add("");
        for (int i = 0; i < friends.size(); i++) {
            usernames.add(friends.get(i).username);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, usernames);
        share_with_select_user.setAdapter(spinnerAdapter);

        if (!isPublic) {
            sharedWithUsername = myParentActivity.getSharedWithUsername();
            if (sharedWithUsername != null) {
                int position = usernames.indexOf(sharedWithUsername);
                share_with_select_user.setSelection(position);
            }
            share_with_select_user.setEnabled(true);
        }
        else {
            sharedWithUsername = null;
            share_with_select_user.setEnabled(false);
            share_with_select_user.setSelection(0);
        }

        cbx_public.setChecked(isPublic);
        cbx_public.setOnClickListener(this);
    }

    public String getSharedWithUsername() {
        if (cbx_public.isChecked())
            sharedWithUsername = null;
        else
        {
            //sharedWithUsername = usernames.get(share_with_select_user.getSelectedItemPosition());
            sharedWithUsername = (String) share_with_select_user.getSelectedItem();
        }

        return sharedWithUsername;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cbxMakePublic) {
            if (cbx_public.isChecked()) {
                share_with_select_user.setSelection(0);
                share_with_select_user.setEnabled(false);
                isPublic = true;
            } else {
                share_with_select_user.setEnabled(true);
                int position = usernames.indexOf(sharedWithUsername);
                share_with_select_user.setSelection(position);
                isPublic = false;
            }
        }
    }
}
