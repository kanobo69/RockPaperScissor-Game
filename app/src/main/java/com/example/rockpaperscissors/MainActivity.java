package com.example.rockpaperscissors;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvGameStatus, tvScore;
    private Button btnRock, btnPaper, btnScissors, btnReset;
    private LinearLayout layoutButtons;
    private ImageView imgPlayer, imgComputer;
    private MediaPlayer mpCountdown, mpWin, mpLose, mpDraw;
    private int wins = 0, losses = 0, draws = 0;
    private final String[] choices = {"Rock", "Paper", "Scissors"};
    private final int[] images = {R.drawable.rock, R.drawable.paper, R.drawable.scissors};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGameStatus = findViewById(R.id.tv_game_status);
        tvScore = findViewById(R.id.tv_score);
        btnRock = findViewById(R.id.btn_rock);
        btnPaper = findViewById(R.id.btn_paper);
        btnScissors = findViewById(R.id.btn_scissors);
        btnReset = findViewById(R.id.btn_reset);
        layoutButtons = findViewById(R.id.layout_buttons);
        imgPlayer = findViewById(R.id.img_player);
        imgComputer = findViewById(R.id.img_computer);

        mpCountdown = MediaPlayer.create(this, R.raw.rock_paper_scissors);
        mpWin = MediaPlayer.create(this, R.raw.win);
        mpLose = MediaPlayer.create(this, R.raw.lose);
        mpDraw = MediaPlayer.create(this, R.raw.draw);

        tvGameStatus.setText("Let's Play!");
        updateScore();

        btnRock.setOnClickListener(v -> playGame("Rock", R.drawable.rock));
        btnPaper.setOnClickListener(v -> playGame("Paper", R.drawable.paper));
        btnScissors.setOnClickListener(v -> playGame("Scissors", R.drawable.scissors));
        btnReset.setOnClickListener(v -> resetGame());
    }

    private void playGame(String playerChoice, int playerImage) {
        layoutButtons.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        imgPlayer.setVisibility(View.VISIBLE);
        imgComputer.setVisibility(View.VISIBLE);

        tvGameStatus.setText("Rock...");
        imgPlayer.setImageResource(R.drawable.rock);
        imgComputer.setImageResource(R.drawable.rock);
        mpCountdown.start();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            tvGameStatus.setText("Paper...");
            imgPlayer.setImageResource(R.drawable.paper);
            imgComputer.setImageResource(R.drawable.paper);
        }, 1000);

        handler.postDelayed(() -> {
            tvGameStatus.setText("Scissors...");
            imgPlayer.setImageResource(R.drawable.scissors);
            imgComputer.setImageResource(R.drawable.scissors);
        }, 1700);

        handler.postDelayed(() -> {
            tvGameStatus.setText("1...");
            imgPlayer.setImageResource(R.drawable.question);
            imgComputer.setImageResource(R.drawable.question);
        }, 2500);

        handler.postDelayed(() -> tvGameStatus.setText("2..."), 3000);
        handler.postDelayed(() -> tvGameStatus.setText("3..."), 3500);

        handler.postDelayed(() -> {
            int randomIndex = new Random().nextInt(3);
            String computerChoice = choices[randomIndex];
            int computerImage = images[randomIndex];
            imgPlayer.setImageResource(playerImage);
            imgComputer.setImageResource(computerImage);
            checkWinner(playerChoice, computerChoice);
        }, 4000);
    }

    private void checkWinner(String playerChoice, String computerChoice) {
        String result;
        if (playerChoice.equals(computerChoice)) {
            result = "It's a Draw!";
            draws++;
            mpDraw.start();
        } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (playerChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (playerChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            result = "You Win!";
            wins++;
            mpWin.start();
        } else {
            result = "You Lose!";
            losses++;
            mpLose.start();
        }

        tvGameStatus.setText(result);
        updateScore();
        new Handler().postDelayed(() -> {
            layoutButtons.setVisibility(View.VISIBLE);
            btnReset.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void updateScore() {
        tvScore.setText("Wins: " + wins + " | Losses: " + losses + " | Draws: " + draws);
    }

    private void resetGame() {
        wins = 0;
        losses = 0;
        draws = 0;
        tvGameStatus.setText("Let's Play!");
        updateScore();
        layoutButtons.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mpCountdown != null) mpCountdown.release();
        if (mpWin != null) mpWin.release();
        if (mpLose != null) mpLose.release();
        if (mpDraw != null) mpDraw.release();
    }
}