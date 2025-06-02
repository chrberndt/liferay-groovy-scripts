import com.liferay.dynamic.data.mapping.model.DDMContent
import com.liferay.dynamic.data.mapping.service.DDMContentLocalServiceUtil

count = DDMContentLocalServiceUtil.getDDMContentsCount()
ddmContents = DDMContentLocalServiceUtil.getDDMContents(0,200000)
occurences = 0;

out.println("count: " + count)
out.println()

for (DDMContent ddmContent : ddmContents) {
    data = ddmContent.data
    if (data.contains("\\u0000")) {
        out.println(ddmContent.contentId + " " + data.replace("\\u0000", "###### NULL #####"))
        occurences++
    }
}

out.println()
out.println("occurences: " + occurences);
