package com.ai.injection.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Component;

import com.ai.AdminGlobal;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.Reply;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveTask;

@Component
public class XMLGenerator {

	public String genXML(ReceiveTask receiveTask, InjectionPlatform platform)
			throws IOException {
		ADI adi = new ADI();
		Reply reply = new Reply();
		reply.setResult("" + receiveTask.getReplyResult());
		reply.setDescription(receiveTask.getReplyErrorDescription());
		adi.setReply(reply);
		return genXML(receiveTask, platform, adi);
	}

	public String genXML(ReceiveTask receiveTask, InjectionPlatform platform,
			ADI adi) throws IOException {
		Properties p = new Properties();
		p.put("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
		VelocityContext context = new VelocityContext();

		context.put("adi", adi);

		String dirPath = "/receive/"
				+ platform.getCspId()
				+ DateFormatUtils
						.format(receiveTask.getCreateTime(), "/yyyyMM");
		String fileName = platform.getCspId() + "_" + platform.getLspId() + "_"
				+ receiveTask.getCorrelateId() + "_"
				+ receiveTask.getCreateTime().getTime() + "_reply.xml";
		String filePath = dirPath + "/" + fileName;

		String templateName = "template/reply.vm";
		Template template = Velocity.getTemplate(templateName, "UTF-8");
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		File file = new File(AdminGlobal.getXmlUploadPath(dirPath));
		if (!file.exists()) {
			file.mkdirs();
		}
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				AdminGlobal.getXmlUploadPath(filePath)), "UTF-8");
		out.write(writer.toString());
		out.close();
		return filePath;
	}
}