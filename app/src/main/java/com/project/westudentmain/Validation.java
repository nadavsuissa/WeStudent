package com.project.westudentmain;

import android.util.Patterns;
import android.widget.EditText;

/**
 * this class making validation to all input from user
 */

public class Validation {

    public boolean RegisterLogin(EditText user_name, EditText pass_word, String email, String password){

        if (email.isEmpty()) {
            user_name.setError("Email is empty");
            user_name.requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user_name.setError("Enter the valid email address");
            user_name.requestFocus();
            return false;
        }
        else if (password.isEmpty()) {
            pass_word.setError("Enter a password");
            pass_word.requestFocus();
            return false;
        }
        else if (password.length() < 6) {
            pass_word.setError("Password length needs to be at least 6");
            pass_word.requestFocus();
            return false;
        }
        return true;
    }

}
