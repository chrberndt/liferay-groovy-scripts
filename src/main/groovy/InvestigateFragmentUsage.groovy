// ### Report usage of fragments ###

import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;


dynamicQuery = FragmentEntryLinkLocalServiceUtil.dynamicQuery();

// Retrieve all groupIds where fragments are used
dynamicQuery.setProjection(ProjectionFactoryUtil.distinct(ProjectionFactoryUtil.property("groupId")));

groupIds = FragmentEntryLinkLocalServiceUtil.dynamicQuery(dynamicQuery);

out.println("Number of groups using fragments: " + groupIds.size());

// Loop over all fragments
fragmentEntries = FragmentEntryLocalServiceUtil.getFragmentEntries(0, Integer.MAX_VALUE); 

for (i=0; i<fragmentEntries.size(); i++) {
	
	fragmentEntry = fragmentEntries.get(i);

        totalUsages = 0;
		 		
	// And check usages in sites (groups) with fragments
	for (groupId in groupIds) {
		
		numUsages = FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinksCount(groupId, fragmentEntry.getFragmentEntryId());
		
		if (numUsages > 0) {
	 		out.println(numUsages + " usages of " + fragmentEntry.getName() + " in group " + groupId);
		}

		totalUsages = totalUsages + numUsages;

	}

	if (totalUsages > 0) {
		out.println(totalUsages + " usages of " + fragmentEntry.getName() + " in total");
	}
}
