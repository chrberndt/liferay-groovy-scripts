import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil

friendlyURL = "/bmw-product"

// 1. determine ids of layouts

dynamicQuery = LayoutLocalServiceUtil.dynamicQuery()
dynamicQuery.add(RestrictionsFactoryUtil.eq("friendlyURL", friendlyURL))
dynamicQuery.addOrder(OrderFactoryUtil.asc("groupId"))

layouts = LayoutLocalServiceUtil.dynamicQuery(dynamicQuery, 0, 100)

out.println("layouts.size(): " + layouts.size())
