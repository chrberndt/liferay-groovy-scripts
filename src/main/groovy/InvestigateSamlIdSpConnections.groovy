import com.liferay.saml.persistence.model.SamlSpIdpConnection
import com.liferay.saml.persistence.service.SamlSpIdpConnectionLocalServiceUtil

connections = SamlSpIdpConnectionLocalServiceUtil.getSamlSpIdpConnections(0, 10)

for (SamlSpIdpConnection connection : connections) {
    out.println(connection)
}

