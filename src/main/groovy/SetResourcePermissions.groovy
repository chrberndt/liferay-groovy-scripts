import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

// id of the layout that needs VIEW permission for Guest
primKey = 12; 

companyId = CompanyThreadLocal.getCompanyId();

guestRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST);

String[] actionIds = [ActionKeys.VIEW];

ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, Layout.class.getName(),
        ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(primKey), guestRole.getRoleId(),
        actionIds);
