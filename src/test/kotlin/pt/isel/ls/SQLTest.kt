package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.Test

val ENV_NAME = "JDBC_DATABASE_URL"
class SQLTest {
    @Test
    fun `update test`() {
        val dataSource = PGSimpleDataSource()
        val UPDATE_CMD = "UPDATE test set name = 'Hello' where name = 'LEIC'"
        val jdbcDatabaseURL = System.getenv(ENV_NAME)
        dataSource.setURL(jdbcDatabaseURL)
        dataSource.connection.use {
            it.autoCommit = false
            val  stm = it.prepareStatement(UPDATE_CMD)
            stm.executeUpdate()
            it.rollback()
            it.autoCommit = false
        }
    }



}
