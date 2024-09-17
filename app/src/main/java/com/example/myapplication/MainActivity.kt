    package com.example.myapplication

    import android.content.Intent
    import android.os.Bundle
    import android.view.inputmethod.InputBinding
    import android.widget.Button
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.tooling.preview.Preview
    import com.example.myapplication.ui.theme.MyApplicationTheme


    class MainActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.main_activity)

            val loginButton = findViewById<Button>(R.id.btnLogin)
            val registerButton = findViewById<Button>(R.id.btnRegister)

            loginButton.setOnClickListener(){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            registerButton.setOnClickListener(){
                val intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }