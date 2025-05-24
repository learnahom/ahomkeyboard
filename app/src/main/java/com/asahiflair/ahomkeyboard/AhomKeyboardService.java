package com.asahiflair.ahomkeyboard;

import com.asahiflair.ahomkeyboard.utils.Transliterator;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

import java.util.Random;

public class AhomKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard currentKeyboard;
    private Keyboard numberKeyboard;
    private Keyboard engSymbolKeyboard;
    private Keyboard eng1Keyboard;
    private Keyboard eng2Keyboard;
    private Keyboard engNumbersKeyboard;
    private Keyboard ahom1Keyboard;
    private Keyboard ahom2Keyboard;

    private SoundPool sp;
    private int sound_standard;

    private boolean isCaps = false;
    public static boolean keyPreview = false;
    public static boolean keySound = false;
    public static boolean suggestionsEnabled = true;
    public static boolean keyVibrate = false;
    private String lang = "English";
    private boolean isSpecial = false;
    private TextView suggestionView;
    private StringBuilder currentWord = new StringBuilder();


    @Override
    public View onCreateInputView() {
        View view = getLayoutInflater().inflate(R.layout.custom_keyboard_layout, null);

        keyboard = new Keyboard(this, R.xml.english1); // Create a qwerty.xml layout
        kv = view.findViewById(R.id.keyboardView);
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(keyPreview);
        kv.setOnKeyboardActionListener(this);

        suggestionView = view.findViewById(R.id.suggestionView);
        //reset suggestions in beginning
        suggestionView.setVisibility(View.GONE);
        suggestionView.setText("");
        suggestionView.setOnClickListener(v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null && currentWord.length() > 0) {
                // Replace the typed word with the transliteration
                ic.deleteSurroundingText(currentWord.length(), 0);
                ic.commitText(suggestionView.getText()+" ", 1);

                currentWord.setLength(0);
                suggestionView.setVisibility(View.GONE);
                suggestionView.setText("");
            }
        });

        return view;
    }

    public void onWindowShown() {
        kv.setPreviewEnabled(keyPreview);
        super.onWindowShown();
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    private void changeKeyboard(Keyboard keyboard) {
        kv.setKeyboard(keyboard);
        currentKeyboard = keyboard;
    }

    private void shiftKeyboard() {
        if (currentKeyboard == getEng1Keyboard()) {
            changeKeyboard(getEng2Keyboard());
        } else if (currentKeyboard == getAhom1Keyboard()) {
            changeKeyboard(getAhom2Keyboard());
        } else if (currentKeyboard == getEng2Keyboard()) {
            changeKeyboard(getEng1Keyboard());
        }  else if (currentKeyboard == getAhom2Keyboard()) {
            changeKeyboard(getAhom1Keyboard());
        }
        isCaps = !isCaps;
        kv.invalidateAllKeys();
    }

    private Keyboard getEng2Keyboard() {
        if (eng2Keyboard == null) eng2Keyboard = new Keyboard(this, R.xml.english2);
        return eng2Keyboard;
    }

    private Keyboard getEng1Keyboard() {
        if (eng1Keyboard == null) eng1Keyboard = new Keyboard(this, R.xml.english1);
        return eng1Keyboard;
    }

    public Keyboard getEngSymbolKeyboard() {
        if (engSymbolKeyboard == null) engSymbolKeyboard = new Keyboard(this, R.xml.eng_symbol);
        return engSymbolKeyboard;
    }

    public Keyboard getAhom1Keyboard() {
        if (ahom1Keyboard == null)
            ahom1Keyboard = new Keyboard(this, R.xml.ahom1);
        return ahom1Keyboard;
    }

    public Keyboard getAhom2Keyboard() {
        if (ahom2Keyboard == null)
            ahom2Keyboard = new Keyboard(this, R.xml.ahom2);
        return ahom2Keyboard;
    }

//    public MaoKeyboard getNumberKeyboard() {
//        if (numberKeyboard == null) numberKeyboard = new Keyboard(this, R.xml.number);
//        return numberKeyboard;
//    }

    public Keyboard getEngNumbersKeyboard() {
        if (engNumbersKeyboard == null)
            engNumbersKeyboard = new Keyboard(this, R.xml.eng_numbers);
        return engNumbersKeyboard;
    }

    private Random random = new Random();
    private final String[] randomSuggestions = new String[]{
            "How can I help you?",
            "Have a nice day!",
            "Hope you’re doing well!",
            "Let’s catch up soon!",
            "What’s up?",
            "Take care!",
            "Sounds good to me!",
            "I'll get back to you."
    };
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence charBeforeCursor = ic.getTextBeforeCursor(1, 0);
                int charCodeBeforeCursor = 0;
                if (charBeforeCursor != null && charBeforeCursor.length() > 0) {
                    charCodeBeforeCursor = charBeforeCursor.charAt(0);
                }

                if ((charBeforeCursor == null) || (charBeforeCursor.length() <= 0)) {
                    return;// fixed on issue of version 1.2, cause=(getText is null)
                }
                // For Ahom Delete
                if (Character.isLowSurrogate(charBeforeCursor.charAt(0))
                        || Character.isHighSurrogate(charBeforeCursor.charAt(0))) {
                    ic.deleteSurroundingText(2, 0);
                } else {
                    ic.deleteSurroundingText(1, 0);
                }
                if (currentWord.length() > 0) {
                    currentWord.deleteCharAt(currentWord.length() - 1);
                }
                showSuggestion();

                break;
            case Keyboard.KEYCODE_SHIFT:
                shiftKeyboard();
                break;
            case -101: // switch language
                if (lang.equals("English")){
                    lang = "Ahom";
                    changeKeyboard(getAhom1Keyboard());
                }else{
                    lang = "English";
                    changeKeyboard(getEng1Keyboard());
                }
                isCaps = false;
                break;
            case -123:
                changeKeyboard(getEngSymbolKeyboard());
                break;
            case -321:
                changeKeyboard(getEng1Keyboard());
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                currentWord.setLength(0); // Clear current word
                suggestionView.setVisibility(View.GONE);
                break;
            default:
                // Tai Ahom
                if (((primaryCode >= 71424) && (primaryCode <= 71487)) || primaryCode==3634) {
                    currentWord.append(Character.toChars(primaryCode));
                    ic.commitText(new String(Character.toChars(primaryCode)), 1);
                    showSuggestion();
                    return;
                }
                char code = (char) primaryCode;
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                }
                if (code >= 33 && code <= 126 && !Character.isWhitespace(code)) {
//                    if (Character.isLetter(code) || currentWord.length()!=0)
                    currentWord.append(code);
                    ic.commitText(String.valueOf(code), 1);

                    showSuggestion();


                } else if (Character.isWhitespace(code) || code == '\n' || code == '\r') {
                    ic.commitText(String.valueOf(code), 1);
                    currentWord.setLength(0);
                    suggestionView.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void showSuggestion(){
        if (!suggestionsEnabled){
            suggestionView.setText("");
            suggestionView.setVisibility(View.GONE);
            return;
        }
        String suggestion = "";
        if (lang.equals("English"))
            suggestion = Transliterator.convertToAhom(currentWord.toString());
        else
            suggestion = Transliterator.convertToRoman(currentWord.toString());
        suggestionView.setText(suggestion);
        if (!suggestion.isEmpty())
            suggestionView.setVisibility(View.VISIBLE);
        else
            suggestionView.setVisibility(View.GONE);
    }

    public SoundPool getSoundPool() {
        if (sp == null) {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            sound_standard = sp.load(getApplicationContext(), R.raw.sound1, 1);
        }
        return sp;
    }

    private void playClick() {
        if (keySound) {
            getSoundPool().play(sound_standard, 1, 1, 0, 0, 1);
        }
    }

    @Override
    public void onText(CharSequence text) {
        getCurrentInputConnection().commitText(text, 1);
        currentWord.append(text);
        showSuggestion();
    }
    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}

