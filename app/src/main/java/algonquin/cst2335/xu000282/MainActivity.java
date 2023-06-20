package algonquin.cst2335.xu000282;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this class is a simple password checker app
 * @author Longpeng Xu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * this holds the text at the centre of the screen.
     */
    private TextView tv = null;
    /**
     * this holds the password text
     */
    private EditText et = null;
    /**
     * this holds the button massage
     */
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.passwordText);
        btn = findViewById(R.id.loginButton);

        btn.setOnClickListener( clk ->{
            String password = et.getText().toString();
            checkPasswordComplexity(password);
        });
    }

    /**
     * This function is to check if this string has an Upper Case letter, a lower case letter, a number,
     * and a special symbol (#$%^&*!@?).
     * @param pw The String object that we are checking.
     * @return returns true if the password meets the requirement of complexity.
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (char ch : pw.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                foundLowerCase = true;
            } else if (Character.isDigit(ch)) {
                foundNumber = true;
            } else if (isSpecialCharacter(ch)) {
                foundSpecial = true;
            }

            // If all conditions are met, break the loop early
            if (foundUpperCase && foundLowerCase && foundNumber && foundSpecial) {
                break;
            }
        }

        if(!foundUpperCase)
        {

            Toast.makeText(this, "You are missing an upper case letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;

        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(this, "You are missing a lower case letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }

        else if( ! foundNumber) {
            Toast.makeText(this, "You are missing a number", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }

        else if(! foundSpecial) {
            Toast.makeText(this, "You are missing a special character", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass!");
            return false;
        }

        else
            tv.setText("Your password meets the requirements");
        return true; //only get here if they're all true

    }

    boolean isSpecialCharacter (char c){
        switch(c)
        {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}