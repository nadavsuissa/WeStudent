package com.project.westudentmain;

import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;

/**
 * this class making validation to all input from user
 */

public class Validation {

    public boolean Register(EditText user_email, EditText user_password, EditText user_firstName, EditText user_lastName, EditText user_userName, EditText user_university, EditText user_dgree,
                            String email, String password, String firstName, String lastName, String userName, String university, String dgree) {
        if (email.isEmpty()) {
            user_email.setError("Email is empty");
            user_email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user_email.setError("Enter the valid email address");
            user_email.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            user_password.setError("Enter a password");
            user_password.requestFocus();
            return false;
        } else if (password.length() < 6) {
            user_password.setError("Password length needs to be at least 6");
            user_password.requestFocus();
            return false;
        } else if (firstName.isEmpty()) {
            user_firstName.setError("Name is empty");
            user_firstName.requestFocus();
            return false;
        } else if (lastName.isEmpty()) {
            user_lastName.setError("last name is empty");
            user_lastName.requestFocus();
            return false;
        } else if (userName.isEmpty()) {
            user_userName.setError("username is empty");
            user_userName.requestFocus();
            return false;
        } else if (university.isEmpty()) {
            user_university.setError("university is empty");
            user_university.requestFocus();
            return false;
        } else if (dgree.isEmpty()) {
            user_dgree.setError("degree is empty");
            user_dgree.requestFocus();
            return false;
        }

        return true;
    }

    public boolean LoginPassword(EditText user_password, String password) {
        if (password.isEmpty()) {
            user_password.setError("Enter a password");
            user_password.requestFocus();
            return false;
        } else if (password.length() < 6) {
            user_password.setError("Password length needs to be at least 6");
            user_password.requestFocus();
            return false;
        }
        return true;
    }

    /**
     *
     * @param user_email
     * @param email
     * @param listener return the email + true or error+ false
     * @return true ONLY WHEN NOT EMPTY
     */
    public boolean LoginEmailOrUser(EditText user_email, String email, CustomOkListener listener) {
        if (email.isEmpty()) {
            user_email.setError("Email/user is empty");
            user_email.requestFocus();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener.onComplete(email,true);
            return true;
        }else  {
            FireBaseData.getEmailByUserName(email, new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    listener.onComplete((String) data,true);
                }

                @Override
                public void onCancelled(@NonNull String error) {
                    user_email.setError("Enter the valid email address");
                    user_email.requestFocus();
                    listener.onComplete("user not exist",false);

                }
            });
        }

        return true;
    }
}
