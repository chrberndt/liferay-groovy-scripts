import com.liferay.portal.background.task.model.BackgroundTask
import com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil
import com.liferay.portal.kernel.backgroundtask.constants.BackgroundTaskConstants
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil

dynamicQuery = BackgroundTaskLocalServiceUtil.dynamicQuery()
dynamicQuery.add(RestrictionsFactoryUtil.eq("status", BackgroundTaskConstants.STATUS_SUCCESSFUL))
//dynamicQuery.add(RestrictionsFactoryUtil.eq("status", BackgroundTaskConstants.STATUS_FAILED))
dynamicQuery.addOrder(OrderFactoryUtil.desc("createDate"))

backgroundTasks = BackgroundTaskLocalServiceUtil.dynamicQuery(dynamicQuery, 0, 10000)

out.println("backgroundTasks.size(): " + backgroundTasks.size())
out.println()

for (BackgroundTask backgroundTask : backgroundTasks) {
        out.println(backgroundTask)
}
