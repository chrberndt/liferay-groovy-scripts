import com.liferay.portal.kernel.dao.jdbc.DataAccess

import java.sql.ResultSet

connection = DataAccess.connection

preparedStatement = connection.prepareStatement("SELECT * FROM configuration_ ORDER BY configurationid")

ResultSet resultSet = preparedStatement.executeQuery()

while (resultSet.next()) {
    configurationId = resultSet.getString("configurationid")
    dictionary = resultSet.getString("dictionary")

    out.println(configurationId)
    out.println(dictionary)
    out.println()
}
