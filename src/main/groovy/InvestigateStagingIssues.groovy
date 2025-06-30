import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMFieldLocalServiceUtil
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil
import com.liferay.portal.kernel.json.JSONFactoryUtil
import com.liferay.portal.kernel.model.Layout
import com.liferay.portal.kernel.model.PortletPreferences
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalServiceUtil
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil
import com.liferay.portal.kernel.service.ServiceContext
import com.liferay.portal.kernel.util.LocaleUtil
import com.liferay.portal.kernel.workflow.WorkflowConstants


stagingGroupId = 1386890L
// stagingGroupId = 33700L
liveGroupId = 419619L
// liveGroupId = 34559L

dynamicQuery = LayoutLocalServiceUtil.dynamicQuery()
dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", liveGroupId))
// dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", stagingGroupId))
dynamicQuery.addOrder(OrderFactoryUtil.asc("plid"))

anonymousUserId = 0

layouts = LayoutLocalServiceUtil.dynamicQuery(dynamicQuery, 0, 5000)

out.println("layouts.size(): " + layouts.size())

for (Layout layout : layouts) {

    groupId = layout.groupId
    plid = layout.plid

    portletPreferences = PortletPreferencesLocalServiceUtil.getPortletPreferencesByPlid(layout.plid)

    for (PortletPreferences preferences : portletPreferences) {

        prefs = PortletPreferenceValueLocalServiceUtil.getPreferences(preferences)

        articleId = prefs.getValue("articleId", null)

        if (articleId != null) {

            journalArticle = JournalArticleLocalServiceUtil.fetchLatestArticle(layout.groupId, articleId, WorkflowConstants.STATUS_APPROVED)

            if (journalArticle != null) {

                content = journalArticle.getContent()

                ddmStructureId = journalArticle.getDDMStructureId()

                ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(ddmStructureId)

                if (ddmStructure != null) {

                    ddmForm = ddmStructure.getDDMForm()

                    id = journalArticle.id

                    ddmFormValues = DDMFieldLocalServiceUtil.getDDMFormValues(ddmForm, id)

                    ddmFormFieldValues = ddmFormValues.getDDMFormFieldValues()

                    for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {

                        firstNestedFormFieldValues = ddmFormFieldValue.getNestedDDMFormFieldValues()

                        for (DDMFormFieldValue firstNestedFormFieldValue : firstNestedFormFieldValues) {

                            secondNestedFormFieldValues = firstNestedFormFieldValue.getNestedDDMFormFieldValues()

                            for (DDMFormFieldValue secondNestedFormFieldValue : secondNestedFormFieldValues) {

                                value = secondNestedFormFieldValue.value

                                if (value != null) {

                                    localizedValue = value.getString(LocaleUtil.siteDefault)

                                    if (localizedValue != null && localizedValue.startsWith("{")) {

                                        out.println()

                                        jsonObject = JSONFactoryUtil.createJSONObject(localizedValue)
                                        out.println("jsonObject: " + jsonObject)

                                        imageGroupId = jsonObject.getLong("groupId");
                                        out.println("imageGroupId: " + imageGroupId)

                                        fileEntryId = jsonObject.getLong("fileEntryId")
                                        out.println("fileEntryId: " + fileEntryId)

                                        uuid = jsonObject.getString("uuid")
                                        out.println("uuid: " + uuid)

                                        if (fileEntryId > 0) {

                                            stagingFileEntry = null
                                            liveFileEntry = null

                                            try {
                                                stagingFileEntry = DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(uuid, stagingGroupId)
                                            } catch (Exception ignore) {
                                                out.println("ERROR: Could not find stagingFileEntry with uuid " + uuid)
                                            }

                                            try {
                                                liveFileEntry = DLAppLocalServiceUtil.getFileEntryByUuidAndGroupId(uuid, liveGroupId)
                                            } catch (Exception ignore) {
                                                out.println("ERROR: Could not find liveFileEntry with uuid " + uuid)
                                            }

                                            out.println("stagingFileEntry: " + stagingFileEntry)
                                            out.println("liveFileEntry: " + liveFileEntry)

                                            if ((stagingFileEntry != null) && (liveFileEntry != null)) {

                                                groupIdFrom = "{\"groupId\":\"" + stagingFileEntry.groupId + "\""
                                                groupIdTo = "{\"groupId\":\"" + liveFileEntry.groupId + "\""

                                                fileEntryIdFrom = "\"fileEntryId\":\"" + stagingFileEntry.fileEntryId + "\""
                                                fileEntryIdTo = "\"fileEntryId\":\"" + liveFileEntry.fileEntryId + "\""

                                                content = content.replace(groupIdFrom, groupIdTo)
                                                content = content.replace(fileEntryIdFrom, fileEntryIdTo)

                                                serviceContext = new ServiceContext()
                                                serviceContext.setScopeGroupId(journalArticle.groupId)

                                                ExportImportThreadLocal.setLayoutImportInProcess(true)

//                                                JournalArticleLocalServiceUtil.updateArticle(anonymousUserId,
//                                                        journalArticle.groupId,
//                                                        journalArticle.folderId,
//                                                        journalArticle.articleId,
//                                                        journalArticle.version,
//                                                        content,
//                                                        serviceContext)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
