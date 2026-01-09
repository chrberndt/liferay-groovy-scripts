import com.liferay.document.library.kernel.model.DLFileEntry
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.model.ResourceConstants
import com.liferay.portal.kernel.model.ResourcePermission
import com.liferay.portal.kernel.model.role.RoleConstants
import com.liferay.portal.kernel.security.permission.ActionKeys
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil
import com.liferay.portal.kernel.service.ResourcePermissionServiceUtil
import com.liferay.portal.kernel.service.RoleLocalServiceUtil

groupId = OL // The id of the site you want to fix the permissions for
companyId = 0L // The virtual instance id
name = DLFileEntry.class.name
String[] actionIds = [ActionKeys.VIEW, ActionKeys.DOWNLOAD]
folderId = OL // Optionally: limit the operation to a particular folder

fixedFilePermissions = 0

dynamicQuery = DLFileEntryLocalServiceUtil.dynamicQuery()

dynamicQuery.addOrder(OrderFactoryUtil.asc("folderId"))
dynamicQuery.addOrder(OrderFactoryUtil.asc("fileName"))


//fileEntries = DLFileEntryLocalServiceUtil.getFileEntries(groupId, folderId)
fileEntries = DLFileEntryLocalServiceUtil.getFileEntries(0, 200000)


out.println(new Date())
out.println("fileEntries.size(): " + fileEntries.size())
out.println()

siteMemberRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.SITE_MEMBER)

for (DLFileEntry dlFileEntry : fileEntries) {

    primKey = dlFileEntry.fileEntryId.toString()
    folderName = dlFileEntry.folder.name

    resourcePermissions = ResourcePermissionLocalServiceUtil.getResourcePermissions(companyId, name, ResourceConstants.SCOPE_INDIVIDUAL, primKey)

    for (ResourcePermission resourcePermission : resourcePermissions) {

        if (siteMemberRole.roleId == resourcePermission.roleId &&
            resourcePermission.actionIds == 1) {

            out.println("folderName: " + folderName)
            out.println("fileName: " + dlFileEntry.fileName)
            out.println("actionIds: " + resourcePermission.actionIds)
            out.println()

            try {

                //
                // Uncomment the line below to actually add the download permission if
                // only the view permission is granted. The request might time out but
                // the thread will continue to run (if the system runs continously).
                //
                // While the thread is running, you can use the commented version to
                // check for the progress of the operation. Depending on the number of
                // documents the diagnostic operation alone might take 2' to 3'.
                //

                // ResourcePermissionServiceUtil.setIndividualResourcePermissions(groupId, companyId, name, primKey, siteMemberRole.roleId, actionIds)
                fixedFilePermissions++

            } catch (Exception e) {
                out.println(e.getMessage())
            }
        }
    }
}

out.println(new Date())
out.println("fixedFilePermissions: " + fixedFilePermissions)
