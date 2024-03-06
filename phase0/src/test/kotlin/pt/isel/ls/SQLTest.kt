package pt.isel.ls

import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import java.sql.Connection

private const val ENV_NAME = "JDBC_DATABASE_URL"

class SQLTest {
    private fun createDataSource(envName: String = ENV_NAME): PGSimpleDataSource =
        PGSimpleDataSource().also { it.setURL(System.getenv(envName)) }

    private inline fun Connection.executeCommand(cmd: Connection.() -> Unit) {
        autoCommit = false
        cmd()
        rollback()
        autoCommit = true
    }

    @Test
    fun `insert test`() {
        val dataSource = createDataSource()
        val insertCmd = "INSERT INTO courses (name) VALUES ('LS_TEST')"
        dataSource.connection.use {
            it.executeCommand {
                val stm = prepareStatement(insertCmd)
                stm.executeUpdate()
            }
        }
    }

    @Test
    fun `select test`() {
        val dataSource = createDataSource()
        val selectCmd = "SELECT * FROM courses"
        dataSource.connection.use {
            it.executeCommand {
                val stm = prepareStatement(selectCmd)
                stm.executeQuery()
            }
        }
    }

    @Test
    fun `delete test`() {
        val dataSource = createDataSource()
        val deleteCmd = "DELETE FROM COURSES WHERE name = 'LEIC'"
        dataSource.connection.use {
            it.executeCommand {
                val stm = prepareStatement(deleteCmd)
                stm.executeUpdate()
            }
        }
    }

    @Test
    fun `update test`() {
        val dataSource = createDataSource()
        val updateCmd = "UPDATE test set name = 'Hello' where name = 'LEIC'"
        dataSource.connection.use {
            it.executeCommand {
                val stm = prepareStatement(updateCmd)
                stm.executeUpdate()
            }
        }
    }
}
