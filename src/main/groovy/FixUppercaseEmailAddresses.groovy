import com.liferay.portal.kernel.model.User
import com.liferay.portal.kernel.service.UserLocalServiceUtil

users = UserLocalServiceUtil.getUsers(0, 5000)

out.println("users.size(): " + users.size())
out.println()

for (User user : users) {

    emailAddress = user.emailAddress

    if (emailAddress != null && emailAddress.matches(".*[A-B].*")) {
        out.println(emailAddress)
        out.println(emailAddress.toLowerCase())
//        user.emailAddress = emailAddress.toLowerCase()
//        UserLocalServiceUtil.updateUser(user)
    }
}
