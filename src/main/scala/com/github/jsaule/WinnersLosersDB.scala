package com.github.jsaule

import java.sql.{Connection, DriverManager, PreparedStatement}

class WinnersLosersDB(val dbPath: String) {
  val url = s"jdbc:sqlite:$dbPath"
  val connect: Connection = DriverManager.getConnection(url)
  def migrate(): Unit = {
    val statement = connect.createStatement()
    val sql =
      """
        |CREATE TABLE IF NOT EXISTS win_results (
        |id INTEGER PRIMARY KEY,
        |winnerName TEXT NOT NULL,
        |loserName TEXT NOT NULL,
        |playedDate TEXT
        |);
)
        |""".stripMargin

    statement.execute(sql)
  }

  def insertResults(winnerName: String, loserName: String): Unit = {

    val insertSql =
      """
        |INSERT INTO win_results (winnerName, loserName, playedDate)
        |values (?,?,CURRENT_TIMESTAMP)
    """.stripMargin

    val preparedStmt: PreparedStatement = connect.prepareStatement(insertSql)

    preparedStmt.setString(1, winnerName)
    preparedStmt.setString(2, loserName)
    preparedStmt.execute()

    preparedStmt.close()
  }

  def migrateDraw(): Unit = {
    val statement = connect.createStatement()
    val sql =
      """
        |CREATE TABLE IF NOT EXISTS draw_results (
        |id INTEGER PRIMARY KEY,
        |playerOneName TEXT NOT NULL,
        |playerTwoName TEXT NOT NULL,
        |playedDate TEXT
        |);
)
        |""".stripMargin

    statement.execute(sql)
  }

  def insertDrawResults(playerOneName: String, playerTwoName: String): Unit = {

    val insertSql =
      """
        |INSERT INTO draw_results (playerOneName, playerTwoName, playedDate)
        |values (?,?,CURRENT_TIMESTAMP)
    """.stripMargin

    val preparedStmt: PreparedStatement = connect.prepareStatement(insertSql)

    preparedStmt.setString(1, playerOneName)
    preparedStmt.setString(2, playerTwoName)
    preparedStmt.execute()

    preparedStmt.close()
  }
}
