import com.liferay.dynamic.data.mapping.service.DDMFieldLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil
import com.liferay.portal.kernel.model.Layout
import com.liferay.portal.kernel.model.PortletPreferences
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalServiceUtil
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil
import com.liferay.portal.kernel.workflow.WorkflowConstants

friendlyURL = "/bmw-product"

// 1. determine ids of layouts

dynamicQuery = LayoutLocalServiceUtil.dynamicQuery()
dynamicQuery.add(RestrictionsFactoryUtil.eq("friendlyURL", friendlyURL))
dynamicQuery.addOrder(OrderFactoryUtil.asc("groupId"))

layouts = LayoutLocalServiceUtil.dynamicQuery(dynamicQuery, 0, 100)

out.println("layouts.size(): " + layouts.size())

for (Layout layout : layouts) {

    groupId = layout.groupId
    plid = layout.plid

    out.println("groupId: " + groupId)
    out.println("plid: " + plid)

    portletPreferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(layout.plid)

    out.println("portletPreferences.size(): " + portletPreferences.size())
    out.println()

    for (PortletPreferences preferences : portletPreferences) {
        // out.println(preferences.portletPreferencesId)

        prefs = PortletPreferenceValueLocalServiceUtil.getPreferences(preferences)

        articleId = prefs.getValue("articleId", null)

        if (articleId != null) {

            journalArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(layout.groupId, articleId, WorkflowConstants.STATUS_APPROVED)

            content = journalArticle.getContent()

            out.println(content)

            ddmStructureId = journalArticle.getDDMStructureId()

            ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(ddmStructureId)

            if (ddmStructure != null) {
                // out.println(ddmStructure)

                ddmForm = ddmStructure.getDDMForm()

                id = journalArticle.id

                ddmFormValues = DDMFieldLocalServiceUtil.getDDMFormValues(ddmForm, id)

                ddmFormFieldValues = ddmFormValues.getDDMFormFieldValues()
            }
        }

    }

}
