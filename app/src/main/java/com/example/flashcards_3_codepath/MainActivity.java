package com.example.flashcards_3_codepath;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView questionTextView;
    TextView answerTextView;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.question);
        answerTextView = findViewById(R.id.right_answer);

        answerTextView.setVisibility(View.INVISIBLE);

        questionTextView.setOnClickListener(view -> {
            questionTextView.setVisibility(View.INVISIBLE);
            answerTextView.setVisibility(View.VISIBLE);
        });
        answerTextView.setOnClickListener(view -> {
            questionTextView.setVisibility(View.VISIBLE);
            answerTextView.setVisibility(View.INVISIBLE);
        });


        ImageView addQuestionImageView = findViewById(R.id.plustn);
        addQuestionImageView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            startActivityForResult(intent, 100);

            Toast.makeText(MainActivity.this, "Enter your text", Toast.LENGTH_SHORT).show();
            Log.i("Andreza", "Entered onClick");
        });

        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0){
        Flashcard firstCard = allFlashcards.get(0);
        questionTextView.setText(firstCard.getQuestion());
        answerTextView.setText(firstCard.getAnswer());
        }

        findViewById(R.id.next).setOnClickListener(v -> {

            if (allFlashcards ==  null || allFlashcards.size() == 0){
                return;
            }

            if (cardIndex >= allFlashcards.size()) {
                Snackbar.make(v,
                        "That's the end!", BaseTransientBottomBar.LENGTH_SHORT).show();
                cardIndex = 0; //reset
                return;
            }

            Flashcard currentCard = allFlashcards.get(cardIndex);
            questionTextView.setText(currentCard.getQuestion());
            answerTextView.setText(currentCard.getQuestion());
        });

        findViewById(R.id.trash).setOnClickListener(v -> {
            flashcardDatabase.deleteCard(((TextView) findViewById(R.id.question)).getText().toString());
            allFlashcards = flashcardDatabase.getAllCards();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult((requestCode), resultCode, data);
        if (requestCode == 100) // this 100 needs to match the 100 we used when we called startActivityForResult!
        {
            if (data != null) {
                String questionString = data.getExtras().getString("Question_key");
                String answerString = data.getExtras().getString("Answer_key");
                questionTextView.setText(questionString);
                answerTextView.setText(answerString);

                Flashcard flashcard = new Flashcard(questionString, answerString);
                flashcardDatabase.insertCard(flashcard);

                allFlashcards = flashcardDatabase.getAllCards();

            }
        }
    }
}