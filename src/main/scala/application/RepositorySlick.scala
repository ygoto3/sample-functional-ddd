package application

import slick.jdbc.MySQLProfile.api._

trait RepositorySlick {
  val db = Database.forURL("jdbc:mysql://localhost/sample_functional_ddd?useSSL=false", "root", "mysql", driver = "com.mysql.cj.jdbc.Driver")
}
