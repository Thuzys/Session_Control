package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource

fun main() {
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
    dataSource.setURL(jdbcDatabaseURL)
    // dataSource.setURL("jdbc:postgresql://localhost/postgres?user=postgres&password=postgres")
<<<<<<<< HEAD:phase0/src/main/kotlin/pt/isel/ls/AppDB.kt
    dataSource.connection.use {
========

    dataSource.getConnection().use {
>>>>>>>> 409d3e16f7ddf726c0122045be603ddbd7312336:initial_demos_project/src/main/kotlin/pt/isel/ls/AppDb.kt
        val stm = it.prepareStatement("select * from students")
        val rs = stm.executeQuery()
        while (rs.next()) {
            println(rs.getString("name"))
        }
    }
}
