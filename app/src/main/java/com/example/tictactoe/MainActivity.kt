package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max
import kotlin.math.min


class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var pos = -1

    enum class Player constructor(val value: Int) {
        HUMAN(1),
        MACHINE(2)
    }

    private var isUser = Player.HUMAN

    private val board = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickListener()
    }


    private fun clickListener() {
        tv_0.setOnClickListener(this)
        tv_1.setOnClickListener(this)
        tv_2.setOnClickListener(this)
        tv_3.setOnClickListener(this)
        tv_4.setOnClickListener(this)
        tv_5.setOnClickListener(this)
        tv_6.setOnClickListener(this)
        tv_7.setOnClickListener(this)
        tv_8.setOnClickListener(this)
    }

    private fun clickOnTv(view: TextView, pos: Int) {

        view.text = "X"
        view.setOnClickListener(null)
        board[pos] = (Player.HUMAN.value)
        isUser = Player.MACHINE
        machineTurn()
    }


    private fun machineTurn() {
        /*val list = arrayListOf<Int>()

        for (i in 0 until board.size) {

            if (board[i] == 0) {
                list.add(i)
            }
        }

        if (list.isNotEmpty()) {
            list.shuffle()
            makeComputerMove(list[0])
            getWinning(board)

        }*/


        winProbability(0, Player.MACHINE)
        makeComputerMove(pos)

        getWinning(board)

    }

    private fun makeComputerMove(pos: Int) {
        when (pos) {
            0 -> {
                tv_0.text = "0"
                board[0] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            1 -> {
                tv_1.text = "0"
                board[1] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            2 -> {
                tv_2.text = "0"
                board[2] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            3 -> {
                tv_3.text = "0"
                board[3] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            4 -> {
                tv_4.text = "0"
                board[4] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            5 -> {
                tv_5.text = "0"
                board[5] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            6 -> {
                tv_6.text = "0"
                board[6] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            7 -> {
                tv_7.text = "0"
                board[7] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
            8 -> {
                tv_8.text = "0"
                board[8] = (Player.MACHINE.value)
                isUser = Player.HUMAN
            }
        }
    }

    private fun getBlankSpace(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (i in board.indices) {
            if (board[i] == 0) {
                list.add(i)
            }
        }

        return list
    }


    private fun winProbability(depth: Int, player: Player): Int {

        if (ifComputerWins()) {

            Log.e("Computer can Wins", "->$ +1")
            return +1
        }
        if (ifHumanWins()) {
            Log.e("Human can Wins", "-> $ -1")
            return -1
        }
        if (getBlankSpace().isEmpty()) {
            Log.e("It's an ", "Dead End. GO back.")
            return 0
        }


        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE


        for (i in getBlankSpace().indices) {
            val value = getBlankSpace()[i]
            if (player == Player.MACHINE) {
                board[value] = Player.MACHINE.value
                val currentScore = winProbability(depth + 1, Player.HUMAN)


                max = max(currentScore, max)
                Log.e("Current Score", "$currentScore And Max score $max at $value for ${Player.MACHINE}")
                if (currentScore >= 0) {
                    if (depth == 0) {
                        this.pos = value

                    }
                }

                if (currentScore == 1) {
                    board[value] = 0
                    break
                }

                if (i == getBlankSpace().size - 1 && max < 0) {
                    if (depth == 0) {
                        pos = value
                    }
                }

            } else if (player == Player.HUMAN) {
                board[value] = Player.HUMAN.value
                val currentScore = winProbability(depth + 1, Player.MACHINE)
                min = min(currentScore, min)

                Log.e("Current Score", "$currentScore And Max score $min at $value for ${Player.HUMAN}")


                if (min == -1) {
                    board[value] = 0
                    break
                }
            }
            board[value] = 0

        }
        return if (player == Player.MACHINE) max else min
    }

    private fun ifComputerWins(): Boolean {

        if ((board[0] == Player.MACHINE.value)) {

            if ((board[1] == Player.MACHINE.value) && (board[2] == Player.MACHINE.value)) {
                return true
            }

            if ((board[3] == Player.MACHINE.value) && (board[6] == Player.MACHINE.value)) {

                return true
            }

            if ((board[4] == Player.MACHINE.value) && (board[8] == Player.MACHINE.value)) {

                return true
            }
        } else

            if ((board[1] == Player.MACHINE.value)) {

                if ((board[4] == Player.MACHINE.value) && (board[7] == Player.MACHINE.value)) {

                    return true
                }
            } else

                if ((board[2] == Player.MACHINE.value)) {

                    if ((board[5] == Player.MACHINE.value) && (board[8] == Player.MACHINE.value)) {

                        return true
                    }

                    if ((board[4] == Player.MACHINE.value) && (board[6] == Player.MACHINE.value)) {

                        return true
                    }
                } else

                    if (board[3] == Player.MACHINE.value && board[4] == Player.MACHINE.value && board[5] == Player.MACHINE.value) {

                        return true

                    }

        if (board[6] == Player.MACHINE.value && board[7] == Player.MACHINE.value && board[8] == Player.MACHINE.value) {

            return true
        }

        return false
    }

    private fun ifHumanWins(): Boolean {

        if ((board[0] == Player.HUMAN.value)) {

            if ((board[1] == Player.HUMAN.value) && (board[2] == Player.HUMAN.value)) {
                return true
            }

            if ((board[3] == Player.HUMAN.value) && (board[6] == Player.HUMAN.value)) {

                return true
            }

            if ((board[4] == Player.HUMAN.value) && (board[8] == Player.HUMAN.value)) {

                return true
            }
        } else

            if ((board[1] == Player.HUMAN.value)) {

                if ((board[4] == Player.HUMAN.value) && (board[7] == Player.HUMAN.value)) {

                    return true
                }
            } else

                if ((board[2] == Player.HUMAN.value)) {

                    if ((board[5] == Player.HUMAN.value) && (board[8] == Player.HUMAN.value)) {

                        return true
                    }

                    if ((board[4] == Player.HUMAN.value) && (board[6] == Player.HUMAN.value)) {

                        return true
                    }
                } else

                    if (board[3] == Player.HUMAN.value && board[4] == Player.HUMAN.value && board[5] == Player.HUMAN.value) {

                        return true

                    }

        if (board[6] == Player.HUMAN.value && board[7] == Player.HUMAN.value && board[8] == Player.HUMAN.value) {

            return true
        }

        return false
    }

    private fun getWinning(board: ArrayList<Int>): Int {

        if (board.size >= 5) {
            var ifSomeoneWins = false

            for (i in 1..2) {

                if ((board[0] == i)) {

                    if ((board[1] == i) && (board[2] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }

                    if ((board[3] == i) && (board[6] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }

                    if ((board[4] == i) && (board[8] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }
                }

                if ((board[1] == i)) {

                    if ((board[4] == i) && (board[7] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }
                }

                if ((board[2] == i)) {

                    if ((board[5] == i) && (board[8] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }

                    if ((board[4] == i) && (board[6] == i)) {

                        Toast.makeText(
                            this,
                            "Player ${if (i == 1) "Human" else "Machine"} wins..",
                            Toast.LENGTH_LONG
                        ).show()
                        ifSomeoneWins = true
                        break
                    }
                }

                if (board[3] == i && board[4] == i && board[5] == i) {

                    Toast.makeText(
                        this,
                        "Player ${if (i == 1) "Human" else "Machine"} wins..",
                        Toast.LENGTH_LONG
                    ).show()
                    ifSomeoneWins = true
                    break

                }

                if (board[6] == i && board[7] == i && board[8] == i) {

                    Toast.makeText(
                        this,
                        "Player ${if (i == 1) "Human" else "Machine"} wins..",
                        Toast.LENGTH_LONG
                    ).show()
                    ifSomeoneWins = true
                    break

                }
            }

            if (ifSomeoneWins) {
                Handler().postDelayed({
                    reset()
                }, 2000)
                return 1
            } else if (getBlankSpace().isEmpty()) {
                Toast.makeText(this, "Both are fools. NO ONE WINS!!!!", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    reset()
                }, 2000)
                return 2
            }

        }
        return 3
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.tv_0 -> {
                clickOnTv(v as TextView, 0)
            }

            R.id.tv_1 -> {
                clickOnTv(v as TextView, 1)
            }

            R.id.tv_2 -> {
                clickOnTv(v as TextView, 2)
            }

            R.id.tv_3 -> {
                clickOnTv(v as TextView, 3)
            }
            R.id.tv_4 -> {
                clickOnTv(v as TextView, 4)
            }
            R.id.tv_5 -> {
                clickOnTv(v as TextView, 5)
            }
            R.id.tv_6 -> {
                clickOnTv(v as TextView, 6)
            }
            R.id.tv_7 -> {
                clickOnTv(v as TextView, 7)
            }
            R.id.tv_8 -> {
                clickOnTv(v as TextView, 8)
            }
        }
    }

    private fun reset() {
        clickListener()
        isUser = Player.HUMAN
        tv_0.text = ""
        tv_1.text = ""
        tv_2.text = ""
        tv_3.text = ""
        tv_4.text = ""
        tv_5.text = ""
        tv_6.text = ""
        tv_7.text = ""
        tv_8.text = ""
        for (i in 0 until board.size) {
            board[i] = 0
        }
    }
}