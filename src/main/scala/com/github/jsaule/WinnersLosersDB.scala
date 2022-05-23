package com.github.jsaule

import java.sql.{Connection, DriverManager, PreparedStatement}

/**
 * creates new database (if it doesn't exist) and imports game results
 * @param dbPath
 * @return new database and new tables (if they doesn't exist)
 */
class WinnersLosersDB(val dbPath: String) {
  val url = s"jdbc:sqlite:$dbPath"
  val connect: Connection = DriverManager.getConnection(url)

  /**
   * creates win_results table (if it doesn't exist)
   * to store winners/losers/time of a game
   */
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

  /**
   * takes game result and writes it into the win_results table
   * @param winnerName
   * @param loserName
   */
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

  /**
   * creates draw_results table (if it doesn't exist)
   * to store player one name/player two name/time of a game
   */
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

  /**
   * takes game result and writes it into the draw_results table
   * @param playerOneName
   * @param playerTwoName
   */
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
