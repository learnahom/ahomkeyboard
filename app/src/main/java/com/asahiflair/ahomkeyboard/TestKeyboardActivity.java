package com.asahiflair.ahomkeyboard;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import com.asahiflair.ahomkeyboard.R;
import com.asahiflair.ahomkeyboard.databinding.ActivityTestKeyboardBinding;
import com.asahiflair.ahomkeyboard.utils.Utils;

public class TestKeyboardActivity extends AppCompatActivity {

    private ActivityTestKeyboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Utils.initLanguage(this);
        binding = ActivityTestKeyboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


}