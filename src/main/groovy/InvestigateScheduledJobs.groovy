import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil
import com.liferay.portal.kernel.scheduler.StorageType
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse

schedulerResponses = SchedulerEngineHelperUtil.getScheduledJobs(StorageType.PERSISTED)

for (SchedulerResponse schedulerResponse : schedulerResponses ) {
    out.println(SchedulerEngineHelperUtil.getStartTime(schedulerResponse))
    out.println(SchedulerEngineHelperUtil.getEndTime(schedulerResponse))
    out.println(SchedulerEngineHelperUtil.getNextFireTime(schedulerResponse))
    out.println(SchedulerEngineHelperUtil.getJobState(schedulerResponse))
    out.println(schedulerResponse)
    // SchedulerEngineHelperUtil.delete(schedulerResponse.jobName, schedulerResponse.groupName, StorageType.PERSISTED)
}
