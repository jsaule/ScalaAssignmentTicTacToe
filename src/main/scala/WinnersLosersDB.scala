import java.sql.{Connection, DriverManager}

class WinnersLosersDB(val dbPath: String) {
  val url = s"jdbc:sqlite:$dbPath"
  val connect: Connection = DriverManager.getConnection(url)

}
