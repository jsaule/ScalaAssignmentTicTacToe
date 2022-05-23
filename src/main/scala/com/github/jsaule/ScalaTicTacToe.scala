package com.github.jsaule

import scala.io.StdIn.readLine
import scala.util.Random

class ScalaTicTacToe {
  // intro + initial input from players (real player one vs. real player two OR real player one vs. computer)
  println("Welcome to the Tic Tac Toe Game!")
  val inputNameOne: String = readLine("What is your name, Player 1? ")
  val inputXO: String = readLine("Please choose X or O. ")
  val playerOne: String = if (inputXO.toUpperCase == "X") inputXO.toUpperCase else "O"
  val playerTwo: String = if (playerOne == "X") "O" else "X"
  println(s"Thank you, $inputNameOne. Your symbol is '$playerOne'.")
  var inputNameTwo: String = readLine("What is your name, Player 2? Press ENTER if Player 2 is Computer. ")
  if (inputNameTwo == "") inputNameTwo = "Computer"
  println(s"Thank you, $inputNameTwo. Your symbol is '$playerTwo'.")

  // starting board all possible positions
  val emptyBoard: Array[Char] = Array('1', '2', '3', '4', '5', '6', '7', '8', '9')

  // all winning board position sets
  val winningSets: Set[Set[Int]] = Set(
    Set(0, 1, 2),
    Set(3, 4, 5),
    Set(6, 7, 8),
    Set(0, 3, 6),
    Set(1, 4, 7),
    Set(2, 5, 8),
    Set(0, 4, 8),
    Set(2, 4, 6)
  )

  // initiates game
  printBoard(emptyBoard)
  nextTurn(emptyBoard)

  // defines who's turn it is (human one, human two, computer), takes move input and initiates board print
  def nextTurn(board: Array[Char]): Unit = {
    val remainingTurns = board.count(_.toString.matches("[1-9]"))
    val next = if(remainingTurns%2 == 0) playerTwo.toCharArray.head else playerOne.toCharArray.head
    val nextMove = if (inputNameTwo == "Computer" && next == playerTwo.toCharArray.head) computerInput(board) else moveInput(board)
    board(nextMove) = nextPlayer(board)
    printBoard(board)
    if (!checkWin(board)) {
      nextTurn(board)
    }
  }

  // returns computer's move input (if it's game human vs. computer)
  def computerInput(board: Array[Char]): Int = {
    val input = Random.nextInt(9).toString
    if (input.matches("[1-9]")) {
      val move = input.toInt -1
      if (board(move).isDigit) {
        println("Player '"+nextPlayer(board)+"' made a move.")
        move
      } else {
        computerInput(board)
      }
    } else {
      computerInput(board)
    }
  }

  // returns human player's move input (human one or human two)
  def moveInput(board: Array[Char]): Int = {
    val input = readLine("Enter your board position number to move (between 1 and 9), Player '"+nextPlayer(board)+"'! ")
    if (input.matches("[1-9]")) {
      val move = input.toInt -1
      if (board(move).isDigit) {
        move
      } else {
        println("That position is already taken. Try again. ")
        moveInput(board)
      }
    } else {
      println("Your choice must be a number between 1 and 9. ")
      moveInput(board)
    }
  }

  // returns next player
  def nextPlayer(board: Array[Char]): Char = {
    val remainingTurns = board.count(_.toString.matches("[1-9]"))
    val next = if(remainingTurns%2 == 0) playerTwo.toCharArray.head else playerOne.toCharArray.head
    next
  }

  // prints board
  def printBoard(board: Array[Char]): Unit = {
    println(
      0 to 2 map { r =>
        0 to 2 map { c =>
          board(c + r*3)
        } mkString " | "
      } mkString ("\n", "\n—   —   —\n", "\n")
    )
  }

  /* returns true or false if it's win or draw and
  saves results (win or draw) in data base (creating it and tables
  if they don't exist) if it's win or draw */
  def checkWin(board: Array[Char]): Boolean = {
    winningSets.foreach(pattern=>{
      if(pattern.forall(board(_) == board(pattern.head))) {
        val winnerName = if (board(pattern.head).toString == playerOne) s"$inputNameOne" else s"$inputNameTwo"
        val loserName = if (board(pattern.head).toString == playerOne) s"$inputNameTwo" else s"$inputNameOne"
        println(s"Winner is ${board(pattern.head)}! Congratulations, $winnerName!")
        println(s"Better luck next time, $loserName!")
        println("\nGame result saved.")
        val db = new WinnersLosersDB("src/resources/db/winners_losers.db")
        db.migrate()
        db.insertResults(winnerName, loserName)
        db.connect.close()
        return true
      }
        else if(board.count(_.toString.matches("[1-9]")) <= 0) {
        val playerOneName = inputNameOne
        val playerTwoName = inputNameTwo
        val db = new WinnersLosersDB("src/resources/db/winners_losers.db")
        db.migrateDraw()
        db.insertDrawResults(playerOneName, playerTwoName)
        db.connect.close()
        println(s"It's a draw! Well played both, $playerOneName and $playerTwoName!")
        println("\nGame result saved.")
        return true
      }
    })
    false
  }

}

object ScalaTicTacToe extends App {

  //creates new game (uses ScalaTicTacToe class)
  val game = new ScalaTicTacToe

}
