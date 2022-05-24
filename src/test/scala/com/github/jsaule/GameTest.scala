package com.github.jsaule

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter

class GameTest extends AnyFunSuite with BeforeAndAfter {
    var ticTac4: ScalaTicTacToe= _
    var WinnersLosersDB: WinnersLosersDB = new WinnersLosersDB("src/resources/winners_losers.db")
    before {
      //we put whatever initialization we need for tests in this particular suite
      ticTac4 = new ScalaTicTacToe()
    }
    test("ticTac4") {
      val playerOne='X'
      val playerTwo='O'
      assert(ticTac4.checkWin(Array('0', '1', '2'))===playerTwo)
    }
    test("ticTac4nextPlayer") {

      val playerOne='X'
      val playerTwo='O'
      val board=Array('1', '2', '3', '4', '5', '6', '7', '8', '9')
      assert(ticTac4.nextPlayer(Array('1', '2', '3', '4', '5', '6', '7', '8', '9'))===playerTwo)
      // assert(ticTac4.nextPlayer(board)===playerTwo)
      // assert(ticTac4.nextPlayer(Array('0', '1', '2'))==='O')
    }
    test("WinnersLosersDB.getUserId") {
      val winnerName = "Nina"
      //val loserName ="Rob"
      //val playedDate=
      //winnerName, loserName, playedDate
      assert(WinnersLosersDB.insertResults("Nina", "Rob") === 12)
    }
}
