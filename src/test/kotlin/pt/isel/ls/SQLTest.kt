package pt.isel.ls

import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource

class SQLTest {

    @Test
    fun `delete from table test`(){
        val dataSource = PGSimpleDataSource()
        val jdbcDatabaseURL = System.getenv("JDBC_TEST_URL")
        dataSource.setURL(jdbcDatabaseURL)

        dataSource.connection.use {
            it.autoCommit = false
            val stm1= it.prepareStatement(
                "DELETE FROM COURSES " +
                        "WHERE name = 'LEIC'"
            )
            stm1.executeUpdate()

            it.rollback()
            it.autoCommit = true
        }
    }
}