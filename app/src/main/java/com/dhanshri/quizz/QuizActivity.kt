package com.dhanshri.quizz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.dhanshri.quizz.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.timer

class QuizActivity : AppCompatActivity() {
    private lateinit var quizBinding : ActivityQuizBinding

    // RealtimeDatabase from firebase
    private val database = FirebaseDatabase.getInstance()  //Firebase database object
    private val databaseReference = database.reference.child("Questions")


    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference



// Counter
    lateinit var timer: CountDownTimer
    private val totalTime = 25000L    //L define log as this value must be constant
    var timerContinue = false
    var leftTime = totalTime

    var question = ""
    var optionA = ""
    var optionB = ""
    var optionC = ""
    var optionD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    var userAnswer = ""
    var userCorrect = 0
    var userWrong =  0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizBinding = DataBindingUtil.setContentView(this,R.layout.activity_quiz)

        gameLogic()
        quizBinding.btnNext.setOnClickListener {
            resetTimer()
            gameLogic()

        }
        quizBinding.btnFinish.setOnClickListener {
            sendScore()
        }
        quizBinding.optionA.setOnClickListener {

            pauseTimer()
            userAnswer = "a"
            if (correctAnswer == userAnswer){
                quizBinding.optionA.setBackgroundColor(Color.GREEN)
                userCorrect ++
                quizBinding.correctCount.text = userCorrect.toString()

            }else{
                quizBinding.optionA.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.incorrectNumber.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }
        quizBinding.optionB.setOnClickListener {
            pauseTimer()

            userAnswer = "b"
            if (correctAnswer == userAnswer){
                quizBinding.optionB.setBackgroundColor(Color.GREEN)
                userCorrect ++
                quizBinding.correctCount.text = userCorrect.toString()

            }else{
                quizBinding.optionB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.incorrectNumber.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }
        quizBinding.optionC.setOnClickListener {
            pauseTimer()
            userAnswer = "c"
            if (correctAnswer == userAnswer){
                quizBinding.optionC.setBackgroundColor(Color.GREEN)
                userCorrect ++
                quizBinding.correctCount.text = userCorrect.toString()

            }else{
                quizBinding.optionC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.incorrectNumber.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }
        quizBinding.optionD.setOnClickListener{

            pauseTimer()
            userAnswer = "d"
            if (correctAnswer == userAnswer){
                quizBinding.optionD.setBackgroundColor(Color.GREEN)
                userCorrect ++
                quizBinding.correctCount.text = userCorrect.toString()

            }else{
                quizBinding.optionD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.incorrectNumber.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }

    }

    private fun gameLogic(){

        restoreOptions()
        databaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()      //this function give th child count of functionparent

                if (questionNumber <= questionCount){
                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    optionA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    optionB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    optionC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    optionD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.txtQuestion.text = question
                    quizBinding.optionA.text = optionA
                    quizBinding.optionB.text = optionB
                    quizBinding.optionC.text = optionC
                    quizBinding.optionD.text = optionD

                    quizBinding.progressBarQuiz.visibility  = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                    quizBinding.lineaarLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButton.visibility = View.VISIBLE

                    startTimer()

                } else {

                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulation!!, \nYou have answered all the questions. Do you want to see the result ")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow, position ->
                        val intent = Intent(this@QuizActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()
//                    Toast.makeText(applicationContext, "You answered all the question", Toast.LENGTH_LONG).show()
                }

                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()

            }

        })
    }

    fun findAnswer(){
        when(correctAnswer){
            "a" -> quizBinding.optionA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.optionA.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.optionA.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.optionA.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickableOfOptions(){
        quizBinding.optionA.isClickable = false
        quizBinding.optionB.isClickable = false
        quizBinding.optionC.isClickable = false
        quizBinding.optionD.isClickable = false
    }

    fun restoreOptions(){

        quizBinding.optionA.setBackgroundColor(Color.WHITE)
        quizBinding.optionB.setBackgroundColor(Color.WHITE)
        quizBinding.optionC.setBackgroundColor(Color.WHITE)
        quizBinding.optionD.setBackgroundColor(Color.WHITE)

        quizBinding.optionA.isClickable = true
        quizBinding.optionB.isClickable = true
        quizBinding.optionC.isClickable = true
        quizBinding.optionD.isClickable = true
    }

    private fun startTimer(){
        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(millisUntilFinish: Long) {
                leftTime = millisUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.txtQuestion.text = "Time up !!"
                Toast.makeText(applicationContext, "Sorry!, Time is up continue with next question", Toast.LENGTH_LONG).show()
                timerContinue = false
            }

        }.start()

        timerContinue = true
    }

    //
    fun updateCountDownText(){
        val remainingTime: Int = (leftTime/1000).toInt()
        quizBinding.timer.text = remainingTime.toString()
    }
    fun pauseTimer(){
        timer.cancel()
        timerContinue = false
    }

    fun resetTimer(){
        pauseTimer()
        leftTime =  totalTime
        updateCountDownText()
    }

    fun sendScore(){
        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext, "Scores sent to database successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}