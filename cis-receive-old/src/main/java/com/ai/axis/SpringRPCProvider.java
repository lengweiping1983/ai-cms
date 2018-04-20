package com.ai.axis;

import java.util.Optional;

import javax.wsdl.OperationType;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.Handler;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.providers.BasicProvider;
import org.apache.axis.providers.java.RPCProvider;
import org.apache.axis.utils.XMLUtils;
import org.springframework.context.ApplicationContext;

public class SpringRPCProvider extends BasicProvider {
	private static final long serialVersionUID = 1L;

	private final RPCProvider rpcProvider;
	private final ApplicationContext applicationContext;

	private static final String OPTION_CLASSNAME = "className";
	private static final String OPTION_QUALIFIER = "qualifier";

	public SpringRPCProvider(ApplicationContext applicationContext) {
		rpcProvider = new RPCProvider();
		this.applicationContext = applicationContext;
	}

	@Override
	public void initServiceDesc(SOAPService service, MessageContext msgContext)
			throws AxisFault {
		rpcProvider.initServiceDesc(service, msgContext);
	}

	@Override
	public void invoke(MessageContext msgContext) throws AxisFault {

		try {
			Handler service = msgContext.getService();

			Optional<Class<?>> clazz = getOptionClass(service);
			Optional<String> qualifier = getOptionQualifier(service);

			Object obj = null;
			if (clazz.isPresent() && qualifier.isPresent()) {
				obj = applicationContext.getBean(qualifier.get(), clazz.get());
			} else if (clazz.isPresent() && qualifier.isPresent() == false) {
				obj = applicationContext.getBean(clazz.get());
			} else {
				obj = applicationContext.getBean(qualifier.get());
			}

			SOAPEnvelope resEnv = null;

			OperationDesc operation = msgContext.getOperation();
			if (operation != null
					&& OperationType.ONE_WAY.equals(operation.getMep())) {
				msgContext.setResponseMessage(null);
			} else {
				Message resMsg = msgContext.getResponseMessage();

				// If we didn't have a response message, make sure we set one up
				// with the appropriate versions of SOAP and Schema
				if (resMsg == null) {
					resEnv = new org.apache.axis.message.SOAPEnvelope(
							msgContext.getSOAPConstants(),
							msgContext.getSchemaVersion());

					resMsg = new Message(resEnv);
					String encoding = XMLUtils.getEncoding(msgContext);
					resMsg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING,
							encoding);
					msgContext.setResponseMessage(resMsg);
				} else {
					resEnv = resMsg.getSOAPEnvelope();
				}
			}

			Message reqMsg = msgContext.getRequestMessage();
			SOAPEnvelope reqEnv = reqMsg.getSOAPEnvelope();

			rpcProvider.processMessage(msgContext, reqEnv, resEnv, obj);
		} catch (SOAPException e) {
			AxisFault fault = AxisFault.makeFault(e);
			throw fault;
		} catch (AxisFault e) {
			throw e;
		} catch (Exception e) {
			AxisFault fault = AxisFault.makeFault(e);
			if (e instanceof RuntimeException) {
				fault.addFaultDetail(
						Constants.QNAME_FAULTDETAIL_RUNTIMEEXCEPTION, "true");
			}
			throw fault;
		}
	}

	protected Optional<Class<?>> getOptionClass(Handler service)
			throws ClassNotFoundException {
		String className = (String) service.getOption(OPTION_CLASSNAME);

		if (className == null)
			return Optional.empty();

		ClassLoader cl = applicationContext.getClassLoader();
		Class<?> clazz = cl.loadClass(className);

		return Optional.of(clazz);
	}

	protected Optional<String> getOptionQualifier(Handler service) {
		String qualifier = (String) service.getOption(OPTION_QUALIFIER);

		if (qualifier == null)
			return Optional.empty();

		return Optional.of(qualifier);
	}
}
