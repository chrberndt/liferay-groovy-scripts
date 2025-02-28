import com.liferay.portal.kernel.security.pwd.PasswordEncryptorUtil
import com.liferay.portal.kernel.service.PasswordPolicyLocalServiceUtil
import com.liferay.portal.kernel.service.UserLocalServiceUtil
import com.liferay.portal.security.pwd.PwdToolkitUtil

companyId = 20097L
emailAddress = "test@liferay.com"

user = UserLocalServiceUtil.getUserByEmailAddress(companyId, emailAddress)

out.println(user)

passwordPolicy = PasswordPolicyLocalServiceUtil
        .getPasswordPolicy(user.companyId, user.organizationIds)

password = PwdToolkitUtil.generate(passwordPolicy)

user.setPassword(PasswordEncryptorUtil.encrypt(password))
user.setPasswordUnencrypted(password)
user.setPasswordEncrypted(true);
user.setPasswordModified(true);
user.setPasswordModifiedDate(new Date());
user.setPasswordReset(false)

user = UserLocalServiceUtil.updateUser(user)

out.println(user)

