package com.asahiflair.ahomkeyboard;

import static com.asahiflair.ahomkeyboard.utils.Constants.APP_LANGUAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.asahiflair.ahomkeyboard.R;
import com.asahiflair.ahomkeyboard.databinding.ActivityMainBinding;
import com.asahiflair.ahomkeyboard.databinding.DialogAppLanguagesBinding;
import com.asahiflair.ahomkeyboard.utils.PermissionUtils;
import com.asahiflair.ahomkeyboard.utils.PrefManager;
import com.asahiflair.ahomkeyboard.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initLanguage(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListeners();
    }


    private void initUi() {
//        binding.cvEnableKeyVibration.setChecked(AhomKeyboardService.keyVibrate);
        binding.cvEnableKeyPreview.setChecked(AhomKeyboardService.keyPreview);
        binding.cvEnableKeySound.setChecked(AhomKeyboardService.keySound);
        binding.cvEnableSuggestions.setChecked(AhomKeyboardService.suggestionsEnabled);
    }

    private void initListeners() {

        binding.cvChangeAppLanguage.setOnClickListener(v -> {
            changeAppLanguageDialog();
        });

//        binding.cvEnableKeyVibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            AhomKeyboardService.keyVibrate = !AhomKeyboardService.keyVibrate;
//        });

        binding.cvEnableKeySound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AhomKeyboardService.keySound = !AhomKeyboardService.keySound;
        });

        binding.cvEnableSuggestions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AhomKeyboardService.suggestionsEnabled = !AhomKeyboardService.suggestionsEnabled;
        });

        binding.cvEnableKeyPreview.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AhomKeyboardService.keyPreview = true;
        });

//        binding.cvChooseTheme.setOnClickListener(v -> {
//            startActivity(new Intent(this, ChooseThemeActivity.class));
//        });
//
//        binding.cvChooseLanguage.setOnClickListener(view -> {
//            startActivity(new Intent(this, ChooseLanguageActivity.class));
//        });
//
//        binding.cvEnableConverters.setOnClickListener(view -> {
//            startActivity(new Intent(this, EnableConvertersActivity.class));
//        });

        binding.cvAbout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutUsActivity.class));
        });

        binding.cvAboutUs.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutUsActivity.class));
        });
        binding.cvTestKeyboard.setOnClickListener(v -> {
            startActivity(new Intent(this, TestKeyboardActivity.class));
        });
    }

    private AlertDialog chooseLanguageDialog;

    private void changeAppLanguageDialog() {
        var dialogBinding = DialogAppLanguagesBinding.inflate(getLayoutInflater());
        var appLanguages = List.of("en", "shn", "my");
        // Preselect the app language
        var appLanguage = PrefManager.getStringValue(getApplicationContext(), APP_LANGUAGE);
        ((RadioButton) dialogBinding.rgAppLanguages.getChildAt(appLanguages.indexOf(appLanguage))).setChecked(true);

        if (chooseLanguageDialog == null) {
            var builder = new AlertDialog.Builder(this);
            chooseLanguageDialog = builder.setTitle("Choose App Language")
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Save", (dialog1, which) -> {
                        int checkedId = dialogBinding.rgAppLanguages.getCheckedRadioButtonId();
                        String locale;
                        if (checkedId == R.id.rb_shan) locale = "shn";
                        else if (checkedId == R.id.rb_burma) locale = "my";
                        else locale = "en";
                        Utils.setAppLocale(this, locale);
                        PrefManager.saveStringValue(this, APP_LANGUAGE, locale);
                        dialog1.cancel();

                        Intent refresh = new Intent(this, MainActivity.class);
                        startActivity(refresh);
                        finish();

                    }).create();
        }
        chooseLanguageDialog.show();
    }


}