// ### Fix data-lfr-editable-id ###

import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;

// Loop over all fragmentEntryLinks
fragmentEntryLinks = FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(0, Integer.MAX_VALUE); 

out.println("Processing " + fragmentEntryLinks.size() + " fragmentEntryLinks"); 

for (i=0; i<fragmentEntryLinks.size(); i++) {
	
	fragmentEntryLink = fragmentEntryLinks.get(i);

    // Cleanup html and editableValues
    if (fragmentEntryLink.html) {
        fragmentEntryLink.html = fragmentEntryLink.html.replaceAll(/data-lfr-editable\.id=\"\$\{fragmentEntryLinkNamespace\}/, "data-lfr-editable-id=\"");
        // out.println(fragmentEntryLink.html);
    }

    if (fragmentEntryLink.editableValues) {
        fragmentEntryLink.editableValues = fragmentEntryLink.editableValues.replaceAll(/\$\{fragmentEntryLinkNamespace\}/, "");
        // out.println(fragmentEntryLink.editableValues);
    }

    FragmentEntryLinkLocalServiceUtil.updateFragmentEntryLink(fragmentEntryLink);

}
