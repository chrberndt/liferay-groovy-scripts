import com.liferay.multi.factor.authentication.timebased.otp.model.MFATimeBasedOTPEntry
import com.liferay.multi.factor.authentication.timebased.otp.service.MFATimeBasedOTPEntryLocalServiceUtil

entries = MFATimeBasedOTPEntryLocalServiceUtil.getMFATimeBasedOTPEntries(0, 1000)

out.println("entries.size(): " + entries.size())
out.println()

for (MFATimeBasedOTPEntry entry : entries) {
    out.println(entry)
}