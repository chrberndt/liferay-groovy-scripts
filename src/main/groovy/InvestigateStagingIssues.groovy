import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMFieldLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil
import com.liferay.portal.kernel.json.JSONFactoryUtil
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

stagingGroupId = 1386890L
liveGroupId = 419619L

layouts = LayoutLocalServiceUtil.dynamicQuery(dynamicQuery, 0, 100)

// out.println("layouts.size(): " + layouts.size())

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

            // out.println(content)

            ddmStructureId = journalArticle.getDDMStructureId()

            ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(ddmStructureId)

            // out.println("ddmStructure: " + ddmStructure)

            if (ddmStructure != null) {
                // out.println(ddmStructure)

                ddmForm = ddmStructure.getDDMForm()

                id = journalArticle.id

                ddmFormValues = DDMFieldLocalServiceUtil.getDDMFormValues(ddmForm, id)

                // out.println("ddmFormValues: " + ddmFormValues)

                ddmFormFieldValues = ddmFormValues.getDDMFormFieldValues()

                // out.println("ddmFormFieldValues: " + ddmFormFieldValues)

                for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {

                    value = ddmFormFieldValue.value

                    out.println("value: " + value)

                    nestedFormFieldValues = ddmFormFieldValue.getNestedDDMFormFieldValues()

                    out.println("nestedFormFieldValues.size(): " + nestedFormFieldValues.size())

                    if (value != null) {

                        localizedValue = value.getString(Locale.US)
                        // out.println(localizedValue)

                        if (localizedValue != null && localizedValue.startsWith("{")) {

                            jsonObject = JSONFactoryUtil.createJSONObject(localizedValue)

                            imageGroupId = jsonObject.getLong("groupId");
                            out.printl("imageGroupId: " + imageGroupId)

                            fileEntryId = jsonObject.getLong("fileEntryId")
                            out.printl("fileEntryId: " + fileEntryId)

                            uuid = jsonObject.getString("uuid")
                            out.printl("uuid: " + uuid)

//                            if (imageGroupId == staginGroupId && fileEntryId > 0) {
//
//                                stagingFileEntry = DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(uuid, staginGroupId)
//                                liveFileEntry = DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(uuid, liveGroupId)
//
//                                out.println("stagingFileEntry: " + stagingFileEntry)
//                                out.println("liveFileEntry: " + liveFileEntry)
//                            }
                        }
                    }
                }
            }
        }
    }
}
