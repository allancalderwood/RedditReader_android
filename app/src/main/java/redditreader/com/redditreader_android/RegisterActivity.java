package redditreader.com.redditreader_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText textFieldPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        buttonBack = findViewById(R.id.buttonBack);

        //editTextUsername = findViewById(R.id.editTextUsername);
        //textFieldPassword = findViewById(R.id.textFieldPassword);
        //buttonLogin = findViewById(R.id.buttonRegister);
       // buttonRegister = findViewById(R.id.buttonRegister);

        //buttonRegister.setOnClickListener(new View.OnClickListener(){
            //@Override
           // public void onClick(View v){
                // TODO code to go to register page
          //  }
       // });

        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View v){
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                startActivity(intent);
            }
         });
    }

}
