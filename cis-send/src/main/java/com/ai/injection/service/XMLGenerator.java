package com.ai.injection.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ai.AdminGlobal;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.common.enums.YesNoEnum;

@Component
public class XMLGenerator {

	public String genXML(SendTask sendTask, InjectionPlatform platform, ADI adi)
			throws IOException {
		Properties p = new Properties();
		p.put("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
		VelocityContext context = new VelocityContext();

		if (sendTask.getPriority() != null) {
			adi.setPriority(sendTask.getPriority());
		} else {
			adi.setPriority(1);
		}
		adi.setStaffID(sendTask.getCorrelateId());

		context.put("platform", platform);
		context.put("adi", adi);

		String dirPath = "/send/" + platform.getCspId()
				+ DateFormatUtils.format(sendTask.getCreateTime(), "/yyyyMM");
		String fileName = platform.getCspId() + "_" + platform.getLspId() + "_"
				+ sendTask.getCorrelateId() + "_"
				+ sendTask.getCreateTime().getTime() + ".xml";
		String filePath = dirPath + "/" + fileName;

		String templateName = "template/template.vm";
		if (platform.getTemplateCustom() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(platform.getTemplateFilename())) {
			String templateFilename = StringUtils.trimToEmpty(platform
					.getTemplateFilename());
			templateName = "template/template_" + templateFilename + ".vm";
		}

		Template template = Velocity.getTemplate(templateName, "UTF-8");
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		File file = new File(AdminGlobal.getXmlUploadPath() + dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				AdminGlobal.getXmlUploadPath() + filePath), "UTF-8");
		out.write(writer.toString());
		out.close();
		return filePath;
	}
}