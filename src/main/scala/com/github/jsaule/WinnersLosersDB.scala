package com.github.jsaule

import java.sql.{Connection, DriverManager, PreparedStatement}

class WinnersLosersDB(val dbPath: String) {
  val url = s"jdbc:sqlite:$dbPath"
  val connect: Connection = DriverManager.getConnection(url)
  def migrate(): Unit = {
    val statement = connect.createStatement()
    val sql =
      """
        |CREATE TABLE IF NOT EXISTS results (
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
        |INSERT INTO results (winnerName, loserName, playedDate)
        |values (?,?,CURRENT_TIMESTAMP)
    """.stripMargin

    val preparedStmt: PreparedStatement = connect.prepareStatement(insertSql)

    preparedStmt.setString(1, winnerName)
    preparedStmt.setString(2, loserName)
    preparedStmt.execute()

    preparedStmt.close()
  }
}
